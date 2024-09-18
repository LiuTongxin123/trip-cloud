package comment.qo;


import cn.tripcode.trip.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentQuery extends QueryObject {

    private Long articleId;
}
