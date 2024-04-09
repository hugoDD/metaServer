package cn.granitech.variantorm.persistence.license;


import cn.granitech.variantorm.exception.LicenseException;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseHelper {
    private static final String LICENSE_CHECK_ACCOUNT_INFO = "http://license.melecode.com:8080/license/checkAccountInfo";
    private static final String USER_HOME = System.getProperty("user.home");

    private static  String getDeviceNo(int port) {
        File licenseFileDir = new File(USER_HOME, ".license");
        File licenseFile = new File(licenseFileDir, LicenseHelper.licenseFilePath(port));
        try {
            if (licenseFile.exists()) {

                List<String> lines = Files.readAllLines(licenseFile.toPath());
                return String.join("", lines);
            }
            return LicenseHelper.getDeviceNoContext(port);
        } catch (IOException e) {
            throw new LicenseException("读取许可文件失败", e);
        }

    }

    private static  String licenseFilePath(int port) {
        return DigestUtils.md5Hex("Meta!_License&*" + port);
    }

    private static  void ALLATORIxDEMO(String companyName, String version, String account, Integer entityLimit) {
        System.out.println("*****************************************************");
        System.out.println(companyName);
        System.out.println("版本："+version);
        System.out.println("账号："+account);
        System.out.println("实体数上限："+(entityLimit == null ? "无限制" : entityLimit));
        System.out.println("*****************************************************");
    }

    private static  String getDeviceNoContext(int port) {
        File dir = new File(USER_HOME, ".license");
        File file = new File(dir, LicenseHelper.licenseFilePath(port));
        try {
            Files.createDirectories(dir.toPath());
            String context = "DN_" + RandomUtil.randomString(35);

            Files.write(file.toPath(), context.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            return context;
        } catch (IOException a4) {
            throw new LicenseException("写入许可文件失败", a4);
        }
    }

    public static void main(String[] stringArray) {
        System.out.println("################################################\n" +
                "#                                              #\n" +
                "#        ## #   #    ## ### ### ##  ###        #\n" +
                "#       # # #   #   # #  #  # # # #  #         #\n" +
                "#       ### #   #   ###  #  # # ##   #         #\n" +
                "#       # # ### ### # #  #  ### # # ###        #\n" +
                "#                                              #\n" +
                "# Obfuscation by Allatori Obfuscator v7.6 DEMO #\n" +
                "#                                              #\n" +
                "#           http://www.allatori.com            #\n" +
                "#                                              #\n" +
                "################################################");
        LicenseHelper.getLicense("Test项目名", "10002", "9eiDVKPuzOpaCpteJSYiZz01D3E59Qw2OCXxc54v", "080408b8cf7e4e5db5896ad2cf021559", 8302, null);
    }


    public static LicenseInfo getLicense(String projectName, String account, String secretKey, String licenseCode, Integer port, String offlineLicense) {
        String deviceNo;
        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> liceseInfoMap = new HashMap<>();
        liceseInfoMap.put("account", account);
        liceseInfoMap.put("licenseCode", licenseCode);
        liceseInfoMap.put("port", port);
        liceseInfoMap.put("timestamp", timestamp);
        try {
            deviceNo = LicenseHelper.getDeviceNo(port);
        } catch (Exception exception) {
            exception.printStackTrace();
            return LicenseInfo.buildFreeLicense("设备码获取失败");
        }
        try {

            String deviceInfo = String.format("项目名称: %s,端口: %d", projectName,port);
            if ((StringUtils.isBlank(account) || StringUtils.isBlank(secretKey) || StringUtils.isBlank(licenseCode)) && StringUtils.isBlank(offlineLicense)) {
                return LicenseInfo.buildFreeLicense(deviceNo);
            }
            if (StringUtils.isNotBlank(offlineLicense)) {
                Map<String, Object> decryptData = OfflineLicenseHelper.decryptData(offlineLicense);
                LicenseAccount licenseAccount = new LicenseAccount(account, port, licenseCode, deviceNo, timestamp, deviceInfo);
                if (!licenseAccount.checkOfflineSign((String) decryptData.get("secretKey"), (String) decryptData.get("offlineSign"))) {
                    LicenseException licenseException = new LicenseException("离线授权设备不匹配！");
                    licenseException.setDeviceNo(deviceNo);
                    throw licenseException;
                }
                return LicenseInfo.buildCommercialLicense((String) decryptData.get("licenseCode"), (String) decryptData.get("companyName"), (String) decryptData.get("account"), (String) decryptData.get("version"), (Integer) decryptData.get("entityLimit"), deviceNo);
            }
            liceseInfoMap.put("deviceNo", deviceNo);
            liceseInfoMap.put("deviceInfo", deviceInfo);
            LicenseAccount licenseAccount = new LicenseAccount(account, port, licenseCode, deviceNo, timestamp, deviceInfo);
            liceseInfoMap.put("sign", licenseAccount.getClientSign(secretKey));
            HttpRequest a11 = HttpUtil.createPost(LICENSE_CHECK_ACCOUNT_INFO);
            a11.body(JSON.toJSONString(liceseInfoMap));
            String a12 = a11.execute().body();
            JSONObject jSONObject = JSON.parseObject(a12);
            if (jSONObject.getIntValue("code") != 200) {
                System.out.println(jSONObject.getString("error"));
                throw new LicenseException(jSONObject.getString("error"));
            }
            JSONObject licenseData = jSONObject.getJSONObject("data");
            if (StringUtils.isBlank(licenseData.getString("serverSign")) || !licenseData.getString("serverSign").equals(licenseAccount.getSeverSign(secretKey))) {
                System.out.println("安全校验异常！！！");
                throw new LicenseException("安全校验异常！");
            }
            return LicenseInfo.buildCommercialLicense(licenseCode, licenseData.getString("companyName"), licenseData.getString("account"), licenseData.getString("version"), licenseData.getInteger("entityLimit"), deviceNo);
        } catch (Exception e) {
            e.printStackTrace();
            return LicenseInfo.buildFreeLicense(deviceNo);
        }
    }
}


