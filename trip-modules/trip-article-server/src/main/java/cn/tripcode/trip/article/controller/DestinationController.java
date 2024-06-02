package cn.tripcode.trip.article.controller;

import cn.tripcode.trip.article.service.DestinationService;
import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.article.domain.Destination;
import cn.tripcode.trip.article.qo.DestinationQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }
    @GetMapping
    public R<Page<Destination>> pageList(DestinationQuery query) {
        return R.ok(destinationService.pageList(query));
    }
    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId) {

        return R.ok(destinationService.findToasts(destId));
    }
    @GetMapping("/hotList")
    public R<List<Destination>> hotList(Long rid) {

        return R.ok(destinationService.findDestsByRid(rid));
    }

    @GetMapping("/list")
    public R<List<Destination>> listAll() {
        return R.ok(destinationService.list());
    }
    @GetMapping("/detail")
    public R<Destination> getById(Long id) {
        return R.ok(destinationService.getById(id));
    }
    @PostMapping("/save")
    public R<?> save(Destination dest) {
        destinationService.save(dest);
        return R.ok();
    }
    @PostMapping("/update")
    public R<?> updateById(Destination dest) {
        destinationService.updateById(dest);
        return R.ok();
    }
    @PostMapping("/delete")
    public R<?> deleteById(Long id) {
        destinationService.removeById(id);
        return R.ok();
    }
}
