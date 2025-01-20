package nc.stock.controllers;

import nc.stock.common.api.CommonResult;
import nc.stock.service.ImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class UploadController {
    @Resource
    private ImportService importService;

    @PostMapping("import")
    public CommonResult importPosition(MultipartFile file) throws IOException {
        importService.importExcel(file);
        return CommonResult.success("import success");
    }
}
