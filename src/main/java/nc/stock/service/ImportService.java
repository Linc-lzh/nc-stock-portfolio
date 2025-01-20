package nc.stock.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import nc.stock.listener.PositionImportListener;
import nc.stock.model.Position;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ImportService {

    @Resource
    private PositionImportListener importListener;


    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    public void importExcel(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), Position.class, importListener)
                .excelType(ExcelTypeEnum.CSV)
                .doReadAll();
    }


    public void importExcelAsync(MultipartFile file) {
        List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int num = i;
            tasks.add(() -> {
                EasyExcel.read(file.getInputStream(), Position.class, importListener)
                        .sheet(num).doRead();
                return null;
            });
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
