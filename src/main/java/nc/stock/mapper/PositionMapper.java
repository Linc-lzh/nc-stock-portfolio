package nc.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import nc.stock.model.Position;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PositionMapper extends BaseMapper<Position> {
}
