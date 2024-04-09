package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.exception.DataAccessException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.impl.VirtualField;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReferenceListField extends VirtualField {
    public static final ID[] EMPTY_ID_ARRAY;

    public String getName() {
        return "ReferenceList";
    }

    static {
        EMPTY_ID_ARRAY = new ID[0];
    }

    public final Object readDBValue(PersistenceManager pm, Field field, ID objectId) {
        throw new UnsupportedOperationException();
    }

    public Object fromJson(Object jsonValue) {
        if (jsonValue == null) {
            return null;
        } else {

            if(jsonValue instanceof List){
                List<String> list = (List)jsonValue;
               return list.stream().map(ID::new).collect(Collectors.toList());
            }else {
                return null;
            }

        }
    }

    public ReferenceListField() {
        super(EMPTY_ID_ARRAY.getClass());
    }

    public Object readDBValue(PersistenceManager pm, Field field, ResultSet rs, int index) {
        try {
            return rs.wasNull() ? null : pm.getQueryCache().getIDNameList(field.getOwner().getName(), field.getName(), rs.getString(index));
        } catch (SQLException e) {
            throw new DataAccessException("Get decimal from ResultSet error", e);
        }
    }

    public void formatFieldValueOfRecord(EntityRecord record, String fieldName, Object valueObj) {
        if (valueObj == null) {
            record.setFieldValue(fieldName, null);
            record.setFieldLabel(fieldName, null);
        } else if (valueObj instanceof List) {
            List<IDName> list = (List)valueObj;
            record.setFieldValue(fieldName, list);
            record.setFieldLabel(fieldName, list.stream().map(IDName::getName).collect(Collectors.joining(",")));
        } else {
            throw new IllegalArgumentException("invalid data format: ["+valueObj+"]");
        }
    }

    public void setParamValue(PersistenceManager pm, Field field, ID objectId, Object value) {
        if (pm == null) {
            throw new NullPointerException("pm");
        } else if (field == null) {
            throw new NullPointerException("field");
        } else if (objectId == null) {
            throw new NullPointerException("objectId");
        } else {
            JdbcTemplate a = new JdbcTemplate(pm.getDataSource());
            a.execute((ConnectionCallback<Boolean>) conn -> {

                Statement statement = null;

                try {
                    try {
                        String sql = String.format("delete from t_reference_list_map where objectId = '%s' and fieldName = '%s'", objectId.getId(),field.getName());
                        statement= conn.createStatement();
                        statement.executeUpdate(sql);
                    } catch (SQLException e) {
                        throw new DataAccessException(e);
                    }
                } catch (Throwable e) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException ex) {
                            throw new DataAccessException(ex);
                        }

                    }

                    throw e;
                }

                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new DataAccessException(e);
                }

                if (value == null) {
                   pm.getQueryCache().reloadReferenceListCache(field.getOwner().getName(), field.getName(),objectId.toString());
                    return false;
                } else {
                    Stream.of((ID[]) value).forEach(id->{
                        EntityRecord entityRecord = pm.newRecord("ReferenceListMap");
                        entityRecord.setFieldValue("entityName", field.getOwner().getName());
                        entityRecord.setFieldValue("fieldName",field.getName());
                        entityRecord.setFieldValue("objectId", objectId);
                        entityRecord.setFieldValue("toId", id);
                       pm.insert(entityRecord);
                    });

                    pm.getQueryCache()
                            .reloadReferenceListCache(field.getOwner().getName(), field.getName(), objectId.toString());
                    return true;
                }
            });
        }
    }
}
