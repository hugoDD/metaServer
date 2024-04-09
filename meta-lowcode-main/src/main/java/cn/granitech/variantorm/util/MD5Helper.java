package cn.granitech.variantorm.util;

import cn.granitech.variantorm.persistence.cache.OptionCacheManager;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Helper {


    public static String md5HexForSalt(String loginPwd, String salt) {
        loginPwd = DigestUtils.md5Hex(loginPwd);
        return DigestUtils.md5Hex(String.format("Meta%sCode%s", loginPwd,salt));
    }
}
