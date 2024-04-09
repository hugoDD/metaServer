package cn.granitech.business.service;

import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2021/1/20 14:11
 * @Version: 1.0
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EntityManagerTest {

    @Autowired
    EntityManagerService entityManagerService;

    //@Test
    public void testCreateDataListViewEntity() {
        Entity dlvEntity = new Entity();
        dlvEntity.setEntityCode(9);
        dlvEntity.setName("DataListView");
        dlvEntity.setLabel("数据列表视图");
        dlvEntity.setPhysicalName("t_data_list_view");
        dlvEntity.setAuthorizable(false);
        dlvEntity.setShareable(false);
        dlvEntity.setShareable(false);
        dlvEntity.setDetailEntityFlag(false);
        //
        Field idField = new Field();
        idField.setName("dataListViewId");
        idField.setLabel("id主键");
        idField.setPhysicalName("dataListViewId");
        idField.setType(FieldTypes.PRIMARYKEY);
        idField.setIdFieldFlag(true);
        dlvEntity.setIdField(idField);
        entityManagerService.createEntity(dlvEntity, null);

        Field entityCodeField = new Field();
        entityCodeField.setName("entityCode");
        entityCodeField.setLabel("实体编码");
        entityCodeField.setPhysicalName("entityCode");
        entityCodeField.setType(FieldTypes.INTEGER);
        entityCodeField.setNullable(false);
        entityCodeField.setCreatable(true);
        entityCodeField.setUpdatable(false);
        entityCodeField.setOwner(dlvEntity);
        entityManagerService.createPlainField(dlvEntity.getEntityCode(), entityCodeField);

        Field viewNameField = new Field();
        viewNameField.setName("viewName");
        viewNameField.setLabel("列表视图名称");
        viewNameField.setPhysicalName("viewName");
        viewNameField.setType(FieldTypes.TEXT);
        viewNameField.setNullable(false);
        viewNameField.setCreatable(true);
        viewNameField.setUpdatable(true);
        viewNameField.setOwner(dlvEntity);
        entityManagerService.createPlainField(dlvEntity.getEntityCode(), viewNameField);

        Field headerJsonField = new Field();
        headerJsonField.setName("headerJson");
        headerJsonField.setLabel("表头Json");
        headerJsonField.setPhysicalName("headerJson");
        headerJsonField.setType(FieldTypes.TEXTAREA);
        headerJsonField.setNullable(true);
        headerJsonField.setCreatable(true);
        headerJsonField.setUpdatable(true);
        headerJsonField.setOwner(dlvEntity);
        entityManagerService.createPlainField(dlvEntity.getEntityCode(), headerJsonField);

        Field filterJsonField = new Field();
        filterJsonField.setName("filterJson");
        filterJsonField.setLabel("筛选条件Json");
        filterJsonField.setPhysicalName("filterJson");
        filterJsonField.setType(FieldTypes.TEXTAREA);
        filterJsonField.setNullable(true);
        filterJsonField.setCreatable(true);
        filterJsonField.setUpdatable(true);
        filterJsonField.setOwner(dlvEntity);
        entityManagerService.createPlainField(dlvEntity.getEntityCode(), filterJsonField);

        Field paginationJsonField = new Field();
        paginationJsonField.setName("paginationJson");
        paginationJsonField.setLabel("分页Json");
        paginationJsonField.setPhysicalName("paginationJson");
        paginationJsonField.setType(FieldTypes.TEXTAREA);
        paginationJsonField.setNullable(true);
        paginationJsonField.setCreatable(true);
        paginationJsonField.setUpdatable(true);
        paginationJsonField.setOwner(dlvEntity);
        entityManagerService.createPlainField(dlvEntity.getEntityCode(), paginationJsonField);
    }

    //@Test
    public void testCreateSortJsonFieldOfDLVEntity() {
        Entity dlvEntity = entityManagerService.getMetadataManager().getEntity("DataListView");
        Field sortJsonField = new Field();
        sortJsonField.setName("sortJson");
        sortJsonField.setLabel("排序Json");
        sortJsonField.setPhysicalName("sortJson");
        sortJsonField.setType(FieldTypes.TEXTAREA);
        sortJsonField.setNullable(true);
        sortJsonField.setCreatable(true);
        sortJsonField.setUpdatable(true);
        sortJsonField.setOwner(dlvEntity);

        entityManagerService.createPlainField(dlvEntity.getEntityCode(), sortJsonField);
    }

}
