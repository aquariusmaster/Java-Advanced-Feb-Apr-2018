package com.flowergarden.dao.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

    private static File fileToDB;
    private static final String PATH_TO_DB = "flowergarden.db";

    public static Connection getConnection() {

        File fileToDB = new File(PATH_TO_DB);
        Connection conn = null;
        try(Connection connection = DriverManager.getConnection(
                            "jdbc:sqlite:"+fileToDB.getCanonicalFile().toURI())) {

            conn = connection;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }
}
