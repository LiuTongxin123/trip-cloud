package cn.tripcode.trip.comment.service;

import cn.tripcode.trip.core.qo.QueryObject;
import comment.domain.StrategyComment;
import comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

public interface StrategyCommentService {
    Page<StrategyComment> page(CommentQuery qo);

    void save(StrategyComment comment);

    void dolike(String cid);

    void replyNumIncr(Long strategyId);
}
