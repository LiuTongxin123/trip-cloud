package cn.tripcode.trip.comment.repository;

import comment.domain.StrategyComment;
import comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyCommentRepository extends MongoRepository<StrategyComment,String> {
}
