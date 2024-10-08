package cn.tripcode.trip.article.controller;

import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.service.StrategyCatalogService;
import cn.tripcode.trip.article.vo.StrategyCatalogGroup;
import cn.tripcode.trip.core.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategies/catalogs")
public class StrategyCatalogController {
    private final StrategyCatalogService strategyCatalogService;
    public StrategyCatalogController(StrategyCatalogService strategyCatalogService) {
        this.strategyCatalogService = strategyCatalogService;
    }
    @GetMapping("/query")
    public R<Page<StrategyCatalog>> pageList(Page<StrategyCatalog> page) {
        return R.ok(strategyCatalogService.page(page));
    }
    @GetMapping("groups")
    public R<List<StrategyCatalogGroup>> groupList() {
        return R.ok(strategyCatalogService.findGroupList());
    }

    @GetMapping("/detail")
    public R<StrategyCatalog> getById(Long id) {
        return R.ok(strategyCatalogService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(StrategyCatalog strategyCatalog) {
        strategyCatalogService.save(strategyCatalog);
        return R.ok();
    }
    @PostMapping("/update")
    public R<?> updateById(StrategyCatalog strategyCatalog) {
        strategyCatalogService.updateById(strategyCatalog);
        return R.ok();
    }
    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        strategyCatalogService.removeById(id);
        return R.ok();
    }
}
