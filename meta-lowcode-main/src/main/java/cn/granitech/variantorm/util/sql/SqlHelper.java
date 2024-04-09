package cn.granitech.variantorm.util.sql;


import cn.granitech.variantorm.exception.SqlInjectionException;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {
    private static final String[] sqlKey;

    public SqlHelper() {
    }

    static {
         sqlKey = new String[26];
        sqlKey[0] = "SELECT ";
        sqlKey[1] = "INSERT ";
        sqlKey[2] = "DELETE ";
        sqlKey[3] = "COUNT(";
        sqlKey[4] = "DROP TABLE";
        sqlKey[5] = "UPDATE ";
        sqlKey[6] = "TRUNCATE ";
        sqlKey[7] = "ASC(";
        sqlKey[8] = "MID(";
        sqlKey[9] = "CHAR(";
        sqlKey[10] = "XP_CMDSHELL";
        sqlKey[11] = "EXEC ";
        sqlKey[12] = "MASTER ";
        sqlKey[13] = "NET ";
        sqlKey[14] = "WHERE  ";
        sqlKey[15] = "FROM ";
        sqlKey[16] = "JOIN ";
        sqlKey[17] = "ROOT";
        sqlKey[18] = "ASCII";
        sqlKey[19] = "CR ";
        sqlKey[20] = "LF ";
        sqlKey[21] = "EVAL(";
        sqlKey[22] = "OPEN(";
        sqlKey[23] = "SYSOPEN(";
        sqlKey[24] = "SYSTEM(";
        sqlKey[25] = "MAILTO:";
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }

    public static boolean checkSqlInjection(String... sqlParams) {

        for (String sqlParam : sqlParams) {
            if (StringUtils.isNotEmpty(sqlParam)) {
                for (String key : sqlKey) {
                    if (sqlParam.toUpperCase().contains(key)) {
                        throw new SqlInjectionException("发现潜在SQL注入攻击风险!");
                    }

                }
            }

        }

        return true;
    }
}
