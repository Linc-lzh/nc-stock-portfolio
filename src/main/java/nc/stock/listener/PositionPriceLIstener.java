package nc.stock.listener;

import nc.stock.job.StockConsumer;
import nc.stock.job.StockProducer;
import nc.stock.model.Position;
import nc.stock.model.dto.PositionDTO;
import nc.stock.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class PositionPriceLIstener implements SmartLifecycle {

    @Autowired
    BlockingQueue<List<PositionDTO>> messageQueue;

    @Autowired
    PositionService positionService;
    private ExecutorService stockProducerExecutorService;
    private ExecutorService stockConsumerExecutorService;

    private boolean isRunning = false;

    @Override
    public void start() {
        stockProducerExecutorService = Executors.newFixedThreadPool(1);
        stockConsumerExecutorService = Executors.newFixedThreadPool(1);

        stockProducerExecutorService.execute(new StockProducer(messageQueue, positionService));
        stockConsumerExecutorService.execute(new StockConsumer(messageQueue));
        isRunning = true;
    }

    @Override
    public void stop() {
        stockProducerExecutorService.shutdown();
        stockConsumerExecutorService.shutdown();
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
