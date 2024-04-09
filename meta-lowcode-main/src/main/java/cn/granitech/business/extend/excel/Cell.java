package cn.granitech.business.extend.excel;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Cell implements Serializable {
    public static final Cell NULL = new Cell(null);
    private static final long serialVersionUID = -1590140578303295189L;
    private final int columnNo;
    private final int rowNo;
    private final Object value;

    public Cell(Object value2) {
        this(value2, -1, -1);
    }

    public Cell(Object value2, int rowNo2, int columnNo2) {
        this.value = value2;
        this.rowNo = rowNo2;
        this.columnNo = columnNo2;
    }

    public int getRowNo() {
        return this.rowNo;
    }

    public int getColumnNo() {
        return this.columnNo;
    }

    public Object getRawValue() {
        return this.value;
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(asString());
    }

    public String asString() {
        return asString(false);
    }

    public String asString(boolean trim) {
        if (this.value == null) {
            return null;
        }
        return trim ? this.value.toString().trim() : this.value.toString();
    }

    public String toString() {
        return asString();
    }

    public Integer asInt() {
        if (isEmpty()) {
            return null;
        }
        if (this.value instanceof Integer) {
            return (Integer) this.value;
        }
        return Integer.valueOf(NumberUtils.toInt(asString().replace(",", "")));
    }

    public Long asLong() {
        if (isEmpty()) {
            return null;
        }
        if (this.value instanceof Long) {
            return (Long) this.value;
        }
        return Long.valueOf(NumberUtils.toLong(asString().replace(",", "")));
    }

    public Double asDouble() {
        if (isEmpty()) {
            return null;
        }
        if (this.value instanceof Double) {
            return (Double) this.value;
        }
        return Double.valueOf(NumberUtils.toDouble(asString(true).replace(",", "")));
    }

    public BigDecimal asBigDecimal() {
        if (isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(this.value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean asBool() {
        if (isEmpty()) {
            return null;
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value;
        }
        return Boolean.valueOf(BooleanUtils.toBoolean(asString(true)));
    }

    public Date asDate() {
        int i = 0;
        if (isEmpty()) {
            return null;
        }
        if (this.value instanceof Date) {
            return (Date) this.value;
        }
        String istr = asString(true);
        if (NumberUtils.isNumber(istr)) {
            return DateUtil.date(NumberUtils.toLong(istr));
        }
        String[] dateFormats = {"yyyy/M/d H:m:s", "yyyy/M/d H:m", "yyyy/M/d", "yyyy-M-d"};
        int length = dateFormats.length;
        while (i < length) {
            try {
                DateTime parsedDate = DateUtil.parse(istr, dateFormats[i]);
                if (parsedDate != null) {
                    return parsedDate.toSqlDate();
                }
                i++;
            } catch (Exception e) {
            }
        }
        return null;
    }
}
