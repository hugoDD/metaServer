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
 * @Date: 2021/2/14 13:27
 * @Version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRolesTest {

    @Autowired
    EntityManagerService entityManagerService;

    //@Test
    public void createRoleEntity() {
        Entity roleEntity = new Entity();
        roleEntity.setEntityCode(SystemEntityCodes.ROLE_CODE);
        roleEntity.setName("Role");
        roleEntity.setLabel("权限角色");
        roleEntity.setPhysicalName("t_role");
        roleEntity.setAuthorizable(false);
        roleEntity.setShareable(false);
        roleEntity.setShareable(false);
        roleEntity.setDetailEntityFlag(false);
        //
        Field idField = new Field();
        idField.setName("roleId");
        idField.setLabel("id主键");
        idField.setPhysicalName("roleId");
        idField.setType(FieldTypes.PRIMARYKEY);
        idField.setIdFieldFlag(true);
        roleEntity.setIdField(idField);
        entityManagerService.createEntity(roleEntity, null);

        Field descriptionField = new Field();
        descriptionField.setName("description");
        descriptionField.setLabel("角色说明");
        descriptionField.setPhysicalName("description");
        descriptionField.setType(FieldTypes.TEXTAREA);
        descriptionField.setNullable(true);
        descriptionField.setCreatable(true);
        descriptionField.setUpdatable(true);
        descriptionField.setOwner(roleEntity);
        entityManagerService.createPlainField(roleEntity.getEntityCode(), descriptionField);
    }

    //@Test
    public void createRoleNameField() {
        Entity roleEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.Role);
        Field roleNameField = new Field();
        roleNameField.setName("roleName");
        roleNameField.setLabel("角色名称");
        roleNameField.setPhysicalName("roleName");
        roleNameField.setType(FieldTypes.TEXT);
        roleNameField.setNullable(false);
        roleNameField.setCreatable(true);
        roleNameField.setUpdatable(true);
        roleNameField.setOwner(roleEntity);
        entityManagerService.createPlainField(roleEntity.getEntityCode(), roleNameField);

        entityManagerService.updateEntityNameField(roleEntity, roleNameField);
    }

    //@Test
    public void createRolesField() {
        Entity userEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.User);
        Entity roleEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.Role);
        Field rolesField = new Field();
        rolesField.setName("roles");
        rolesField.setLabel("权限角色");
        rolesField.setPhysicalName("roles");
        rolesField.setType(FieldTypes.REFERENCELIST);
        rolesField.setReferTo(new HashSet<Entity>() {{
            add(roleEntity);
        }});
        rolesField.setNullable(true);
        rolesField.setCreatable(true);
        rolesField.setUpdatable(true);
        rolesField.setOwner(userEntity);
        entityManagerService.createPlainField(userEntity.getEntityCode(), rolesField);
    }

    //@Test
    public void createUserDisabledField() {
        Entity userEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.User);
        Field disabledField = new Field();
        disabledField.setName("disabled");
        disabledField.setLabel("是否禁用");
        disabledField.setPhysicalName("disabled");
        disabledField.setType(FieldTypes.BOOLEAN);
        disabledField.setNullable(false);
        disabledField.setCreatable(true);
        disabledField.setUpdatable(true);
        disabledField.setOwner(userEntity);

        entityManagerService.createPlainField(userEntity.getEntityCode(), disabledField);
    }

    //@Test
    public void createRoleDisabledField() {
        Entity roleEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.Role);
        Field disabledField = new Field();
        disabledField.setName("disabled");
        disabledField.setLabel("是否禁用");
        disabledField.setPhysicalName("disabled");
        disabledField.setType(FieldTypes.BOOLEAN);
        disabledField.setNullable(false);
        disabledField.setCreatable(true);
        disabledField.setUpdatable(true);
        disabledField.setOwner(roleEntity);

        entityManagerService.createPlainField(roleEntity.getEntityCode(), disabledField);
    }

    //@Test
    public void createRightJsonField() {
        Entity roleEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.Role);
        Field rightJsonField = new Field();
        rightJsonField.setName("rightJson");
        rightJsonField.setLabel("权限明细");
        rightJsonField.setPhysicalName("rightJson");
        rightJsonField.setType(FieldTypes.TEXTAREA);
        rightJsonField.setNullable(true);
        rightJsonField.setCreatable(true);
        rightJsonField.setUpdatable(true);
        rightJsonField.setOwner(roleEntity);
        entityManagerService.createPlainField(roleEntity.getEntityCode(), rightJsonField);
    }

    //@Test
    public void createSystemSettingEntity() {
        Entity settingEntity = new Entity();
        settingEntity.setEntityCode(SystemEntityCodes.SYSTEM_SETTING_CODE);
        settingEntity.setName("SystemSetting");
        settingEntity.setLabel("系统参数");
        settingEntity.setPhysicalName("t_system_setting");
        settingEntity.setAuthorizable(false);
        settingEntity.setShareable(false);
        settingEntity.setShareable(false);
        settingEntity.setDetailEntityFlag(false);
        //
        Field idField = new Field();
        idField.setName("systemSettingId");
        idField.setLabel("id主键");
        idField.setPhysicalName("systemSettingId");
        idField.setType(FieldTypes.PRIMARYKEY);
        idField.setIdFieldFlag(true);
        settingEntity.setIdField(idField);
        entityManagerService.createEntity(settingEntity, null);

        Field settingNameField = new Field();
        settingNameField.setName("settingName");
        settingNameField.setLabel("参数名");
        settingNameField.setPhysicalName("settingName");
        settingNameField.setType(FieldTypes.TEXT);
        settingNameField.setNullable(false);
        settingNameField.setCreatable(true);
        settingNameField.setUpdatable(true);
        settingNameField.setOwner(settingEntity);
        entityManagerService.createPlainField(settingEntity.getEntityCode(), settingNameField);

        Field settingValueField = new Field();
        settingValueField.setName("settingValue");
        settingValueField.setLabel("参数值");
        settingValueField.setPhysicalName("settingValue");
        settingValueField.setType(FieldTypes.TEXTAREA);
        settingValueField.setNullable(true);
        settingValueField.setCreatable(true);
        settingValueField.setUpdatable(true);
        settingValueField.setOwner(settingEntity);
        entityManagerService.createPlainField(settingEntity.getEntityCode(), settingValueField);

        Field defaultValueField = new Field();
        defaultValueField.setName("defaultValue");
        defaultValueField.setLabel("参数默认值");
        defaultValueField.setPhysicalName("defaultValue");
        defaultValueField.setType(FieldTypes.TEXTAREA);
        defaultValueField.setNullable(true);
        defaultValueField.setCreatable(true);
        defaultValueField.setUpdatable(true);
        defaultValueField.setOwner(settingEntity);
        entityManagerService.createPlainField(settingEntity.getEntityCode(), defaultValueField);
    }

    //@Test
    public void createDepartmentDescription() {
        Entity departmentEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.Department);

        Field descriptionField = new Field();
        descriptionField.setName("description");
        descriptionField.setLabel("部门说明");
        descriptionField.setPhysicalName("description");
        descriptionField.setType(FieldTypes.TEXTAREA);
        descriptionField.setNullable(true);
        descriptionField.setCreatable(true);
        descriptionField.setUpdatable(true);
        descriptionField.setOwner(departmentEntity);
        entityManagerService.createPlainField(departmentEntity.getEntityCode(), descriptionField);
    }

    //@Test
    public void createPresetFilterOfDataListView() {
        Entity dlvEntity = entityManagerService.getMetadataManager().getEntity(SystemEntities.DataListView);

        Field presetFilterField = new Field();
        presetFilterField.setName("presetFilter");
        presetFilterField.setLabel("内置筛选条件");
        presetFilterField.setPhysicalName("presetFilter");
        presetFilterField.setType(FieldTypes.TEXTAREA);
        presetFilterField.setNullable(true);
        presetFilterField.setCreatable(true);
        presetFilterField.setUpdatable(true);
        presetFilterField.setOwner(dlvEntity);
        entityManagerService.createPlainField(dlvEntity.getEntityCode(), presetFilterField);
    }

}
