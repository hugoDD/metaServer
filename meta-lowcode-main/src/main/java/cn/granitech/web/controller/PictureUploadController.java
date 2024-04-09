package cn.granitech.web.controller;

import cn.granitech.util.QiNiuHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.UploadDir;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping({"/picture"})
@RestController
public class PictureUploadController extends BaseController {
    static final /* synthetic */ boolean $assertionsDisabled = (!PictureUploadController.class.desiredAssertionStatus());
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ResourceLoader resourceLoader;
    private final UploadDir uploadDir;
    @Resource
    SystemSetting systemSetting;

    @Autowired
    public PictureUploadController(ResourceLoader resourceLoader2, UploadDir uploadDir2) {
        this.resourceLoader = resourceLoader2;
        this.uploadDir = uploadDir2;
    }

    @ResponseBody
    @RequestMapping(method = {RequestMethod.GET}, produces = {"image/jpeg;image/png;image/gif;"}, value = {"/get/{filename:.+}"})
    public ResponseEntity<?> getFile(@PathVariable String filename, HttpServletRequest request, HttpServletResponse response) {
        String ROOT = this.uploadDir.getImgDir();
        try {
            String qiNiuKey = FileUploadController.CLOUD_STORAGE_QINIU_KEY;
            if (filename.startsWith(qiNiuKey)) {
                response.sendRedirect(QiNiuHelper.getDownloadUrl(filename.substring(qiNiuKey.length()), HttpUtil.isHttps(request.getScheme())));
                return null;
            }
            return ResponseEntity.ok(this.resourceLoader.getResource("file:" + Paths.get(ROOT, filename)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping({"/get/logo"})
    public ResponseEntity logo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String logo = this.systemSetting.getLogo();
        if (StrUtil.isBlank(logo) || !logo.startsWith("/picture")) {
            logo = "/img/logo.png";
        }
        request.getRequestDispatcher(logo).forward(request, response);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/upload"})
    public ResponseBean<Map<String, String>> upload(MultipartFile file) throws IOException {
        String ROOT = this.uploadDir.getImgDir();
        String originalName = file.getOriginalFilename();
        String fileName = file.getOriginalFilename();
        if ($assertionsDisabled || fileName != null) {
            String fileName2 = UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
            File dir = new File(ROOT);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(Paths.get(ROOT, fileName2));
            Map<String, String> uploadResult = new HashMap<>();
            uploadResult.put("name", originalName);
            uploadResult.put("url", "/picture/get/" + fileName2);
            return ResponseHelper.ok(uploadResult, "success");
        }
        throw new AssertionError();
    }
}
