package cn.tripcode.trip.comment.service;

import comment.domain.StrategyComment;
import comment.domain.TravelComment;
import comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelCommentService {
    Page<TravelComment> page(CommentQuery qo);

    void save(TravelComment comment);

    List<TravelComment> findList(Long travelId);
}
