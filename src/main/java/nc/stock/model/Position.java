package nc.stock.model;

import lombok.Data;

@Data
public class Position {
    private String symbol;
    private int positionSize;
}
