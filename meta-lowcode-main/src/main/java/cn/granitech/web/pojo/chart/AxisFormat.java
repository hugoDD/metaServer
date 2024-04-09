package cn.granitech.web.pojo.chart;

public class AxisFormat {
    private int decimalPlaces;
    private String numericUnits;
    private boolean thousandsSeparator;

    public AxisFormat() {
    }

    public AxisFormat(boolean thousandsSeparator2, int decimalPlaces2, String numericUnits2) {
        this.thousandsSeparator = thousandsSeparator2;
        this.decimalPlaces = decimalPlaces2;
        this.numericUnits = numericUnits2;
    }

    public boolean isThousandsSeparator() {
        return this.thousandsSeparator;
    }

    public void setThousandsSeparator(boolean thousandsSeparator2) {
        this.thousandsSeparator = thousandsSeparator2;
    }

    public int getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces2) {
        this.decimalPlaces = decimalPlaces2;
    }

    public String getNumericUnits() {
        return this.numericUnits;
    }

    public void setNumericUnits(String numericUnits2) {
        this.numericUnits = numericUnits2;
    }
}
