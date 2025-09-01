package com.task.mci.dao.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DB {

    private static HikariDataSource dataSource;

    public static void init() {
        if (dataSource != null) {
            return; 
        }
        Properties properties = new Properties();
        try (InputStream input = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find db.properties. Please make sure it's in your classpath.");
                throw new RuntimeException("Can't find db.properties file.");
            }
            properties.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("pool.maximumPoolSize", "5")));
            config.setMinimumIdle(Integer.parseInt(properties.getProperty("pool.minimumIdle", "2")));
            config.setConnectionTimeout(Long.parseLong(properties.getProperty("pool.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(properties.getProperty("pool.idleTimeout", "600000")));
            config.setMaxLifetime(Long.parseLong(properties.getProperty("pool.maxLifetime", "1800000")));
            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            System.err.println("Error loading db.properties file: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric pool properties: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DatabaseConnectionManager is not initialized. Call init() first.");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
