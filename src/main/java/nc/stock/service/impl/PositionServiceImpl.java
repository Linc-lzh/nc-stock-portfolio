package nc.stock.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import nc.stock.mapper.PositionMapper;
import nc.stock.model.Position;
import nc.stock.service.PositionService;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {
}
