package cn.granitech.variantorm.metadata;


import cn.granitech.variantorm.serializer.IDSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.UUID;

@JsonSerialize(
        using = IDSerializer.class
)
public final class ID implements Serializable, Comparable<ID> {
    private static final String[] bitStr;
    private int entityCode;
    private String id;

    public String toString() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof String) {
            return this.id.equals(obj);
        } else {
            return obj instanceof ID && ((ID) obj).id.equals(this.id);
        }
    }

    public static ID newID(int typeCode) {
        if (typeCode >= 0 && typeCode <= 99999) {
            String str = String.valueOf(typeCode);
            return new ID( bitStr[str.length()]+str+"-"+getNewUUIDStr());
        } else {
            throw new IllegalArgumentException("typeCode="+typeCode);
        }
    }

    public ID() {
    }

    public static boolean isId(Object id) {
        if (id == null) {
            return false;
        } else if (id instanceof ID) {
            return true;
        } else {
            try {
                new ID(id.toString());
                return true;
            } catch (IllegalArgumentException var2) {
                return false;
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public static String getNewUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }



    public int compareTo(ID id) {
        return this.id.compareTo(id.id);
    }

    public static ID valueOf(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("ID could't be null");
        } else {
            return id instanceof ID ? (ID)id : new ID(id.toString());
        }
    }

    public ID(String id) {
        if (id == null) {
            throw new NullPointerException("ID could't be null");
        } else if (id.getBytes().length != 40) {
            throw new IllegalArgumentException("Invalid length of ID, [id: "+id+", length: "+id.length()+"]");
        } else {
            int a = id.indexOf(45);
            this.entityCode = Integer.parseInt(id.substring(0, a));
            this.id = id;
        }
    }

    static {
        bitStr = new String[8];
        bitStr[0] = null;
        bitStr[1] = "000000";
        bitStr[2] = "00000";
        bitStr[3] = "0000";
        bitStr[4] = "000";
        bitStr[5] = "00";
        bitStr[6] = "0";
        bitStr[7] = "";
    }

    public int getEntityCode() {
        return this.entityCode;
    }
}

