package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.QiNiuHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.UploadDir;
import cn.granitech.web.pojo.application.CloudStorageSetting;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping({"/file"})
public class FileUploadController extends BaseController {
    public static final String QINIU_STORAGE = "QiNiu";
    public static String CLOUD_STORAGE_QINIU_KEY = "QiNiu=";
    private final ResourceLoader resourceLoader;
    private final UploadDir uploadDir;
    @Resource
    SystemSetting systemSetting;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader, UploadDir uploadDir) {
        this.resourceLoader = resourceLoader;
        this.uploadDir = uploadDir;
    }

    @RequestMapping(
            method = {RequestMethod.GET},
            value = {"/get/{file:.+}"},
            produces = {"application/octet-stream"}
    )
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String file, @RequestParam(required = false) String fileName, HttpServletRequest request, HttpServletResponse response) {
        String ROOT = this.uploadDir.getFileDir();

        try {
            String qiNiuKey = CLOUD_STORAGE_QINIU_KEY;
            if (file.startsWith(qiNiuKey)) {
                String downloadUrl = QiNiuHelper.getDownloadUrl(file.substring(qiNiuKey.length()), HttpUtil.isHttps(request.getScheme()));
                response.sendRedirect(downloadUrl);
                return null;
            } else {
                fileName = StringUtils.isBlank(fileName) ? file : fileName;
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                org.springframework.core.io.Resource fileResource = this.resourceLoader.getResource("file:" + Paths.get(ROOT, file));
                return ResponseEntity.ok().headers(headers).body(fileResource);
            }
        } catch (Exception var9) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(
            method = {RequestMethod.GET},
            value = {"/get/backup/{file:.+}"},
            produces = {"application/octet-stream"}
    )
    @ResponseBody
    @SystemRight(SystemRightEnum.ADMIN_ROLE)
    public ResponseEntity<?> getBackupFile(@PathVariable String file, @RequestParam(required = false) String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (this.systemSetting.getTrialVersionFlag()) {
            throw new ServiceException("当前为演示版本，功能暂不开放！");
        } else {
            return this.getFile("backup/" + file, fileName, request, response);
        }
    }

    @RequestMapping(
            method = {RequestMethod.POST},
            value = {"/upload"}
    )
    public ResponseBean<Map<String, String>> upload(MultipartFile file) throws IOException {
        String ROOT = this.uploadDir.getFileDir();
        String originalName = file.getOriginalFilename();
        String fileName = file.getOriginalFilename();

        assert fileName != null;

        String fileExtName = fileName.substring(fileName.lastIndexOf("."));
        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + this.randomFileString() + fileExtName;
        File dir = new File(ROOT);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file.transferTo(Paths.get(ROOT, fileName));
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("name", originalName);
        uploadResult.put("url", "/file/get/" + fileName);
        return ResponseHelper.ok(uploadResult, "success");
    }

    private String randomFileString() {
        String msPart = (new SimpleDateFormat("mmss")).format(new Date());
        String randomPart = String.format("%02d", (new Random()).nextInt(99));
        return "_" + randomPart + msPart;
    }

    @RequestMapping(
            value = {"/getStorageInfo"},
            method = {RequestMethod.GET}
    )
    public ResponseBean<Map<String, Object>> getStorageInfo() {
        Map<String, Object> result = new HashMap<>();
        CloudStorageSetting cloudStorageSetting = this.systemSetting.getCloudStorageSetting();
        boolean cloudStorage = ObjectUtil.isNotNull(cloudStorageSetting) && cloudStorageSetting.isOpenStatus() && StringUtils.isNotBlank(cloudStorageSetting.getHost()) && StringUtils.isNotBlank(cloudStorageSetting.getBucket()) && StringUtils.isNotBlank(cloudStorageSetting.getSecretKey()) && StringUtils.isNotBlank(cloudStorageSetting.getAccessKey());
        result.put("cloudStorage", cloudStorage);
        if (cloudStorage) {
            result.put("cloudStorageType", "QiNiu");
            Map<String, Object> qiNiuMap = new HashMap<>();
            qiNiuMap.put("token", QiNiuHelper.getToken(null));

            try {
                qiNiuMap.put("bucketInfo", QiNiuHelper.getBucketInfo());
            } catch (QiniuException var6) {
                var6.printStackTrace();
                throw new ServiceException("七牛云信息获取异常");
            }

            result.put("cloudStorageData", qiNiuMap);
        }

        return ResponseHelper.ok(result, "success");
    }
}
