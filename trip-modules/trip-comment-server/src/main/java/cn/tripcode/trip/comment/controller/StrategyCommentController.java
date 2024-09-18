package cn.tripcode.trip.comment.controller;

import cn.tripcode.trip.auth.anno.RequireLogin;
import cn.tripcode.trip.comment.service.StrategyCommentService;
import cn.tripcode.trip.core.utils.R;
import comment.domain.StrategyComment;
import comment.qo.CommentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies/comments")
public class StrategyCommentController {
    private final StrategyCommentService strategyCommentService;

    public StrategyCommentController(StrategyCommentService strategyCommentService) {
        this.strategyCommentService = strategyCommentService;
    }
    @GetMapping("/query")
    public R<Page<StrategyComment>> query(CommentQuery qo) {
        return R.ok(strategyCommentService.page(qo));
    }
    @RequireLogin
    @PostMapping("/likes")
    public R<?> likes(String cid){
        strategyCommentService.dolike(cid);
        return R.ok();
    }
    @RequireLogin
    @PostMapping("/save")
    public R<?> save(StrategyComment comment) {
        strategyCommentService.save(comment);
//        // 评论数+1
        strategyCommentService.replyNumIncr(comment.getStrategyId());
        return R.ok();
    }


}
