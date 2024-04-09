package cn.granitech.util;

import cn.granitech.variantorm.pojo.Field;
import cn.granitech.web.pojo.layout.FieldProps;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/12/4 17:21
 * @Version: 1.0
 */

public class CommonHelper {

    public static FieldProps copyFieldProps(Field field) {
        FieldProps fieldProps = new FieldProps();
        fieldProps.setName(field.getName());
        fieldProps.setLabel(field.getLabel());
        fieldProps.setType(field.getType().getName());
        fieldProps.setNullable(field.isNullable());
        fieldProps.setCreatable(field.isCreatable());
        fieldProps.setUpdatable(field.isUpdatable());
        fieldProps.setFieldViewModel(field.getFieldViewModel());
        return fieldProps;
    }

}
