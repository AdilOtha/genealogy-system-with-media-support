import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Defines a static method to receive the Connection object for JDBC Connection.
 * All methods that interact with the database will call this method for initiating the connection.
 */
public class DBConnection {
    /**
     * static method to receive the Connection object for JDBC Connection.
     * @return JDBC Connection object
     * @throws SQLException if error while creating Connection with database
     */
    public static Connection getConnection() throws SQLException {
        // check if MySQL driver is assigned for the project
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // the JDBC database URL along with the name of database to use
        final String URL = "jdbc:mysql://localhost:3306/3901_course_project_b00900955";
        // the username for access to the database
        final String USERNAME = "root";
        // the password for access to the database
        final String PASSWORD = "root";
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);

    }
}
