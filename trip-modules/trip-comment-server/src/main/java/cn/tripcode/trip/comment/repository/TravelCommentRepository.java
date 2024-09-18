package cn.tripcode.trip.comment.repository;

import comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TravelCommentRepository extends MongoRepository<TravelComment,String> {
}
