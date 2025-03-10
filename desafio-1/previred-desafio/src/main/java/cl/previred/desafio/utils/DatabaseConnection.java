package cl.previred.desafio.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections using HikariCP connection pool.
 * Loads database configuration from a properties file.
 */
public class DatabaseConnection {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String PROPERTIES_FILE = "/database.properties";
    private static HikariDataSource dataSource;

    /**
     * Static initializer block to load database properties and initialize the connection pool.
     */
    static {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                String errorMessage = "Properties file not found: " + PROPERTIES_FILE;
                logger.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }

            Properties properties = new Properties();
            properties.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName("org.postgresql.Driver");

            // Configure connection pool settings (adjust as needed)
            config.setMaximumPoolSize(10); // Maximum number of connections
            config.setMinimumIdle(5); // Minimum number of idle connections
            config.setMaxLifetime(1800000); // 30 minutes (milliseconds)
            config.setConnectionTimeout(30000); // 30 seconds (milliseconds)

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully.");

        } catch (Exception e) {
            String errorMessage = "Error initializing database connection pool";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Retrieves a database connection from the connection pool.
     *
     * @return A database connection.
     * @throws SQLException If an error occurs while getting a connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw e;
        }
    }
}