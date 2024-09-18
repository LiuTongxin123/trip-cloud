package cn.tripcode.trip.article.mapper;

import cn.tripcode.trip.article.domain.Destination;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DestinationMapper extends BaseMapper <Destination>{
    List<Destination> selectHotListByRid(@Param("rid") Long rid,@Param("ids") List<Long> ids);
}
