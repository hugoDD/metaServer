package cn.granitech.report.enumration;

import cn.granitech.report.utils.NumberChineseFormater;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

public enum FormatTypeEnum {
    NUMBER_CHINESE_FORMATER {
        public String dataFormat(String value, Object format) {
            if (!ObjectUtil.isNull(value) && !ObjectUtil.isNull(format)) {
                boolean isFormat = (Boolean)format;
                if (!isFormat) {
                    return value;
                } else {
                    double amount = Double.parseDouble(value);
                    return NumberChineseFormater.format(amount, false, true);
                }
            } else {
                return value;
            }
        }
    },
    DATETIME_FORMAT {
        public String dataFormat(String value, Object format) {
            if (!ObjectUtil.isNull(value) && !ObjectUtil.isNull(format)) {
                DateTime parse = DateUtil.parse(value);
                return DateUtil.format(parse, format.toString());
            } else {
                return null;
            }
        }
    };

    private static final Map<String, FormatTypeEnum> formatTypeEnumMap = new HashMap<>();

    private FormatTypeEnum() {
    }

    public static FormatTypeEnum getFormatEnumByKey(String key) {
        return formatTypeEnumMap.containsKey(key) ? (FormatTypeEnum)formatTypeEnumMap.get(key) : null;
    }

    public String dataFormat(String value, Object format) {
        return null;
    }

    static {
        formatTypeEnumMap.put("dateTimeFormat", DATETIME_FORMAT);
        formatTypeEnumMap.put("dateFormat", DATETIME_FORMAT);
        formatTypeEnumMap.put("timeFormat", DATETIME_FORMAT);
        formatTypeEnumMap.put("amountFormat", NUMBER_CHINESE_FORMATER);
    }
}
