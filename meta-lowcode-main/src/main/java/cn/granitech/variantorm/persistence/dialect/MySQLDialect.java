package cn.granitech.variantorm.persistence.dialect;


public class MySQLDialect implements Dialect {
    public String getQuotedIdentifier(String identifier) {
        if (identifier == null) {
            throw new NullPointerException("identifier");
        } else {
            return "`identifier`";
        }
    }

    public MySQLDialect() {
    }
}
