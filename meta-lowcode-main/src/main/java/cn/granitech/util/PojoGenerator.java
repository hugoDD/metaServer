package cn.granitech.util;

import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;

public class PojoGenerator {
    public static String generatePojoClass(Entity pojoEntity, String packageName) throws IOException {
        TypeSpec.Builder pojoClassBuilder = TypeSpec.classBuilder(pojoEntity.getName()).addModifiers(Modifier.PUBLIC).superclass(ClassName.get("cn.granitech.web.pojo", "BasePojo"));
        for (Field field : pojoEntity.getFieldSet()) {
            if (field.getType() == FieldTypes.PRIMARYKEY || field.getType() == FieldTypes.REFERENCE || field.getType() == FieldTypes.ANYREFERENCE) {
                addFieldAndMethods(pojoClassBuilder, field, ID.class, "get", true);
            } else if (field.getType() == FieldTypes.BOOLEAN) {
                addFieldAndMethods(pojoClassBuilder, field, Boolean.class, "is", true);
            } else if (field.getType() == FieldTypes.INTEGER || field.getType() == FieldTypes.PERCENT || field.getType() == FieldTypes.OPTION) {
                addFieldAndMethods(pojoClassBuilder, field, Integer.class, "get", true);
            } else if (field.getType() == FieldTypes.DECIMAL || field.getType() == FieldTypes.MONEY) {
                addFieldAndMethods(pojoClassBuilder, field, BigDecimal.class, "get", true);
            } else if (field.getType() == FieldTypes.TEXT || field.getType() == FieldTypes.EMAIL || field.getType() == FieldTypes.URL || field.getType() == FieldTypes.TEXTAREA || field.getType() == FieldTypes.TAG || field.getType() == FieldTypes.PASSWORD || field.getType() == FieldTypes.PICTURE || field.getType() == FieldTypes.FILE) {
                addFieldAndMethods(pojoClassBuilder, field, String.class, "get", true);
            } else if (field.getType() == FieldTypes.DATE) {
                addFieldAndMethods(pojoClassBuilder, field, Date.class, "get", false);
            } else if (field.getType() == FieldTypes.DATETIME) {
                addFieldAndMethods(pojoClassBuilder, field, java.util.Date.class, "get", true);
            } else if (field.getType() == FieldTypes.REFERENCELIST) {
                addReferenceListFieldAndMethods(pojoClassBuilder, field);
            }
        }
        return JavaFile.builder(packageName, pojoClassBuilder.build()).indent("    ").build().toString();
    }

    private static void addOptionFieldAndMethods(TypeSpec.Builder pojoClassBuilder, Field field) {
        ClassName stringClass = ClassName.get("java.lang", "String");
        ClassName objectClass = ClassName.get("java.lang", "Object");
        TypeName mapOfStringObject = ParameterizedTypeName.get(ClassName.get("java.util", "Map"), stringClass, objectClass);
        FieldSpec fieldSpec = FieldSpec.builder(mapOfStringObject, field.getName()).addModifiers(new Modifier[]{Modifier.PRIVATE}).build();
        MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + firstLetterToUpperCase(field.getName())).addModifiers(Modifier.PUBLIC).returns(mapOfStringObject).addStatement(String.format("%s = (%s) _entityRecord.getFieldValue(\"%s\")", field.getName(), "Map<String, Object>", field.getName())).addStatement(String.format("return %s", field.getName()), new Object[0]).build();
        pojoClassBuilder.addField(fieldSpec).addMethod(getMethodSpec).addMethod(MethodSpec.methodBuilder("set" + firstLetterToUpperCase(field.getName())).addModifiers(Modifier.PUBLIC).addParameter(mapOfStringObject, field.getName()).addStatement(String.format("this.%s = %s", field.getName(), field.getName())).addStatement(String.format("_entityRecord.setFieldValue(\"%s\", %s)", field.getName(), field.getName()), new Object[0]).build());
    }

    private static void addReferenceListFieldAndMethods(TypeSpec.Builder pojoClassBuilder, Field field) {
        TypeName arrayOfID = ArrayTypeName.of(ID.class);
        FieldSpec fieldSpec = FieldSpec.builder(arrayOfID, field.getName()).addModifiers(new Modifier[]{Modifier.PRIVATE}).build();
        MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + firstLetterToUpperCase(field.getName())).addModifiers(Modifier.PUBLIC).returns(arrayOfID).addStatement(String.format("%s = (%s) _entityRecord.getFieldValue(\"%s\")", field.getName(), "ID[]", field.getName())).addStatement(String.format("return %s", field.getName()), new Object[0]).build();
        pojoClassBuilder.addField(fieldSpec).addMethod(getMethodSpec).addMethod(MethodSpec.methodBuilder("set" + firstLetterToUpperCase(field.getName())).addModifiers(Modifier.PUBLIC).addParameter(arrayOfID, field.getName()).addStatement(String.format("this.%s = %s", field.getName(), field.getName())).addStatement(String.format("_entityRecord.setFieldValue(\"%s\", %s)", field.getName(), field.getName()), new Object[0]).build());
    }

    private static void addFieldAndMethods(TypeSpec.Builder pojoClassBuilder, Field field, Type type, String getPrefix, boolean usingShortTypeName) {
        FieldSpec fieldSpec = makeFieldSpec(field, type);
        MethodSpec getMethodSpec = makeGetMethodSpec(field, type, getPrefix, usingShortTypeName);
        pojoClassBuilder.addField(fieldSpec).addMethod(getMethodSpec).addMethod(makeSetMethodSpec(field, type));
    }

    private static FieldSpec makeFieldSpec(Field field, Type type) {
        return FieldSpec.builder(type, field.getName()).addModifiers(new Modifier[]{Modifier.PRIVATE}).build();
    }

    private static MethodSpec makeGetMethodSpec(Field field, Type type, String methodPrefix, boolean usingShortTypeName) {
        String typeName;
        if (!usingShortTypeName) {
            typeName = type.getTypeName();
        } else {
            typeName = type.getTypeName().substring(type.getTypeName().lastIndexOf(".") + 1);
        }
        return MethodSpec.methodBuilder(methodPrefix + firstLetterToUpperCase(field.getName())).addModifiers(Modifier.PUBLIC).returns(type).addStatement(String.format("%s = (%s) _entityRecord.getFieldValue(\"%s\")", field.getName(), typeName, field.getName())).addStatement(String.format("return %s", field.getName()), new Object[0]).build();
    }

    private static MethodSpec makeSetMethodSpec(Field field, Type type) {
        return MethodSpec.methodBuilder("set" + firstLetterToUpperCase(field.getName())).addModifiers(Modifier.PUBLIC).addParameter(type, field.getName()).addStatement(String.format("this.%s = %s", field.getName(), field.getName())).addStatement(String.format("_entityRecord.setFieldValue(\"%s\", %s)", field.getName(), field.getName()), new Object[0]).build();
    }

    private static String firstLetterToUpperCase(String str) {
        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }
}
