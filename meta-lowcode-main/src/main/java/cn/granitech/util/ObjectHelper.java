package cn.granitech.util;

import cn.granitech.variantorm.persistence.EntityRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectHelper {
    private static final Logger log = LoggerFactory.getLogger(ObjectHelper.class);

    public ObjectHelper() {
    }

    public static <T> T clone(T object) {
        try {
            Class<?> clazz = object.getClass();
            Object newObj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                    field.set(newObj, field.get(object));
                }
            }

            return (T) newObj;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static <T> T entityToObj(EntityRecord record, T object) {
        try {
            Collection<cn.granitech.variantorm.pojo.Field> fieldSet = record.getEntity().getFieldSet();
            Map<String, Object> valuesMap = record.getValuesMap();
            List<String> fields = Arrays.stream(object.getClass().getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

            for (cn.granitech.variantorm.pojo.Field field : fieldSet) {
                if (fields.contains(field.getName())) {
                    Field declaredField = object.getClass().getDeclaredField(field.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(object, valuesMap.get(field.getName()));
                }
            }

            return object;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            return object;
        }
    }

    public static void mapToObj(Map<String, Object> map, Object object) {
        try {
            Class<?> clazz = object.getClass();

            for (String key : map.keySet()) {
                Field field;
                try {
                    field = clazz.getDeclaredField(key);
                } catch (NoSuchFieldException e) {
                    log.info("字段={}异常，请仔细核对！", key);
                    continue;
                }

                field.setAccessible(true);
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                    field.set(object, map.get(key));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
