package cn.granitech.variantorm.dbmapping.ddl;


import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;

public interface DBDDL {
    void alterFieldColumn(Field field);

    void deleteEntityTable(Entity entity);

    void deleteFieldColumn(Field field);

    void createEntityTable(Entity entity);

    void createFieldColumn(Field field);
}
