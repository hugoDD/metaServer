package cn.granitech.variantorm.util.sql;

import cn.granitech.variantorm.exception.DataAccessException;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;

public final class DataReader {
    private final Statement statement;
    private final ResultSet resultSet;

    public int getInt(int columnIndex) {
        try {
            return this.resultSet.getInt(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public float getFloat(int columnIndex) {
        try {
            return this.resultSet.getFloat(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public Timestamp getTimestamp(int columnIndex) {
        try {
            return this.resultSet.getTimestamp(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public InputStream getBinaryStream(int columnIndex) {
        try {
            return this.resultSet.getBinaryStream(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public DataReader(Statement statement, ResultSet resultSet) {
        if (statement == null) {
            throw new NullPointerException("statement");
        } else if (resultSet == null) {
            throw new NullPointerException("resultSet");
        } else {
            this.statement = statement;
            this.resultSet = resultSet;
        }
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public Array getArray(int columnIndex) {
        try {
            return this.resultSet.getArray(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public Reader getCharacterStream(int columnIndex) {
        try {
            return this.resultSet.getCharacterStream(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public boolean next() {
        try {
            return this.resultSet.next();
        } catch (SQLException var2) {
            throw new DataAccessException(var2);
        }
    }

    public short getShort(int columnIndex) {
        try {
            return this.resultSet.getShort(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) {
        try {
            return this.resultSet.getBigDecimal(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public void close() {
        try {
            this.statement.close();
        } catch (SQLException var2) {
            throw new DataAccessException("Close DataReader Error.", var2);
        }
    }

    public long getLong(int columnIndex) {
        try {
            return this.resultSet.getLong(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public ResultSetMetaData getMetaData() {
        try {
            return this.resultSet.getMetaData();
        } catch (SQLException var2) {
            throw new DataAccessException(var2);
        }
    }

    public Object getObject(int columnIndex) {
        try {
            return this.resultSet.getObject(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public int getColumnCount() {
        try {
            return this.getMetaData().getColumnCount();
        } catch (SQLException var2) {
            throw new DataAccessException(var2);
        }
    }

    public double getDouble(int columnIndex) {
        try {
            return this.resultSet.getDouble(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public byte getByte(int columnIndex) {
        try {
            return this.resultSet.getByte(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public boolean wasNull() {
        try {
            return this.resultSet.wasNull();
        } catch (SQLException var2) {
            throw new DataAccessException(var2);
        }
    }

    public Date getDate(int columnIndex) {
        try {
            return this.resultSet.getDate(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public boolean getBoolean(int columnIndex) {
        try {
            return this.resultSet.getBoolean(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public byte[] getBytes(int columnIndex) {
        try {
            return this.resultSet.getBytes(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public String getString(int columnIndex) {
        try {
            return this.resultSet.getString(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }

    public Time getTime(int columnIndex) {
        try {
            return this.resultSet.getTime(columnIndex);
        } catch (SQLException var3) {
            throw new DataAccessException(var3);
        }
    }
}
