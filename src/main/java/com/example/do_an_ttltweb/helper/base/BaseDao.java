package com.example.do_an_ttltweb.helper.base;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public class BaseDao {
    private Jdbi jdbi;

    protected Jdbi getJdbi() {
        if (jdbi == null) {
            makeConnect();
        }
        return jdbi;
    }
    private void makeConnect() {
        MysqlDataSource ds = new MysqlDataSource();
        String url = "jdbc:mysql://" + DBProperties.host() + ":" + DBProperties.port() + "/" + DBProperties.dbname() + "?" + DBProperties.option();
        ds.setURL(url);
        ds.setUser(DBProperties.username());
        ds.setPassword(DBProperties.password());

        try {
            ds.setUseCompression(true);
            ds.setAutoReconnect(true);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        jdbi = Jdbi.create(ds);
    }

}
