package cn.granitech.report.luckySheet;

import cn.granitech.util.JsonHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LuckySheetCellUtil {
    private final SXSSFWorkbook wb;

    private final SXSSFSheet sheet;


    public LuckySheetCellUtil(SXSSFWorkbook wb, SXSSFSheet sheet) {
        this.wb = wb;
        this.sheet = sheet;
    }

    public static Map<String, Object> getCellStyleMap(Map<String, Object> cellConfig) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("fontName", cellConfig.getOrDefault(LuckySheetPropsEnum.FONTFAMILY.getCode(), "Times New Roman"));

        if (cellConfig.containsKey(LuckySheetPropsEnum.BOLD.getCode())) {
            String bold = String.valueOf(cellConfig.get(LuckySheetPropsEnum.BOLD.getCode()));
            if ("1".equals(bold)) {
                result.put("bold", Boolean.TRUE);
            } else {
                result.put("bold", Boolean.FALSE);
            }
        } else {
            result.put("bold", Boolean.FALSE);
        }

        if (cellConfig.containsKey(LuckySheetPropsEnum.ITALIC.getCode())) {
            String italic = String.valueOf(cellConfig.get(LuckySheetPropsEnum.ITALIC.getCode()));
            if ("1".equals(italic)) {
                result.put("italic", Boolean.TRUE);
            } else {
                result.put("italic", Boolean.FALSE);
            }
        } else {
            result.put("italic", Boolean.FALSE);
        }

        if (cellConfig.containsKey(LuckySheetPropsEnum.CANCELLINE.getCode())) {
            String cancleLine = String.valueOf(cellConfig.get(LuckySheetPropsEnum.CANCELLINE.getCode()));
            if ("1".equals(cancleLine)) {
                result.put("strikeOut", Boolean.TRUE);
            } else {
                result.put("strikeOut", Boolean.FALSE);
            }
        } else {
            result.put("strikeOut", Boolean.FALSE);
        }

        if (cellConfig.containsKey(LuckySheetPropsEnum.UNDERLINE.getCode())) {
            String underLine = String.valueOf(cellConfig.get(LuckySheetPropsEnum.UNDERLINE.getCode()));
            if ("1".equals(underLine)) {
                result.put("underLine", (byte) 1);
            } else {
                result.put("underLine", (byte) 0);
            }
        } else {
            result.put("underLine", (byte) 0);
        }

        if (cellConfig.containsKey(LuckySheetPropsEnum.FONTCOLOR.getCode())) {
            String fontColor = String.valueOf(cellConfig.get(LuckySheetPropsEnum.FONTCOLOR.getCode()));
            if (fontColor.contains("rgb")) {
                int[] rgb = StringUtil.rgbStringToRgb(fontColor);
                result.put("fontColor", rgb);
            } else {
                int[] rgb = StringUtil.hexToRgb(fontColor);
                result.put("fontColor", rgb);
            }
        } else {
            int[] rgb = new int[3];
            result.put("fontColor", rgb);
        }

        if (cellConfig.containsKey(LuckySheetPropsEnum.BACKGROUND.getCode())) {
            Object object = cellConfig.get(LuckySheetPropsEnum.BACKGROUND.getCode());
            if (object != null) {
                String background = String.valueOf(object);
                int[] rgb = StringUtil.hexToRgb(background);
                result.put("background", rgb);
            } else {
                result.put("background", null);
            }
        } else {

            result.put("background", null);
        }

        result.put("horizontal", cellConfig.getOrDefault(LuckySheetPropsEnum.HORIZONTALTYPE.getCode(), "0"));
        result.put("vertical", cellConfig.getOrDefault(LuckySheetPropsEnum.VERTICALTYPE.getCode(), "0"));

        result.put("fontSize", cellConfig.getOrDefault(LuckySheetPropsEnum.FONTSIZE.getCode(), "10"));
        return result;
    }

    public void createCells(int maxX, int maxY, Map<String, Object> rowlen) {
        if (rowlen == null) {
            rowlen = new HashMap<>();
        }
        for (int i = 0; i <= maxX; i++) {
            SXSSFRow row = this.sheet.createRow(i);
            if (rowlen.get(String.valueOf(i)) != null) {
                try {
                    row.setHeightInPoints(Float.parseFloat(String.valueOf(rowlen.get(String.valueOf(i)))));
                } catch (Exception e) {
                    row.setHeightInPoints(20.0F);
                }
            }
            for (int j = 0; j <= maxY; j++) {
                row.createCell(j);
            }
        }
    }

    public void setCellValues(List<Map<String, Object>> cellDatas, Map<String, Map<String, Object>> hyperlinks, List<Object> borderInfos) {
        if (cellDatas != null && cellDatas.size() > 0) {
            Map<String, XSSFCellStyle> cellStyleMap = new HashMap<>();
            for (Map<String, Object> cellData : cellDatas) {
                Cell cell = getCell(Integer.parseInt(String.valueOf(cellData.get(LuckySheetPropsEnum.R.getCode()))), Integer.parseInt(String.valueOf(cellData.get(LuckySheetPropsEnum.C.getCode()))));
                if (cellStyleMap.size() < 64000) {
                    CellStyle cellStyle = getCellStyle(cellData, cellStyleMap);
                    cell.setCellStyle(cellStyle);
                }
                Map<String, Object> cellValue = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
                Map<String, Object> cellType = (Map<String, Object>) cellValue.get(LuckySheetPropsEnum.CELLTYPE.getCode());
                String t = "s";
                if (cellType != null) {
                    t = String.valueOf(cellType.get(LuckySheetPropsEnum.TYPE.getCode()));
                }
                if (LuckySheetPropsEnum.INLINESTR.getCode().equals(t)) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) cellType.get(LuckySheetPropsEnum.STRING.getCode());
                    if (list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode()) != null) {
                        cell.setCellValue(String.valueOf(list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode())));
                    } else {
                        cell.setCellValue("");
                    }

                } else if (cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()) != null) {
                    cell.setCellValue(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())));
                } else {
                    cell.setCellValue("");
                }


                Map<String, Object> mergeCell = (Map<String, Object>) cellValue.get(LuckySheetPropsEnum.MERGECELLS.getCode());
                if (mergeCell != null &&
                        mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    int firstRow = Integer.parseInt(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
                    int lastRow = Integer.parseInt(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.parseInt(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
                    int firstCol = Integer.parseInt(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
                    int lastCol = Integer.parseInt(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.parseInt(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
                    mergeCell(firstRow, lastRow, firstCol, lastCol);
                }
            }
        }

        setCellBorder(borderInfos);
    }

    private void setCellBorder(List<Object> borderInfos) {
        if (borderInfos != null && borderInfos.size() > 0) {
            for (Object borderInfo : borderInfos) {
                Map<String, Object> border = (Map<String, Object>) borderInfo;
                String rangeType = (String) border.get(LuckySheetPropsEnum.RANGETYPE.getCode());
                if (LuckySheetPropsEnum.BORDERRANGE.getCode().equals(rangeType)) {
                    String borderType = (String) border.get(LuckySheetPropsEnum.BORDERTYPE.getCode());
                    List<Map<String, Object>> ranges = (List<Map<String, Object>>) border.get(LuckySheetPropsEnum.BORDERRANGE.getCode());
                    for (Map<String, Object> range : ranges) {
                        List<Integer> rows = (List<Integer>) range.get(LuckySheetPropsEnum.BORDERROWRANGE.getCode());
                        List<Integer> cols = (List<Integer>) range.get(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode());
                        int rs = rows.get(1) - rows.get(0) + 1;
                        int cs = cols.get(1) - cols.get(0) + 1;
                        for (int k = 0; k < rs; k++) {
                            for (int k2 = 0; k2 < cs; k2++) {
                                Cell cell = getCell(rows.get(0) + k, cols.get(0) + k2);
                                CellStyle cellStyle = cell.getCellStyle();
                                if (BorderTypeEnum.BORDERALL.getCode().equals(borderType)) {
                                    cellStyle.setBorderBottom(BorderStyle.THIN);
                                    cellStyle.setBorderLeft(BorderStyle.THIN);
                                    cellStyle.setBorderRight(BorderStyle.THIN);
                                    cellStyle.setBorderTop(BorderStyle.THIN);
                                } else if (BorderTypeEnum.BORDERBOTTOM.getCode().equals(borderType)) {
                                    cellStyle.setBorderBottom(BorderStyle.THIN);
                                } else if (BorderTypeEnum.BORDERLEFT.getCode().equals(borderType)) {
                                    cellStyle.setBorderLeft(BorderStyle.THIN);
                                } else if (BorderTypeEnum.BORDERRIGHT.getCode().equals(borderType)) {
                                    cellStyle.setBorderRight(BorderStyle.THIN);
                                } else if (BorderTypeEnum.BORDERTOP.getCode().equals(borderType)) {
                                    cellStyle.setBorderTop(BorderStyle.THIN);
                                }
                            }
                        }
                    }
                } else {

                    Map<String, Object> value = (Map<String, Object>) border.get(LuckySheetPropsEnum.RANGECELLVALUE.getCode());
                    int r = (Integer) value.get("row_index");
                    int c = (Integer) value.get("col_index");
                    Cell cell = getCell(r, c);
                    CellStyle cellStyle = cell.getCellStyle();
                    Object rightBorder = value.get("r");
                    if (rightBorder != null) {
                        cellStyle.setBorderRight(BorderStyle.THIN);
                    }
                    Object leftBorder = value.get("l");
                    if (leftBorder != null) {
                        cellStyle.setBorderLeft(BorderStyle.THIN);
                    }
                    Object topBorder = value.get("t");
                    if (topBorder != null) {
                        cellStyle.setBorderTop(BorderStyle.THIN);
                    }
                    Object bottomBorder = value.get("b");
                    if (bottomBorder != null) {
                        cellStyle.setBorderBottom(BorderStyle.THIN);
                    }
                }
            }
        }
    }

    private Cell getCell(int rowIndex, int colIndex) {
        SXSSFRow row = this.sheet.getRow(rowIndex);
        return row.getCell(colIndex);
    }

    private void mergeCell(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        this.sheet.addMergedRegion(region);
    }

    private CellStyle getCellStyle(Map<String, Object> cellData, Map<String, XSSFCellStyle> cellStyleMap) {
        Map<String, Object> cellConfig = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
        Map<String, Object> styleMap = getCellStyleMap(cellConfig);
        String md5Key = Md5Util.generateMd5(JsonHelper.writeObjectAsString(styleMap));
        XSSFCellStyle cellStyle = cellStyleMap.get(md5Key);
        if (cellStyle != null) {
            return cellStyle;
        }
        cellStyle = (XSSFCellStyle) this.wb.createCellStyle();
        XSSFFont font = (XSSFFont) this.wb.createFont();

        String fontName = String.valueOf(styleMap.get("fontName"));
        font.setFontName(fontName);

        boolean bold = (Boolean) styleMap.get("bold");
        font.setBold(bold);

        boolean italic = (Boolean) styleMap.get("italic");
        font.setItalic(italic);

        boolean strikeOut = (Boolean) styleMap.get("strikeOut");
        font.setStrikeout(strikeOut);

        byte underLine = (Byte) styleMap.get("underLine");
        font.setUnderline(underLine);

        int[] fontColor = (int[]) styleMap.get("fontColor");
        font.setColor(new XSSFColor(new Color(fontColor[0], fontColor[1], fontColor[2]), new DefaultIndexedColorMap()));

        if (styleMap.get("background") != null) {
            int[] backgroundXSSFColor = (int[]) styleMap.get("background");
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(backgroundXSSFColor[0], backgroundXSSFColor[1], backgroundXSSFColor[2]), new DefaultIndexedColorMap()));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        String horizontal = String.valueOf(styleMap.get("horizontal"));
        switch (horizontal) {
            case "0":
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                break;
            case "1":
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                break;
            case "2":
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                break;
        }
        String vertical = String.valueOf(styleMap.get("vertical"));
        switch (vertical) {
            case "0":
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                break;
            case "1":
                cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
                break;
            case "2":
                cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
                break;
        }

        String fontSize = String.valueOf(styleMap.get("fontSize"));
        font.setFontHeightInPoints(Short.parseShort(fontSize));
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyleMap.put(md5Key, cellStyle);
        return cellStyle;
    }
}



