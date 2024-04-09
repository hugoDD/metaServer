package cn.granitech.variantorm.persistence.queryCompiler;

import cn.granitech.variantorm.metadata.MetadataManager;

public interface SqlCompiler {
    SelectStatement compileEasySql(MetadataManager metadata, String eSql);
}
