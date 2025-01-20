package nc.stock.model.dto;

import lombok.Data;
import nc.stock.model.Position;

import java.math.BigDecimal;

@Data
public class PositionDTO extends Position {
    private BigDecimal price;
    private BigDecimal value;
}
