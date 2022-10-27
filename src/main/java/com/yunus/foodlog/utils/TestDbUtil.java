package com.yunus.foodlog.utils;

import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDbUtil {

    private TestDbUtil() {}

    public static void resetAutoIncrementColumn(ApplicationContext applicationContext, String... tableNames) {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try(Connection dbConnection = dataSource.getConnection()) {
            for(String tableName : tableNames) {
                String resetSql = "ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1;";
                Statement statement = dbConnection.createStatement();
                statement.execute(resetSql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
