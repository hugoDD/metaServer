package cn.granitech.report.luckySheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;


public class StringUtil {
    protected static final String COMMA = ",";
    protected static final String orderTemplete = " `%s` %s ";

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() != 0;
    }


    public static String trim(String str) {
        if (str == null) {
            return str;
        }
        if (str.trim().length() == 0) {
            return "";
        }
        return str.trim();
    }


    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    public static int[] hexToRgb(String hex) {
        int[] color = new int[3];
        color[0] = Integer.parseInt(hex.substring(1, 3), 16);
        color[1] = Integer.parseInt(hex.substring(3, 5), 16);
        color[2] = Integer.parseInt(hex.substring(5, 7), 16);
        return color;
    }

    public static int[] rgbStringToRgb(String rgb) {
        int[] color = new int[3];
        rgb = rgb.replace("rgb", "");
        rgb = rgb.replace("(", "").replace(")", "").replaceAll(" ", "");
        String[] colors = rgb.split(",");
        color[0] = Integer.parseInt(colors[0]);
        color[1] = Integer.parseInt(colors[1]);
        color[2] = Integer.parseInt(colors[2]);
        return color;
    }

    public static String rgb2Hex(int r, int g, int b) {
        return String.format("%02x%02x%02x", Integer.valueOf(r), Integer.valueOf(g), Integer.valueOf(b));
    }

    public static boolean isEmptyMap(Map map) {
        if (map == null) {
            return true;
        }
        return map.isEmpty();
    }


    public static File getFile(String url) throws Exception {
        String fileName = url.substring(url.lastIndexOf("."));
        File file = null;


        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("net_url", fileName);

            URL urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}



