package cn.granitech.util;

public class LoginInterceptorHelper {
    private static String SKIP_INTERCEPTOR_PATH = ".*/((index.html)|(magic/*)|(magic-api/*)|(.*.ico)|(css/)|(js/)|(fonts/)|(user/login)|(dingTalk/*)|(picture/get)|(anon)|(queryPublic)|(img/)).*";

    public static boolean check(String path) {
        return path.matches(SKIP_INTERCEPTOR_PATH);
    }

    public static void addSkipInterceptorPath(String path) {
        int lastIndexOf = SKIP_INTERCEPTOR_PATH.lastIndexOf(")");
        SKIP_INTERCEPTOR_PATH = String.format("%s|(%s)%s", SKIP_INTERCEPTOR_PATH.substring(0, lastIndexOf), path, SKIP_INTERCEPTOR_PATH.substring(lastIndexOf));
    }
}
