package cn.granitech.business.service;

import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.constant.SystemEntityCodes;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2021/3/2 15:28
 * @Version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouterMenuTest {

    @Autowired
    EntityManagerService entityManagerService;

    //@Test
    public void createRouterMenuEntity() {
        Entity rmEntity = new Entity();
        rmEntity.setEntityCode(SystemEntityCodes.ROUTER_MENU_CODE);
        rmEntity.setName(SystemEntities.RouterMenu);
        rmEntity.setLabel("系统导航菜单");
        rmEntity.setPhysicalName("t_router_menu");
        rmEntity.setAuthorizable(false);
        rmEntity.setShareable(false);
        rmEntity.setShareable(false);
        rmEntity.setDetailEntityFlag(false);
        //
        Field idField = new Field();
        idField.setName("routerMenuId");
        idField.setLabel("id主键");
        idField.setPhysicalName("routerMenuId");
        idField.setType(FieldTypes.PRIMARYKEY);
        idField.setIdFieldFlag(true);
        rmEntity.setIdField(idField);
        entityManagerService.createEntity(rmEntity, null);

        Field menuJsonField = new Field();
        menuJsonField.setName("menuJson");
        menuJsonField.setLabel("菜单Json");
        menuJsonField.setPhysicalName("menuJson");
        menuJsonField.setType(FieldTypes.TEXTAREA);
        menuJsonField.setNullable(false);
        menuJsonField.setCreatable(true);
        menuJsonField.setUpdatable(true);
        menuJsonField.setOwner(rmEntity);
        entityManagerService.createPlainField(rmEntity.getEntityCode(), menuJsonField);
    }

    //@Test
    public void createDepartmentNodeEntity() {
        Entity dnEntity = new Entity();
        dnEntity.setEntityCode(SystemEntityCodes.DEPARTMENT_NODE_CODE);
        dnEntity.setName(SystemEntities.DepartmentNode);
        dnEntity.setLabel("部门节点关系");
        dnEntity.setPhysicalName("t_department_node");
        dnEntity.setAuthorizable(false);
        dnEntity.setShareable(false);
        dnEntity.setShareable(false);
        dnEntity.setDetailEntityFlag(false);
        //
        Field idField = new Field();
        idField.setName("departmentNodeId");
        idField.setLabel("id主键");
        idField.setPhysicalName("departmentNodeId");
        idField.setType(FieldTypes.PRIMARYKEY);
        idField.setIdFieldFlag(true);
        dnEntity.setIdField(idField);
        entityManagerService.createEntity(dnEntity, null);
    }

    //@Test
    public void createParentAndChildFieldOfDepartmentNode() {
        Entity dnEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.DepartmentNode);
        Entity departmentEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.Department);

        Field parentField = new Field();
        parentField.setName("parentDepartmentId");
        parentField.setLabel("父级部门Id");
        parentField.setPhysicalName("parentDepartmentId");
        parentField.setType(FieldTypes.REFERENCE);
        parentField.setReferTo(new HashSet<Entity>() {{
            add(departmentEntity);
        }});
        parentField.setNullable(false);
        parentField.setCreatable(true);
        parentField.setUpdatable(true);
        parentField.setOwner(dnEntity);
        entityManagerService.createPlainField(dnEntity.getEntityCode(), parentField);

        Field childField = new Field();
        childField.setName("childDepartmentId");
        childField.setLabel("下级部门Id");
        childField.setPhysicalName("childDepartmentId");
        childField.setType(FieldTypes.REFERENCE);
        childField.setReferTo(new HashSet<Entity>() {{
            add(departmentEntity);
        }});
        childField.setNullable(false);
        childField.setCreatable(true);
        childField.setUpdatable(true);
        childField.setOwner(dnEntity);
        entityManagerService.createPlainField(dnEntity.getEntityCode(), childField);
    }

}
