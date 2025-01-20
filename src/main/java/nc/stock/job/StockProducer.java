package nc.stock.job;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import nc.stock.model.Position;
import nc.stock.model.dto.PositionDTO;
import nc.stock.service.PositionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Slf4j
public class StockProducer implements Runnable {

    protected BlockingQueue<List<PositionDTO>> blockingQueue;
    private PositionService positionService;

    public StockProducer(BlockingQueue<List<PositionDTO>> queue, PositionService positionService) {
        this.blockingQueue = queue;
        this.positionService = positionService;
    }

    @Override
    public void run() {
        int i = 0;
        Random rand = new Random();

        while (true) {
            try {
                List<PositionDTO> dtoList = positionService.list().stream().map(p -> {
                    PositionDTO dto = new PositionDTO();
                    BeanUtil.copyProperties(p, dto);
                    double randomDouble = rand.nextDouble() * 100;
                    dto.setPrice(new BigDecimal(randomDouble).setScale(2, RoundingMode.HALF_UP));
                    dto.setValue(dto.getPrice().multiply(new BigDecimal(dto.getPositionSize())));
                    return dto;
                }).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(dtoList)){
                    blockingQueue.put(dtoList);
                }
                else {
                    log.info("Please import position csv");
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
