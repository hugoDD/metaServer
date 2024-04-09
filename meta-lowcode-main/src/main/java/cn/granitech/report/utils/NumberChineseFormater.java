package cn.granitech.report.utils;


public class NumberChineseFormater {
    private static final String[] simpleDigits = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private static final String[] traditionalDigits = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};


    private static final String[] simpleUnits = new String[]{"", "十", "百", "千"};

    private static final String[] traditionalUnits = new String[]{"", "拾", "佰", "仟"};


    public static String format(double amount, boolean isUseTraditional) {
        return format(amount, isUseTraditional, false);
    }


    public static String format(double amount, boolean isUseTraditional, boolean isMoneyMode) {
        String[] numArray = isUseTraditional ? traditionalDigits : simpleDigits;

        if (amount > 9.999999999999998E13D || amount < -9.999999999999998E13D) {
            throw new IllegalArgumentException("Number support only: (-99999999999999.99 ～ 99999999999999.99)！");
        }

        boolean negative = false;
        if (amount < 0.0D) {
            negative = true;
            amount = -amount;
        }

        long temp = Math.round(amount * 100.0D);
        int numFen = (int) (temp % 10L);
        temp /= 10L;
        int numJiao = (int) (temp % 10L);
        temp /= 10L;


        int[] parts = new int[20];
        int numParts = 0;
        for (int i = 0; temp != 0L; i++) {
            int part = (int) (temp % 10000L);
            parts[i] = part;
            numParts++;
            temp /= 10000L;
        }

        boolean beforeWanIsZero = true;

        String chineseStr = "";
        for (int j = 0; j < numParts; j++) {
            String partChinese = toChinese(parts[j], isUseTraditional);
            if (j % 2 == 0) {
                beforeWanIsZero = (partChinese == null || partChinese.length() == 0);
            }

            if (j != 0) {
                if (j % 2 == 0) {
                    chineseStr = "亿" + chineseStr;
                } else if ("".equals(partChinese) && false == beforeWanIsZero) {

                    chineseStr = "零" + chineseStr;
                } else {
                    if (parts[j - 1] < 1000 && parts[j - 1] > 0) {
                        chineseStr = "零" + chineseStr;
                    }
                    chineseStr = "万" + chineseStr;
                }
            }

            chineseStr = partChinese + chineseStr;
        }


        if ("".equals(chineseStr)) {
            chineseStr = numArray[0];
        }

        if (negative) {
            chineseStr = "负" + chineseStr;
        }


        if (numFen != 0 || numJiao != 0) {
            if (numFen == 0) {
                chineseStr = chineseStr + (isMoneyMode ? "元" : "点") + numArray[numJiao] + (isMoneyMode ? "角" : "");
            } else if (numJiao == 0) {
                chineseStr = chineseStr + (isMoneyMode ? "元零" : "点零") + numArray[numFen] + (isMoneyMode ? "分" : "");
            } else {
                chineseStr = chineseStr + (isMoneyMode ? "元" : "点") + numArray[numJiao] + (isMoneyMode ? "角" : "") + numArray[numFen] + (isMoneyMode ? "分" : "");
            }

        } else if (isMoneyMode) {

            chineseStr = chineseStr + "元整";
        }

        return chineseStr;
    }


    private static String toChinese(int amountPart, boolean isUseTraditional) {
        String[] numArray = isUseTraditional ? traditionalDigits : simpleDigits;
        String[] units = isUseTraditional ? traditionalUnits : simpleUnits;

        int temp = amountPart;

        String chineseStr = "";
        boolean lastIsZero = true;
        for (int i = 0; temp > 0 &&
                temp != 0; i++) {


            int digit = temp % 10;
            if (digit == 0) {
                if (false == lastIsZero) {
                    chineseStr = "零" + chineseStr;
                }
                lastIsZero = true;
            } else {
                chineseStr = numArray[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp /= 10;
        }
        return chineseStr;
    }
}



