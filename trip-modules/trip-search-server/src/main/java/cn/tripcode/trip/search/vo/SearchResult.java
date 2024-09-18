package cn.tripcode.trip.search.vo;

import cn.tripcode.trip.article.dto.DestinationDto;
import cn.tripcode.trip.article.dto.StrategyDto;
import cn.tripcode.trip.article.dto.TravelDto;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchResult {

    private List<DestinationDto> dests = new ArrayList<>();
    private List<StrategyDto> strategies = new ArrayList<>();
    private List<TravelDto> travels = new ArrayList<>();
    private List<UserInfoDTO> users = new ArrayList<>();
    private Long total = 0L;
}
