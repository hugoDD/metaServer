package cn.granitech.util;

import cn.granitech.exception.ServiceException;
import cn.granitech.web.pojo.application.CloudStorageSetting;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.hutool.core.util.ObjectUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.BucketInfo;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiNiuHelper {
    private static final Logger log = LoggerFactory.getLogger(QiNiuHelper.class);
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final Configuration cfg = new Configuration(Region.autoRegion());
    private static SystemSetting systemSetting = null;

    public static String getToken(String fileKey) {
        Auth auth = getAuth();
        String bucket = getBucket();
        return StringUtils.isBlank(fileKey) ? auth.uploadToken(bucket) : auth.uploadToken(bucket, fileKey);
    }

    public static BucketInfo getBucketInfo() throws QiniuException {
        Auth auth = getAuth();
        return new BucketManager(auth, cfg).getBucketInfo(getBucket());
    }

    public static void deleteFiles(String... keyList) throws QiniuException {
        Auth auth = getAuth();
        String bucket = getBucket();
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            BatchStatus[] batchStatusList = bucketManager.batch(batchOperations).jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                if (status.code == 200) {
                    log.info("七牛云文件删除：{}", key);
                } else {
                    log.info("七牛云文件删除失败：{}，error:", key, status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error("七牛云文件删除失败", ex);
            throw ex;
        }
    }

    public static String getDownloadUrl(String key, boolean useHttps) throws QiniuException {
        Auth auth = getAuth();
        String host = getCloudStorageSetting().getHost();
        if (host.startsWith(HTTP)) {
            host = host.substring(HTTP.length());
            useHttps = false;
        } else if (host.startsWith(HTTPS)) {
            host = host.substring(HTTPS.length());
            useHttps = true;
        }
        return new DownloadUrl(host, useHttps, key).buildURL(auth, (System.currentTimeMillis() / 1000) + 3600);
    }

    private static CloudStorageSetting getCloudStorageSetting() {
        if (ObjectUtil.isNull(systemSetting)) {
            systemSetting = SpringHelper.getBean(SystemSetting.class);
        }
        if (ObjectUtil.isNull(systemSetting)) {
            throw new ServiceException("云存储配置信息获取失败！");
        }
        CloudStorageSetting cloudStorageSetting = systemSetting.getCloudStorageSetting();
        if (ObjectUtil.isNull(cloudStorageSetting)) {
            throw new ServiceException("云存储配置信息获取失败！");
        } else if (!StringUtils.isBlank(cloudStorageSetting.getAccessKey()) && !StringUtils.isBlank(cloudStorageSetting.getSecretKey()) && !StringUtils.isBlank(cloudStorageSetting.getBucket()) && !StringUtils.isBlank(cloudStorageSetting.getHost())) {
            return cloudStorageSetting;
        } else {
            throw new ServiceException("云存储配置信息异常！");
        }
    }

    private static Auth getAuth() {
        CloudStorageSetting cloudStorageSetting = getCloudStorageSetting();
        return Auth.create(cloudStorageSetting.getAccessKey(), cloudStorageSetting.getSecretKey());
    }

    private static String getBucket() {
        return getCloudStorageSetting().getBucket();
    }
}
