package cn.granitech.variantorm.persistence.license;


import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson2.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

public class OfflineLicenseHelper {
    private static final String passphrase = "Meta_Code_hhr@~!";

    public static Map<String, Object> decryptData(String encryptedData) {
        try {
            byte[] a = OfflineLicenseHelper.generateSecret();
            SecretKeySpec secretKeySpec = new SecretKeySpec(a, "AES");
            return JSONObject.parseObject(new SymmetricCrypto("AES/ECB/PKCS5Padding", secretKeySpec).decryptStr(encryptedData), Map.class);
        }
        catch (Exception a) {
            throw new RuntimeException("解密字符串失败", a);
        }
    }

    private static  byte[] generateSecret() {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec pbeKeySpec = new PBEKeySpec(OfflineLicenseHelper.passphrase.toCharArray(), new byte[16], 10000, 128);
            return secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        }
        catch (Exception a) {
            throw new RuntimeException("秘钥派生失败", a);
        }
    }

    public static void main(String[] stringArray) throws Exception {
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
        Map<String, Object> a = OfflineLicenseHelper.decryptData("FTYtAMha8kho/HMsyJHRROLwFzPXL+oPimykbrin7tc3HMHRkfi2PobAQHdC3s6Bnt+fYtHwubWrWgPzF2a3CM4Vf0UuoWVOCb7kc2h+oBGC5EVxX+iXYBtSrwFY0ms+WKryXx5Tg4p1NcErlSIGWQ0ZK06vOZ1pds/NtLvp6fkdc2kzXsIi6uDTJPHhaHB50Ne+Vf/spn3GaJvPogQeJLdOGqR7N7zMU8LKxPLpNCcJNOdkYaqGKmO2ZVH44PgYZJSof7Yvl7AS7rX1qGGDJA==");
        System.out.println(a);
    }

    public static String encryptData(Map<String, Object> data) {
        try {
            byte[] bytes = OfflineLicenseHelper.generateSecret();
            SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, "AES");
            String a3 = JSONObject.toJSONString(data);
            return new SymmetricCrypto("AES/ECB/PKCS5Padding", secretKeySpec).encryptBase64(a3);
        }
        catch (Exception a) {
            throw new RuntimeException("加密对象失败", a);
        }
    }
}
