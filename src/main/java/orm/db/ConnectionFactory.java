package orm.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by abhishek on 21/06/19.
 */
public class ConnectionFactory {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return conn;
    }
}
