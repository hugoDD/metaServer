package cn.granitech.web.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadDir {
    @Value("${upload.file-dir}")
    private String fileDir;
    @Value("${upload.img-dir}")
    private String imgDir;

    public String getImgDir() {
        return this.imgDir;
    }

    public String getFileDir() {
        return this.fileDir;
    }
}
