package nc.stock.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import nc.stock.model.Position;
import nc.stock.service.PositionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class PositionImportListener implements ReadListener<Position>{
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    private ThreadLocal<ArrayList<Position>> positionList = ThreadLocal.withInitial(ArrayList::new);
    private static AtomicInteger count = new AtomicInteger(1);
    private static final int batchSize = 2;

    @Resource
    private PositionService positionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invoke(Position data, AnalysisContext context) {
        positionList.get().add(data);
        if (positionList.get().size() >= batchSize) {
            asyncSaveData();
        }
    }

    public void saveOne(Position data){
        positionService.save(data);
        log.info("the number " + count.getAndAdd(1) + "of item insert one row of data");
    }

    public void saveData() {
        if (!positionList.get().isEmpty()) {
            positionService.saveBatch(positionList.get(), positionList.get().size());
            log.info("The number " + count.getAndAdd(1) + " of item inserted " + positionList.get().size() + "rows of data");
            positionList.get().clear();
        }
    }

    public void asyncSaveData() {
        if (!positionList.get().isEmpty()) {
            ArrayList<Position> positions = (ArrayList<Position>) positionList.get().clone();
            executorService.execute(new SaveTask(positions, positionService));
            positionList.get().clear();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("All Sheets are completed");
        if (!positionList.get().isEmpty()) {
            saveData();
        }
    }

    static class SaveTask implements Runnable {

        private List<Position> positionList;
        private PositionService positionService;

        public SaveTask(List<Position> positionList, PositionService positionService) {
            this.positionList = positionList;
            this.positionService = positionService;
        }

        @Override
        public void run() {
            positionService.saveBatch(positionList);
            log.info("The number " + count.getAndAdd(1) + " of item inserted " + positionList.size() + " rows of data");
        }
    }

}
