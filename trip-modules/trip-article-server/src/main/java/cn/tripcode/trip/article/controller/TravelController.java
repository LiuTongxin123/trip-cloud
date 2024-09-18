package cn.tripcode.trip.article.controller;

import cn.tripcode.trip.article.domain.Travel;
import cn.tripcode.trip.article.qo.TravelQuery;
import cn.tripcode.trip.article.service.TravelService;
import cn.tripcode.trip.auth.anno.RequireLogin;
import cn.tripcode.trip.core.qo.QueryObject;
import cn.tripcode.trip.core.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/travels")
public class TravelController {
    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }
    @GetMapping("/query")
    public R<Page<Travel>> pageList(TravelQuery query) {
        return R.ok(travelService.pageList(query));
    }
    @GetMapping("/findByDestName")
    public R<List<Travel>> findByDestName(@RequestParam String destName) {
        return R.ok(travelService.list(new QueryWrapper<Travel>().eq("dest_name", destName)));
    }

    @PostMapping("/search")
    public R<List<Travel>> searchList(@RequestBody QueryObject qo) {
        return R.ok(travelService.list(new QueryWrapper<Travel>().last("limit " + qo.getOffset() + ", " + qo.getSize())));
    }
    @GetMapping("/detail")
    public R<Travel> getById(Long id) {
        return R.ok(travelService.getById(id));
    }
    @PostMapping("/save")
    public R<?> save(Travel dest) {
        travelService.save(dest);
        return R.ok();
    }
    @PostMapping("/update")
    public R<?> updateById(Travel dest) {
        travelService.updateById(dest);
        return R.ok();
    }
    @PostMapping("/delete")
    public R<?> deleteById(Long id) {
        travelService.removeById(id);
        return R.ok();
    }
}
