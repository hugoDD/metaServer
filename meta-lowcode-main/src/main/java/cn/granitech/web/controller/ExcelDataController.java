package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.dataimport.DataExporter;
import cn.granitech.business.service.dataimport.DataFileParser;
import cn.granitech.business.service.dataimport.DataImporter;
import cn.granitech.business.service.dataimport.ImportRule;
import cn.granitech.business.task.HeavyTask;
import cn.granitech.business.task.TaskExecutors;
import cn.granitech.util.RedisUtil;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.enumration.RedisKeyEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.ImportRequestBody;
import cn.granitech.web.pojo.ListQueryRequestBody;
import cn.granitech.web.pojo.ResponseBean;
import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping({"/excelData"})
@RestController
public class ExcelDataController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    CrudController crudController;
    @Resource
    PersistenceManager pm;
    @Resource
    RedisUtil redisUtil;

    @PostMapping({"exportExcel"})
    public ResponseBean exportExcel(@RequestBody ListQueryRequestBody requestBody) {
        String redisKey = RandomUtil.randomString(16);
        this.redisUtil.set(RedisKeyEnum.TMP_CACHE.getKey(redisKey), requestBody, 30L);
        return ResponseHelper.ok(redisKey);
    }

    @GetMapping({"downloadExcelFile"})
    public ResponseBean downloadExcelFile(HttpServletResponse response, String key) throws Exception {
        ListQueryRequestBody requestBody = this.redisUtil.get(RedisKeyEnum.TMP_CACHE.getKey(key));
        if (requestBody == null) {
            return ResponseHelper.fail("有效期已过!");
        }
        Entity entity = this.pm.getMetadataManager().getEntity(requestBody.getMainEntity());
        DataExporter exporter = new DataExporter(this.crudController.queryListMap(requestBody).getData().getDataList(), entity, requestBody.getFieldsList());
        String encodeName = URLEncoder.encode(entity.getLabel(), DataFileParser.UTF8) + "-" + System.currentTimeMillis() + ".xlsx";
        response.addHeader("Content-Disposition", "attachment;filename=" + encodeName);
        response.addHeader("filename", encodeName);
        exporter.export(response.getOutputStream());
        this.redisUtil.remove(RedisKeyEnum.TMP_CACHE.getKey(key));
        return ResponseHelper.ok();
    }

    @GetMapping({"/check-file"})
    @SystemRight(SystemRightEnum.RECYCLE_IMPORT_MANAGE)
    public ResponseBean checkFile(@RequestParam("uploadFile") String uploadFile) {
        File tmp = getFileOfImport(uploadFile);
        if (tmp == null) {
            return ResponseHelper.fail("数据文件无效");
        }
        try {
            return ResponseHelper.ok(new DataFileParser(tmp).parse(0).get(0));
        } catch (Exception ex) {
            this.log.info("文件解析失败！异常信息={}", ex);
            return ResponseHelper.fail("无法解析数据，请检查数据文件格式");
        }
    }

    @PostMapping({"/import-submit"})
    @SystemRight(SystemRightEnum.RECYCLE_IMPORT_MANAGE)
    public ResponseBean importSubmit(@RequestBody ImportRequestBody importRequestBody) {
        return ResponseHelper.ok(TaskExecutors.submit(new DataImporter(ImportRule.parse(importRequestBody, this.pm.getMetadataManager().getEntity(importRequestBody.getMainEntity()))), importRequestBody.getOwningUser()));
    }

    @GetMapping({"/import-trace"})
    @SystemRight(SystemRightEnum.RECYCLE_IMPORT_MANAGE)
    public ResponseBean importTrace(@RequestParam String taskId) {
        HeavyTask<?> task = TaskExecutors.get(taskId);
        if (task == null) {
            return ResponseHelper.fail("taskId is not found！");
        }
        return ResponseHelper.ok(((DataImporter) task).getTraceLogs());
    }

    @PostMapping({"upload"})
    @SystemRight(SystemRightEnum.RECYCLE_IMPORT_MANAGE)
    public ResponseBean upload(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {
        File folder = new File(ImportRule.tempPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = uploadFile.getOriginalFilename();
        String newName = UUID.randomUUID() + (!oldName.contains(".") ? oldName : oldName.substring(oldName.lastIndexOf(".")));
        try {
            uploadFile.transferTo(Paths.get(ImportRule.tempPath, newName));
            Map<String, String> result = new HashMap<>();
            result.put("originalFilename", oldName);
            result.put("fileName", newName);
            return ResponseHelper.ok(result);
        } catch (IOException e) {
            this.log.info("文件上传失败！异常信息={}", e);
            return ResponseHelper.fail(500, "上传失败");
        }
    }

    private File getFileOfImport(String filePath) {
        if (filePath.contains("%")) {
            try {
                filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        File tmp = new File(ImportRule.tempPath + filePath);
        if (!tmp.exists() || tmp.isDirectory()) {
            return null;
        }
        return tmp;
    }
}
