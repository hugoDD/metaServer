package cn.granitech.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Helper {
    public static String md5HexForSalt(String loginPwd, String salt) {
        return DigestUtils.md5Hex(String.format("Meta%sCode%s", DigestUtils.md5Hex(loginPwd), salt));
    }
}
