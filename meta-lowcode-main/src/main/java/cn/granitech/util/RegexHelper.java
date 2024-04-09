package cn.granitech.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    public static final String BRACE = "\\{([a-zA-Z0-9$\\.]+)}";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    public static final String MOBILE_PHONE = "^1\\d{10}$";
    public static final String NUMBER = "[0-9]*";
    public static final Pattern TEL_PATTERN = Pattern.compile("[0-9\\-]{7,18}");
    public static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public static Set<String> getVariableSet(String text, String p) {
        Matcher m = Pattern.compile(p).matcher(text);
        Set<String> set = new HashSet<>();
        while (m.find()) {
            set.add(m.group(1));
        }
        return set;
    }

    public static boolean isEmail(String email) {
        return StringUtils.isNotBlank(email) && EMAIL_PATTERN.matcher(email).find() && !email.endsWith("yahoo.cn") && !email.endsWith("yahoo.com.cn") && !email.endsWith(".") && !email.startsWith(".") && StringUtils.countMatches(email, "@") <= 1 && !email.contains("..");
    }

    public static boolean isUrl(String url) {
        return StringUtils.isNotBlank(url) && URL_PATTERN.matcher(url).find();
    }

    public static boolean isTel(String tel) {
        if (StringUtils.isBlank(tel) || tel.length() < 7 || tel.startsWith("-") || tel.endsWith("-") || tel.contains("--")) {
            return false;
        }
        return TEL_PATTERN.matcher(tel).find();
    }
}
