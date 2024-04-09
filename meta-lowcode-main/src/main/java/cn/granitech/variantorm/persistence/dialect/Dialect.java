package cn.granitech.variantorm.persistence.dialect;

public interface Dialect {
    String getQuotedIdentifier(String identifier);
}
