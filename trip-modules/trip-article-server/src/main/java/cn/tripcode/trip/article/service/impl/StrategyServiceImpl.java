package cn.tripcode.trip.article.service.impl;

import cn.tripcode.trip.article.domain.*;
import cn.tripcode.trip.article.feign.UserInfoFeignService;
import cn.tripcode.trip.article.mapper.StrategyContentMapper;
import cn.tripcode.trip.article.mapper.StrategyMapper;
import cn.tripcode.trip.article.qo.StrategyQuery;
import cn.tripcode.trip.article.redis.key.StrategyRedisKeyPrefix;
import cn.tripcode.trip.article.service.DestinationService;
import cn.tripcode.trip.article.service.StrategyCatalogService;
import cn.tripcode.trip.article.service.StrategyService;
import cn.tripcode.trip.article.service.StrategyThemeService;
import cn.tripcode.trip.article.utils.OssUtil;
import cn.tripcode.trip.article.vo.StrategyCondition;
import cn.tripcode.trip.auth.utils.AuthenticationUtils;
import cn.tripcode.trip.core.utils.DateUtils;
import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.redis.utils.RedisCache;
import cn.tripcode.trip.user.vo.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
    private final UserInfoFeignService userInfoFeignService;
    private final RedisCache redisCache;
    private final StrategyCatalogService  strategyCatalogService;
    private final DestinationService destinationService;
    private  final StrategyThemeService  strategyThemeService;
    private final StrategyContentMapper strategyContentMapper;
    public StrategyServiceImpl(StrategyCatalogService strategyCatalogService,
                               DestinationService destinationService,
                               StrategyThemeService strategyThemeService,
                               StrategyContentMapper strategyContentMapper,
                               RedisCache redisCache,
                               @Lazy UserInfoFeignService userInfoFeignService) {
        this.strategyCatalogService = strategyCatalogService;
        this.destinationService = destinationService;
        this.strategyThemeService = strategyThemeService;
        this.strategyContentMapper = strategyContentMapper;
        this.redisCache = redisCache;
        this.userInfoFeignService = userInfoFeignService;
    }
    @Override
    public  Strategy getById(Serializable id){
        Strategy strategy = super.getById(id);
        StrategyContent content = strategyContentMapper.selectById(id);
        strategy.setContent(content);
        // 查询当前用户是否已收藏攻略
        LoginUser user = AuthenticationUtils.getUser();
        if (user != null) {
            R<List<Long>> favoriteStrategyIdList = userInfoFeignService.getFavorStrategyIdList(user.getId());
            List<Long> list = favoriteStrategyIdList.checkAndGet();
            strategy.setFavorite(list.contains(id));
        }
        // 从 redis 中查询最新的统计数据
        Map<String, Object> statData = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
        if (statData != null) {
            strategy.setViewnum((Integer) statData.get("viewnum"));
            strategy.setReplynum((Integer) statData.get("replynum"));
            strategy.setFavornum((Integer) statData.get("favornum"));
            strategy.setSharenum((Integer) statData.get("sharenum"));
            strategy.setThumbsupnum((Integer) statData.get("thumbsupnum"));
        }
        return strategy;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Strategy entity) {
        return doSaveOrUpdate(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Strategy entity) {
        return doSaveOrUpdate(entity);
    }

    private boolean doSaveOrUpdate(Strategy entity) {
        // 1. 完成封面图片的上传, 且得到 url 后重新设置到 cover 属性中
        if (!StringUtils.isEmpty(entity.getCoverUrl()) || !entity.getCoverUrl().startsWith("http")) {
            String fileName = UUID.randomUUID().toString();
            String url = OssUtil.uploadImgByBase64("images/strategies", fileName + ".jpg", entity.getCoverUrl());
            entity.setCoverUrl(url);
        }
        // 2. 补充分类名称
        StrategyCatalog catalog = strategyCatalogService.getById(entity.getCatalogId());
        entity.setCatalogName(catalog.getName());
        // 3. 根据分类中的目的地id/名称设置到攻略对象中
        entity.setDestId(catalog.getDestId());
        entity.setDestName(catalog.getDestName());
        // 4. 基于目的地判断是否是国外
        List<Destination> toasts = destinationService.findToasts(catalog.getDestId());
        Destination dest = toasts.get(0);
        if (dest.getId() == 1) {
            entity.setIsabroad(Strategy.ABROAD_NO);
        } else {
            entity.setIsabroad(Strategy.ABROAD_YES);
        }
        // 5. 查询主题, 设置主题名称
        StrategyTheme theme = strategyThemeService.getById(entity.getThemeId());
        entity.setThemeName(theme.getName());

        // 判断是更新还是新增
        if (entity.getId() == null) {
            // 6. 设置创建时间
            entity.setCreateTime(new Date());
            // 7. 设置各种数量为0
            entity.setViewnum(0);
            entity.setSharenum(0);
            entity.setThumbsupnum(0);
            entity.setReplynum(0);
            entity.setFavornum(0);
            // 8. 重新设置默认状态, 覆盖前端提交的值
            entity.setState(Strategy.STATE_NORMAL);
            // 9. 保存攻略对象, 得到攻略自增的 id
            boolean save = super.save(entity);
            // 10. 将攻略 id 设置到内容对象中, 并保存内容对象
            StrategyContent content = entity.getContent();
            content.setId(entity.getId());
            return save && strategyContentMapper.insert(content) > 0;
        }

        // 更新操作
        boolean ret = super.updateById(entity);
        StrategyContent content = entity.getContent();
        content.setId(entity.getId());
        int row = strategyContentMapper.updateById(entity.getContent());
        return ret && row > 0;
    }

    @Override
    public List<StrategyCatalog> findGroupsByDestId(Long destId) {
        return getBaseMapper().selectGroupsByDestId(destId);
    }

    @Override
    public StrategyContent getContentById(Long id) {
        return strategyContentMapper.selectById(id);
    }

    @Override
    public List<Strategy> findViewnumTop3ByDestId(Long destId) {
        //查询指定攻略下的前三项
        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq("dest_id", destId)
                .orderByDesc("viewnum")
                .last("limit 3");
        return list(wrapper);
    }

    @Override
    public Page<Strategy> pageList(StrategyQuery qo) {
        if(qo.getType()!=null&&qo.getType()>0&&qo.getRefid()!=null&&qo.getRefid()!=-1){
            if(qo.getType()==3){
                qo.setThemeId(qo.getRefid());
            }
            else {
                qo.setDestId(qo.getRefid());
            }
        }
        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq(qo.getDestId() != null, "dest_id", qo.getDestId())
                .eq(qo.getThemeId() != null, "theme_id", qo.getThemeId())
                .orderByDesc(!StringUtils.isEmpty(qo.getOrderBy()), qo.getOrderBy());
        return super.page(new Page<>(qo.getCurrent(), qo.getSize()),wrapper);
    }

    @Override
    public List<StrategyCondition> findDestCondition(int abroad) {
        return getBaseMapper().selectDestCondition(abroad);
    }

    @Override
    public List<StrategyCondition> findthemeCondition() {
        return getBaseMapper().selectThemeCondition();
    }

    @Override
    public void ViewnumIncr(Long id) {
        this.statDataIncr("viewnum",id);

    }

    @Override
    public boolean thumbnumIncr(Long sid) {
        LoginUser user= AuthenticationUtils.getUser();
        StrategyRedisKeyPrefix keyPrefix=StrategyRedisKeyPrefix.STRATEGIES_TOP_MAP;
        String fullKey =keyPrefix.fullKey(sid+"");
        Integer count = redisCache.getCacheMapValue(fullKey,user.getId()+"");
        if(count!=null&&count>0){
            return false;
        }
        keyPrefix.setTimeout(DateUtils.getLastMillisSeconds());
        keyPrefix.setUnit(TimeUnit.MILLISECONDS);
        redisCache.hashIncrement(keyPrefix,user.getId()+"",1,sid+"");
        this.statDataIncr("thumbsupnum",sid);
        return true;
    }
    private void statDataIncr(String hashKey,Long sid){
        redisCache.hashIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP,
                hashKey,1,sid+"");
        // 记录操作次数
        redisCache.zsetIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 1, sid);
    }

    @Override
    public Map<String, Object> getStatData(Long id) {
        return redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
    }
}
