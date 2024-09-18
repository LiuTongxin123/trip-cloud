package cn.tripcode.trip.comment.controller;

import cn.tripcode.trip.auth.anno.RequireLogin;
import cn.tripcode.trip.comment.service.TravelCommentService;
import cn.tripcode.trip.core.utils.R;
import comment.domain.TravelComment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travels/comments")
public class TravelCommentController {
    private final TravelCommentService travelCommentService;

    public TravelCommentController(TravelCommentService travelCommentService) {
        this.travelCommentService = travelCommentService;
    }
    @GetMapping("/query")
    public  R<List<TravelComment>> query(Long travelId){
        return R.ok(travelCommentService.findList(travelId));
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> save(TravelComment comment){
        travelCommentService.save(comment);
        return R.ok();

    }

}
