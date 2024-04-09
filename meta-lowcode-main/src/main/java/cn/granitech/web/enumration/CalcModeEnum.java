package cn.granitech.web.enumration;

public enum CalcModeEnum {
    SUM("sum", "SUM(%s)"),
    COUNT("count", "COUNT(%s)"),
    COUNT_SET("countSet", "COUNT(DISTINCT %s)"),
    AVERAGE("average", "AVG(%s)"),
    MAX("max", "MAX(%s)"),
    MIN("min", "MIN(%s)"),
    CONCAT("concat", "GROUP_CONCAT(%s)"),
    CONCAT_SET("concatSet", "GROUP_CONCAT(DISTINCT %s)");

    String calcMode;
    String calcSql;

    CalcModeEnum(String calcMode, String calcSql) {
        this.calcMode = calcMode;
        this.calcSql = calcSql;
    }

    public static CalcModeEnum getCalcModeEnum(String calcMode) {
        CalcModeEnum[] calcModeEnums = values();
        CalcModeEnum[] var2 = calcModeEnums;
        int var3 = calcModeEnums.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            CalcModeEnum modeEnum = var2[var4];
            if (modeEnum.calcMode.equals(calcMode)) {
                return modeEnum;
            }
        }

        return null;
    }

    public String getCalcSql(String name) {
        return String.format(this.calcSql, name);
    }
}
