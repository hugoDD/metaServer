package cn.granitech.business.service.dataimport;

import cn.granitech.business.extend.excel.Cell;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.OptionManagerService;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.RegexHelper;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RecordCheckout {
    private static final String MVAL_SPLIT = "[,，;；]";
    private final Map<Field, Integer> fieldsMapping;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<String> traceLogs = new ArrayList<>();

    protected RecordCheckout(Map<Field, Integer> fieldsMapping2) {
        this.fieldsMapping = fieldsMapping2;
    }

    public EntityRecord checkout(EntityRecord record, Cell[] row) {
        Cell cellValue;
        for (Map.Entry<Field, Integer> e : this.fieldsMapping.entrySet()) {
            int cellIndex = e.getValue();
            if (cellIndex < row.length && (cellValue = row[cellIndex]) != Cell.NULL && !cellValue.isEmpty()) {
                Field field = e.getKey();
                Object value = checkoutFieldValue(field, cellValue, true);
                if (value != null) {
                    record.setFieldValue(field.getName(), value);
                } else {
                    putTraceLog(cellValue, "异常字段：" + field.getName() + "，value：null");
                }
            }
        }
        return record;
    }

    /* access modifiers changed from: protected */
    public Object checkoutFieldValue(Field field, Cell cell, boolean validate) {
        FieldType dt = field.getType();
        if (dt == FieldTypes.INTEGER) {
            return cell.asInt();
        }
        if (dt == FieldTypes.DECIMAL || dt == FieldTypes.MONEY) {
            return cell.asBigDecimal();
        }
        if (dt == FieldTypes.DATE || dt == FieldTypes.DATETIME) {
            return checkoutDateValue(cell);
        }
        if (dt == FieldTypes.OPTION) {
            return checkoutOptionValue(field, cell);
        }
        if (dt == FieldTypes.REFERENCE) {
            return checkoutReferenceValue(field, cell);
        }
        if (dt == FieldTypes.REFERENCELIST) {
            return checkoutN2NReferenceValue(field, cell);
        }
        if (dt == FieldTypes.BOOLEAN) {
            return cell.asBool() || "是".equals(cell.asString()) || "Y".equalsIgnoreCase(cell.asString());
        } else if (dt == FieldTypes.FILE || dt == FieldTypes.PICTURE) {
            return checkoutFileOrImage(cell);
        } else {
            String text = cell.asString();
            if (text != null) {
                text = text.trim();
            }
            if (!validate) {
                return text;
            }
            if (dt == FieldTypes.EMAIL) {
                if (!RegexHelper.isEmail(text)) {
                    return null;
                }
                return text;
            } else if (dt != FieldTypes.URL || RegexHelper.isUrl(text)) {
                return text;
            } else {
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Integer checkoutOptionValue(Field field, Cell cell) {
        String val = cell.asString();
        for (OptionModel optionModel : SpringHelper.getBean(OptionManagerService.class).getOptionList(field.getOwner().getName(), field.getName())) {
            if (optionModel.getLabel().equals(val.trim())) {
                return optionModel.getValue();
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public ID checkoutReferenceValue(Field field, Cell cell) {
        String val = cell.asString();
        ID referenceId = SpringHelper.getBean(CrudService.class).queryReferenceIdByName(field.getReferTo().iterator().next(), val);
        if (referenceId != null) {
            return referenceId;
        }
        this.log.warn("Reference ID `{}` not exists", val);
        return null;
    }

    /* access modifiers changed from: protected */
    public ID[] checkoutN2NReferenceValue(Field field, Cell cell) {
        String val = cell.asString();
        Set<ID> ids = new LinkedHashSet<>();
        for (String s : val.split(MVAL_SPLIT)) {
            ID id = checkoutReferenceValue(field, new Cell(s, cell.getRowNo(), cell.getColumnNo()));
            if (id != null) {
                ids.add(id);
            }
        }
        return ids.toArray(new ID[0]);
    }

    /* access modifiers changed from: protected */
    public Date checkoutDateValue(Cell cell) {
        String date2str = cell.asString();
        Date date = cell.asDate();
        if (date != null) {
            return date;
        }

        DateTime dt = DateUtil.parse(date2str);
        if (dt != null) {
            date = dt.toSqlDate();
        }

        return date;
    }

    /* access modifiers changed from: protected */
    public String checkoutFileOrImage(Cell cell) {
        String val = cell.asString();
        List<String> urls = new ArrayList<>();
        for (String s : val.split(MVAL_SPLIT)) {
            if (RegexHelper.isUrl(s)) {
                urls.add(s);
            }
        }
        if (urls.isEmpty()) {
            return null;
        }
        return JsonHelper.writeObjectAsString(urls);
    }

    public List<String> getTraceLogs() {
        return this.traceLogs;
    }

    private void putTraceLog(Cell cell, String log2) {
        StringBuilder name = new StringBuilder();
        for (int num = cell.getColumnNo(); num >= 0; num = ((int) Math.floor(num / 26d)) - 1) {
            name.insert(0, (char) ((num % 26) + 65));
        }
        name.append(cell.getRowNo() + 1);
        this.traceLogs.add(name + ":" + log2);
    }
}
