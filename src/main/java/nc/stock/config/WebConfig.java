package nc.stock.config;

import nc.stock.model.Position;
import nc.stock.model.dto.PositionDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

@Configuration
public class WebConfig {
    final BlockingQueue<List<PositionDTO>> synchronousQueue = new SynchronousQueue<>();

    @Bean
    public BlockingQueue<List<PositionDTO>> messageQueue(){
        return synchronousQueue;
    }
}
