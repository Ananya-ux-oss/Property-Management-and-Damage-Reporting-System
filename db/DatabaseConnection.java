package db;

import java.sql.*;

public class DatabaseConnection {

    private static final String DB_NAME = "property_management_db";

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME
        + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static DatabaseConnection instance;
    private Connection connection;
    private boolean    available = false;  

    private DatabaseConnection() {
        openConnection();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
  
    public boolean isAvailable() {
        return available;
    }

    private void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
            try (Connection serverConn = DriverManager.getConnection(SERVER_URL, DB_USER, DB_PASS);
                 Statement   stmt       = serverConn.createStatement()) {
                stmt.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME
                           + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                System.out.println("[DatabaseConnection] Database '" + DB_NAME + "' is ready.");
            }
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            available  = true;
            System.out.println("[DatabaseConnection] Connected to " + DB_URL);
            createSchema();

        } catch (ClassNotFoundException e) {
            System.out.println("Make sure the '.jar' file inside the 'lib/' folder is linked when compiling and running.");
            throw new RuntimeException("Database Driver missing. App cannot run.");
        } catch (SQLException e) {
            System.out.println("[DatabaseConnection] Could not connect to MySQL: " + e.getMessage());
            throw new RuntimeException("Database connection failed. App cannot run.");
        }
    }

    private void createSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id       VARCHAR(20)  PRIMARY KEY,
                    name          VARCHAR(100) NOT NULL,
                    email         VARCHAR(150) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    role          VARCHAR(20)  NOT NULL,
                    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT chk_users_role CHECK (role IN ('Student', 'Manager', 'Admin'))
                ) ENGINE=InnoDB
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS properties (
                    property_id   VARCHAR(20)  PRIMARY KEY,
                    property_name VARCHAR(150) NOT NULL,
                    type          VARCHAR(50),
                    location      VARCHAR(255) DEFAULT 'Unassigned',
                    manager_id    VARCHAR(20),
                    active        BOOLEAN      DEFAULT TRUE,
                    registered_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT fk_properties_manager FOREIGN KEY (manager_id) REFERENCES users(user_id)
                ) ENGINE=InnoDB
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS damage_reports (
                    report_id    VARCHAR(30)  PRIMARY KEY,
                    student_id   VARCHAR(20)  NOT NULL,
                    property_id  VARCHAR(20)  NOT NULL,
                    description  TEXT,
                    status       VARCHAR(20)  DEFAULT 'PENDING',
                    review_notes TEXT,
                    reported_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT fk_reports_student  FOREIGN KEY (student_id)  REFERENCES users(user_id),
                    CONSTRAINT fk_reports_property FOREIGN KEY (property_id) REFERENCES properties(property_id),
                    CONSTRAINT chk_reports_status  CHECK (status IN ('PENDING', 'REVIEWED', 'RESOLVED'))
                ) ENGINE=InnoDB
                """);

            System.out.println("[DatabaseConnection] Schema verified/created (users, properties, damage_reports).");
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DatabaseConnection] Connection closed.");
            } catch (SQLException e) {
                System.out.println("[DatabaseConnection] Error closing connection: " + e.getMessage());
            }
        }
    }
}
