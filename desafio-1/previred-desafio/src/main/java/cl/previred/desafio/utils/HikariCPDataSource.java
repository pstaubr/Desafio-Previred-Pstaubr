package cl.previred.desafio.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using HikariCP connection pool.
 * Provides a static method to obtain database connections.
 */
public class HikariCPDataSource {

    private static final Logger logger = LoggerFactory.getLogger(HikariCPDataSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            logger.info("Initializing HikariCP configuration...");
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
            config.setUsername("postgres");
            config.setPassword("1234");
            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(20000);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            ds = new HikariDataSource(config);
            logger.info("HikariCP configuration initialized successfully.");
        } catch (Exception e) {
            String errorMessage = "Error initializing HikariCPDataSource";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private HikariCPDataSource() {
    }

    /**
     * Retrieves a database connection from the HikariCP connection pool.
     *
     * @return A database connection.
     * @throws SQLException If an error occurs while getting a connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw e;
        }
    }
}