package cn.tripcode.trip.article.controller;

import cn.tripcode.trip.article.domain.Strategy;
import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.domain.StrategyContent;
import cn.tripcode.trip.article.domain.StrategyRank;
import cn.tripcode.trip.article.qo.StrategyQuery;
import cn.tripcode.trip.article.service.StrategyRankService;
import cn.tripcode.trip.article.service.StrategyService;
import cn.tripcode.trip.article.utils.OssUtil;
import cn.tripcode.trip.article.vo.StrategyCondition;
import cn.tripcode.trip.auth.anno.RequireLogin;
import cn.tripcode.trip.core.qo.QueryObject;
import cn.tripcode.trip.core.utils.R;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/strategies")
public class StrategyController {
    private final StrategyService strategyService;
    private final StrategyRankService strategyRankService;
    public StrategyController(StrategyService strategyService, StrategyRankService strategyRankService) {
        this.strategyService = strategyService;
        this.strategyRankService = strategyRankService;
    }
    @PostMapping("/search")
    public R<List<Strategy>> searchList(@RequestBody QueryObject qo) {
        return R.ok(strategyService.list(new QueryWrapper<Strategy>().last("limit " + qo.getOffset() + ", " + qo.getSize())));
    }
    @GetMapping("/getById")
    public Strategy getById(Long id) {
        return strategyService.getById(id);
    }
    @GetMapping("/findByDestName")
    public R<List<Strategy>> findByDestName(@RequestParam String destName) {
        return R.ok(strategyService.list(new QueryWrapper<Strategy>().eq("dest_name", destName)));
    }
    @GetMapping("/stat/data")
    public R<Map<String, Object>> getStatData(Long id) {
        Map<String, Object> ret = strategyService.getStatData(id);
        return R.ok(ret);
    }
    @RequireLogin
    @GetMapping("/thumbnumIncr")
    public R<Boolean> thumbnumIncr(Long sid){
        boolean ret=strategyService.thumbnumIncr(sid);
        return R.ok(ret);
    }
    @GetMapping("/query")
    public R<Page<Strategy>> pageList(StrategyQuery qo) {

        return R.ok(strategyService.pageList(qo));
    }
    @GetMapping("/groups")
    public R<List<StrategyCatalog>> findGroupByDestId(Long destId) {
        return R.ok(strategyService.findGroupsByDestId(destId));
    }
    @GetMapping("/ranks")
    public R<JSONObject> findRanks(Long destId) {
        List<StrategyRank> abroadRank=strategyRankService.selectLastRanksByType(StrategyRank.TYPE_ABROAD);
        List<StrategyRank> chinaRank=strategyRankService.selectLastRanksByType(StrategyRank.TYPE_CHINA);
        List<StrategyRank> hotRank=strategyRankService.selectLastRanksByType(StrategyRank.TYPE_HOT);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("abroadRank",abroadRank);
        jsonObject.put("chinaRank",chinaRank);
        jsonObject.put("hotRank",hotRank);
        return R.ok(jsonObject);
    }
    @GetMapping("/detail")
    public R<Strategy> detail(Long id) {
        // 更新浏览量
        strategyService.ViewnumIncr(id);
        return R.ok(strategyService.getById(id));
    }
    @GetMapping("/conditions")
    public R<Map<String, List<StrategyCondition>>> getConditions() {
        Map<String, List<StrategyCondition>> map = new HashMap<>();
        List<StrategyCondition> chinaCondition= strategyService.findDestCondition(Strategy.ABROAD_NO);
        List<StrategyCondition> abroadCondition= strategyService.findDestCondition(Strategy.ABROAD_YES);
        List<StrategyCondition> themeCondition= strategyService.findthemeCondition();
        map.put("chinaCondition",chinaCondition);
        map.put("abroadCondition",abroadCondition);
        map.put("themeCondition",themeCondition);
        return R.ok(map);
    }
    @GetMapping("/content")
    public R<StrategyContent> getContentById(Long id) {
        return R.ok(strategyService.getContentById(id));
    }
    @GetMapping("/viewnumTop3")
    public R<List<Strategy>> viewnumTop3(Long destId) {
      return R.ok(strategyService.findViewnumTop3ByDestId(destId));
    }
    @PostMapping("/uploadImg")
    public JSONObject uploadImg(MultipartFile upload){
        JSONObject jsonObject = new JSONObject();
        if (upload == null || upload.isEmpty()) {
            jsonObject.put("uploaded",0);
            JSONObject error = new JSONObject();
            error.put("message","请选择要上传的文件");
            jsonObject.put("error",error);
            return jsonObject;
        }
        String fileName=
                upload.getOriginalFilename().substring(0,upload.getOriginalFilename().lastIndexOf("."))
                +"_"+System.currentTimeMillis();
        String url = OssUtil.upload("images",fileName,upload);
        jsonObject.put("uploaded",1);
        jsonObject.put("fileName",upload.getOriginalFilename());
        jsonObject.put("url",url);
        return jsonObject;
    }
    @PostMapping("/save")
    public R<?> save(Strategy strategy) {
        strategyService.save(strategy);
        return R.ok();
    }
    @PostMapping("/update")
    public R<?> updateById(Strategy strategy) {
        strategyService.updateById(strategy);
        return R.ok();
    }
    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        strategyService.removeById(id);
        return R.ok();
    }
}
