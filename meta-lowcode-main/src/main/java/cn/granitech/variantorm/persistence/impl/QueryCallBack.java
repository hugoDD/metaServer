package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class QueryCallBack implements PreparedStatementCallback<Integer> {
    public Integer doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
        boolean execute = preparedStatement.execute();


        if (execute) {


            ResultSet a = preparedStatement.getResultSet();

            while (a != null && a.next()) {
                if (!preparedStatement.getMoreResults()) {

                    DataQueryImpl.doCascadeQuery(this.G, a, this.L, DataQueryImpl.getPersistenceManager(this.G), this.F, DataQueryImpl.getQueryCache(this.G), this.D);
                } else {
                    if (this.pagination == null) {
                        break;
                    }


                    this.pagination.setTotal(a.getInt(1));
                }
            }


        }

        return null;
    }

    DataQueryImpl G;
    List L;
    QuerySchema F;
    List D;
    Pagination pagination;

    QueryCallBack(DataQueryImpl this$0, List var2, QuerySchema var3, List var4, Pagination var5) {
        this.G = this$0;
        this.L = var2;
        this.F = var3;
        this.D = var4;
        this.pagination = var5;
    }
}

