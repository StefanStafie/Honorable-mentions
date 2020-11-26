package ro.uaic.info.myapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database{
    private static Database single_instance = null;
    public java.sql.Statement stmt;
    public java.sql.Connection conn;
    /**connects to the database*/
    private Database(){
        try {
            this.conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "STUDENT", "STUDENT");
            this.stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (single_instance == null)
            single_instance = new Database();
        return single_instance;
    }

    public Statement getStmt() {
        return stmt;
    }

    public Connection getConn() {
        return conn;
    }
}
