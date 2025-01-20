package nc.stock.job;

import lombok.extern.slf4j.Slf4j;
import nc.stock.model.dto.PositionDTO;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StockConsumer implements Runnable{
    protected BlockingQueue<List<PositionDTO>> blockingQueue;
    private AtomicInteger indexer = new AtomicInteger(0);
    private String FORMAT = "%s                    %.2f          %d         %.2f";
    public StockConsumer(BlockingQueue<List<PositionDTO>> queue) {
        this.blockingQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BigDecimal total = BigDecimal.ZERO;
                List<PositionDTO> data = blockingQueue.take();
                data.sort(Comparator.comparing(PositionDTO::getSymbol));
                log.info("## {} Macket Data Update", indexer.addAndGet(1));
                log.info("Symbol                    price          qty          value");
                for (PositionDTO dto : data) {
                    log.info(String.format(FORMAT, dto.getSymbol(), dto.getPrice(), dto.getPositionSize(), dto.getValue()));
                    total = total.add(dto.getValue());
                }

                log.info(String.format("#Total portfolio                    %.2f", total));
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
