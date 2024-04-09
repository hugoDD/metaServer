package cn.granitech.report.utils;

import cn.granitech.report.luckySheet.LuckySheetCellUtil;
import cn.granitech.report.luckySheet.LuckySheetPropsEnum;
import cn.granitech.report.luckySheet.ResLuckySheetDataDto;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportExcelUtil {
    private static final String IMAGE_KEY = "images";
    private static final String SHEET_NAME_KEY = "name";
    private static final String MAX_X = "maxX";
    private static final String MAX_Y = "maxY";

    public static InputStream exportExcel(JSONArray dataJsonArray) {
        SXSSFWorkbook wb = new SXSSFWorkbook();

        for (int i = 0; i < dataJsonArray.size(); i++) {
            JSONObject sheetJsonObject = dataJsonArray.getJSONObject(i);

            ResLuckySheetDataDto resLuckySheetDataDto = JSON.parseObject(sheetJsonObject.toJSONString(), ResLuckySheetDataDto.class);
            Object borderInfos = null;
            Map<String, Object> rowlen = null;
            Map<String, Object> columnlen = null;

            if (resLuckySheetDataDto.getConfig() != null) {

                borderInfos = resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());

                rowlen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());

                columnlen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());
            }
            List<Object> borderInfoList = null;
            if (borderInfos != null) {
                if (borderInfos instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) borderInfos;
                    if (CollUtil.isNotEmpty(jsonObject)) {
                        borderInfoList = (List<Object>) borderInfos;
                    }
                }
            }

            Map<String, Integer> maxXAndY = new HashMap<>();
            maxXAndY.put(MAX_X, 50);
            maxXAndY.put(MAX_Y, 150);
            resLuckySheetDataDto.setMaxXAndY(maxXAndY);

            SXSSFSheet sheet = wb.createSheet(sheetJsonObject.getString(SHEET_NAME_KEY));

            sheet.setRandomAccessWindowSize(-1);
            LuckySheetCellUtil cellUtil = new LuckySheetCellUtil(wb, sheet);

            cellUtil.createCells(maxXAndY.get(MAX_X), maxXAndY.get(MAX_Y), rowlen);
            if (columnlen != null) {
                for (Map.Entry<String, Object> entry : columnlen.entrySet()) {
                    BigDecimal wid = new BigDecimal(String.valueOf(entry.getValue()));
                    BigDecimal excleWid = new BigDecimal(35);
                    sheet.setColumnWidth(Integer.parseInt(String.valueOf(entry.getKey())), wid.multiply(excleWid).setScale(0, RoundingMode.HALF_UP).intValue());
                }
            }

            cellUtil.setCellValues(resLuckySheetDataDto.getCelldata(), resLuckySheetDataDto.getHyperlinks(), borderInfoList);
            SXSSFDrawing sXSSFDrawing = sheet.createDrawingPatriarch();
            JSONObject images = sheetJsonObject.getJSONObject(IMAGE_KEY);
            if (images != null) {

                for (Map.Entry<String, Object> entry : images.entrySet()) {
                    JSONObject image = images.getJSONObject(entry.getKey());

                    String base64 = image.getString("src");

                    JSONObject imageDefault = image.getJSONObject("default");
                    if (imageDefault == null) {
                        continue;
                    }

                    int col_st = getImagePosition(imageDefault.getIntValue("left"), sheetJsonObject.getJSONArray("visibledatacolumn"));

                    int row_st = getImagePosition(imageDefault.getIntValue("top"), sheetJsonObject.getJSONArray("visibledatarow"));

                    int col_ed = getImagePosition(imageDefault.getIntValue("left") + imageDefault.getIntValue("width"), sheetJsonObject.getJSONArray("visibledatacolumn"));

                    int row_ed = getImagePosition(imageDefault.getIntValue("top") + imageDefault.getIntValue("height"), sheetJsonObject.getJSONArray("visibledatarow"));
                    int addPicture = wb.addPicture(getImageByte(base64), 6);

                    XSSFClientAnchor xSSFClientAnchor = new XSSFClientAnchor(0, 0, 0, 0, col_st, row_st, col_ed + 1, row_ed + 1);
                    xSSFClientAnchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    Picture picture = sXSSFDrawing.createPicture(xSSFClientAnchor, addPicture);

                    picture.resize(1.0D);
                }
            }
        }
        return getSheetInputStream(wb);
    }


    public static int getImagePosition(int num, JSONArray arr) {
        int minIndex, maxIndex, index = 0;


        for (int i = 0; i < arr.size(); i++) {
            if (num < arr.getIntValue(i)) {
                index = i;
                break;
            }
        }
        if (index == 0) {
            minIndex = 0;
            maxIndex = 1;
            return Math.abs((num) / (arr.getIntValue(maxIndex) - arr.getIntValue(minIndex))) + index;
        }
        if (index == arr.size() - 1) {
            minIndex = arr.size() - 2;
            maxIndex = arr.size() - 1;
        } else {
            minIndex = index - 1;
            maxIndex = index;
        }
        int min = arr.getIntValue(minIndex);
        int max = arr.getIntValue(maxIndex);
        return Math.abs((num - min) / (max - min)) + index;
    }


    public static byte[] getImageByte(String base64) {
        if (base64 != null) {
            return Base64.decode(base64.substring(base64.indexOf(",") + 1));
        }
        return null;
    }


    public static ByteArrayInputStream getSheetInputStream(SXSSFWorkbook wb) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }


    public static WriteCellData<Void> writeByUrl(List<String> urls) {
        if (urls == null || urls.size() == 0) {
            return null;
        }
        WriteCellData<Void> writeCellData = new WriteCellData<>();
        writeCellData.setType(CellDataTypeEnum.EMPTY);
        List<ImageData> imageDataList = new ArrayList<>();
        writeCellData.setImageDataList(imageDataList);

        int witch = Math.max(1, 100 / urls.size());
        for (int i = 0; i < urls.size(); i++) {
            ImageData imageData = new ImageData();
            imageDataList.add(imageData);

            byte[] bytes = HttpRequest.get(URLUtils.urlEncode(urls.get(i))).execute().bodyBytes();

            imageData.setImage(bytes);

            imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
            imageData.setTop(5);
            imageData.setBottom(5);
            imageData.setLeft(i * witch + 2);
            imageData.setRight((urls.size() - i - 1) * witch + 2);
        }

        return writeCellData;
    }
}



