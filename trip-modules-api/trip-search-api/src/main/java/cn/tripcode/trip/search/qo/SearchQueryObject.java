package cn.tripcode.trip.search.qo;

import cn.tripcode.trip.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchQueryObject extends QueryObject {

    private Integer type = -1;
}
