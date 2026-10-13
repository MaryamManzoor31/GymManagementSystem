package database;

// Module M5: JDBC configuration for MySQL database access.
// Update the username and password to match your local MySQL setup.
public final class DatabaseConfig {

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String HOST = "localhost";
    public static final String PORT = "3306";
    public static final String DATABASE = "gym_management";
    public static final String USER = System.getProperty(
            "gym.db.user", environmentValue("GYM_DB_USER", "root"));
    public static final String PASSWORD = System.getProperty(
            "gym.db.password", environmentValue("GYM_DB_PASSWORD", "root"));
    public static final String SERVER_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private DatabaseConfig() {
        // Utility class
    }

    private static String environmentValue(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }
}
