package pkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final String URL = "jdbc:mysql://localhost:3306/3901_course_project_testing";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);

    }
}
