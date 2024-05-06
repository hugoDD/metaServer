package cn.granitech.business.service.dataimport;

import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.utils.BlockUtils.isAllBlocksEmpty(BlockUtils.java:564)
    	at jadx.core.dex.visitors.ExtractFieldInit.getConstructorsList(ExtractFieldInit.java:245)
    	at jadx.core.dex.visitors.ExtractFieldInit.moveCommonFieldsInit(ExtractFieldInit.java:126)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:46)
    */
public class DataExporter {
    public static final int MAX_ROWS = 65534;
    private final Entity dataEntity;
    private final List<Map<String, Object>> queryData;
    private final List<String> selectFields;
    private List<Field> headFields = new ArrayList<>();


    public DataExporter(List<Map<String, Object>> queryData, Entity dataEntity, String selectFields) {
        this.queryData = queryData;
        this.dataEntity = dataEntity;
        this.selectFields = Arrays.stream(selectFields.split(",")).map(String::trim).collect(Collectors.toList());
    }

    protected List<List<String>> buildData() {
        String labelNop = "[无权限]";
        String labelUns = "[暂不支持]";
        List<List<String>> dataList = new ArrayList<>();

        for (Map<String, Object> queryDatum : this.queryData) {
            List<String> cellVals = new ArrayList<>();

            for (int i = 0; i < this.headFields.size(); ++i) {
                FieldType dt = this.headFields.get(i).getType();
                Object cellVal = queryDatum.get(this.selectFields.get(i));
                if (cellVal == null) {
                    cellVals.add("");
                } else {
                    if (dt != FieldTypes.FILE && dt != FieldTypes.PICTURE) {
                        if (dt != FieldTypes.DECIMAL && dt != FieldTypes.INTEGER) {
                            if (dt != FieldTypes.DATETIME && dt != FieldTypes.DATE) {
                                if (dt != FieldTypes.REFERENCE && dt != FieldTypes.ANYREFERENCE) {
                                    if (dt == FieldTypes.OPTION || dt == FieldTypes.STATUS) {
                                        cellVal = ((OptionModel) cellVal).getLabel();
                                    }
                                } else {
                                    cellVal = ((IDName) cellVal).getName();
                                }
                            } else {
                                cellVal = DateUtil.format((Date) cellVal, DatePattern.NORM_DATETIME_FORMAT);
                            }
                        } else {
                            cellVal = cellVal.toString().replaceAll("[^0-9|^.-]", "");
                        }
                    } else {
                        cellVal = "[暂不支持]";
                    }

                    if (cellVal instanceof List) {
                        List<IDName> nameList = (List) cellVal;
                        cellVal = nameList.stream().map(IDName::getName).collect(Collectors.joining(","));
                    }

                    cellVals.add(cellVal.toString());
                }
            }

            dataList.add(cellVals);
        }

        return dataList;
    }

    public void export(OutputStream outputStream) {
        List<String> head = buildHead(this.dataEntity);
        List<List<String>> head4Excel = new ArrayList<>();
        for (String h : head) {
            head4Excel.add(Collections.singletonList(h));
        }
        EasyExcel.write(outputStream).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).registerWriteHandler(buildExcelStyle()).sheet(this.dataEntity.getLabel()).head(head4Excel).doWrite(buildData());
    }

    /* access modifiers changed from: protected */
    public List<String> buildHead(Entity entity) {
        List<String> headList = new ArrayList<>();
        for (String fieIdName : this.selectFields) {
            Field field = getRerField(entity, fieIdName);
            this.headFields.add(field);
            headList.add(field.getLabel());
        }
        return headList;
    }

    private Field getRerField(Entity entity, String name) {
        int dotIndex = name.indexOf(".");
        if (dotIndex == -1) {
            return entity.getField(name);
        }
        String referEntity = name.substring(0, dotIndex);
        return getRerField(entity.getField(referEntity).getReferTo().stream().findFirst().orElse(null), name.substring(dotIndex + 1));
    }

    private HorizontalCellStyleStrategy buildExcelStyle() {
        WriteFont baseFont = new WriteFont();
        baseFont.setFontHeightInPoints((short) 11);
        baseFont.setColor(IndexedColors.BLACK.getIndex());
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setWriteFont(baseFont);
        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        contentStyle.setWriteFont(baseFont);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        return new HorizontalCellStyleStrategy(headStyle, contentStyle);
    }
}
