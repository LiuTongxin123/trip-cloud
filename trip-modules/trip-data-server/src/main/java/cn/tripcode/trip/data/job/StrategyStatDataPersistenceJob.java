package cn.tripcode.trip.data.job;

import cn.tripcode.trip.article.domain.Strategy;
import cn.tripcode.trip.article.redis.key.StrategyRedisKeyPrefix;
import cn.tripcode.trip.data.service.StrategyService;
import cn.tripcode.trip.redis.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class StrategyStatDataPersistenceJob {

    private final RedisCache redisCache;
    private final StrategyService strategyService;

    public StrategyStatDataPersistenceJob(RedisCache redisCache, StrategyService strategyService) {
        this.redisCache = redisCache;
        this.strategyService = strategyService;
    }

     @Scheduled(cron = "0 */10 * * * *")
//    @Scheduled(cron = "0/10 * * * * *")
    public void task() {
        log.info("[攻略数据持久化] --------------持久化数据开始--------------");
        // 1. 根据分数范围获取指定的成员
        Set<Integer> list = redisCache.zsetRevrange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
        if (list != null && list.size() > 0) {
            // 2. 根据成员id, 拼接 key 取出统计数据
            List<Strategy> updateList = new ArrayList<>();
            for (Integer id : list) {
                Map<String, Object> map = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
                // 3. 将数据封装为攻略对象, 将对象存入待更新的集合
                Strategy strategy = new Strategy();
                strategy.setViewnum((Integer) map.get("viewnum"));
                strategy.setSharenum((Integer) map.get("sharenum"));
                strategy.setReplynum((Integer) map.get("replynum"));
                strategy.setFavornum((Integer) map.get("favornum"));
                strategy.setThumbsupnum((Integer) map.get("thumbsupnum"));
                strategy.setId(id.longValue());

                updateList.add(strategy);
            }
            // 4. 批量更新到数据库
            strategyService.updateBatchById(updateList, 100);
            // 5. 删除已经更新过的成员
            redisCache.zsetRemoveRange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
        }
        log.info("[攻略数据持久化] 持久化数量: {}", list.size());
        log.info("[攻略数据持久化] --------------持久化数据结束--------------");
    }
}
