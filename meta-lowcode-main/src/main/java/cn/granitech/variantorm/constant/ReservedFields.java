package cn.granitech.variantorm.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.granitech.variantorm.constant.SystemEntities.*;


public class ReservedFields {
    private static Map<String, Set<String>> reservedFieldMap = new HashMap<>();

    public ReservedFields() {
    }

    static {
        reservedFieldMap.put(User, Stream.of("userName", "loginName", "loginPwd", "departmentId", "roles", "disabled").collect(Collectors.toSet()));
        reservedFieldMap.put(Department, Stream.of("departmentName", "parentDepartmentId").collect(Collectors.toSet()));
        reservedFieldMap.put(Role, Stream.of("roleName", "description", "rightJson", "disabled").collect(Collectors.toSet()));
    }

    public static boolean isReservedField(String entityName, String fieldName) {

        if (!CommonFields.containField(fieldName)) {
            return reservedFieldMap.containsKey(entityName) && reservedFieldMap.get(entityName).contains(fieldName);
        } else {
            return true;
        }
    }
}


