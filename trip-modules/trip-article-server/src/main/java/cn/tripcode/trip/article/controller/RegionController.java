package cn.tripcode.trip.article.controller;

import cn.tripcode.trip.article.service.DestinationService;
import cn.tripcode.trip.article.service.RegionService;
import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.article.domain.Destination;
import cn.tripcode.trip.article.domain.Region;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {
    private final RegionService regionService;
    private final DestinationService destinationService;
    public RegionController(RegionService regionService, DestinationService destinationService) {
        this.regionService = regionService;
        this.destinationService = destinationService;
    }
    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page) {
        return R.ok(regionService.page(page));
    }

    @GetMapping("/detail")
    public R<Region> getById(Long id) {
        return R.ok(regionService.getById(id));
    }

    @GetMapping("/hotList")
    public R<List<Region>> hotList() {
        return R.ok(regionService.findHotList());
    }
    @PostMapping("/save")
    public R<?> save(Region region) {
        regionService.save(region);
        return R.ok();
    }
    @PostMapping("/update")
    public R<?> updateById(Region region) {
        regionService.updateById(region);
        return R.ok();
    }
    @GetMapping("/{id}/destination")
    public R<List<Destination>> getDestinationByRegionId(@PathVariable Long id) {
        return R.ok(destinationService.getDestinationByRegionId(id));
    }
    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        regionService.removeById(id);
        return R.ok();
    }
}
