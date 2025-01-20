package nc.stock;

import nc.stock.model.Position;
import nc.stock.service.PositionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class H2Test {

    @Autowired
    private PositionService positionService;


    @Test
    void testInsert() {
        Position position = new Position();
        position.setSymbol("test");
        position.setPositionSize(1000);
        positionService.save(position);
    }
}
