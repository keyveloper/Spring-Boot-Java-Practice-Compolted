package com.example.webserver;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

@Getter
@Repository
@Slf4j
public class DataBaseModel {
    private HikariDataSource dataSource;

    @PostConstruct
    public void startConnection() {
        log.info("Starting database connection with HikariCP");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mariadb://localhost:3306/http");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("3353");
        hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setConnectionTimeout(20000);
        hikariConfig.setIdleTimeout(300000);
        hikariConfig.setMaxLifetime(600000);

        this.dataSource = new HikariDataSource(hikariConfig);

        try (Connection connection = dataSource.getConnection()) {
            log.info("Database connection established successfully");
        } catch (SQLException e) {
            log.error("Failed to establish database connection: " + e.getMessage());
        }
    }
    public Optional<String> putText(String key, String value) {
        String sql = "INSERT INTO texts (text_id, text_value) VALUES ?, ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, value);

            int affectedRows = preparedStatement.executeUpdate();

            // Check if any rows were affected.
            if (affectedRows > 0) {
                log.info("A new row has been inserted successfully.");
                return Optional.of("insertion successful");  // Return true if the insertion was successful.
            } else {
                log.error("A new row insertion failed");
                return Optional.empty();  // Return false if the insertion failed.
            }
        } catch (SQLException e) {
            log.error("DataBase Manger: putText err: " + e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<String> getText(String key) {
        String sql = "SELECT text_value FROM texts WHERE text_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()){
            preparedStatement.setString(1, key);

            if (resultSet.next()) {
                return Optional.of(resultSet.getString(1));
            }
        } catch (SQLException e) {
            log.error("DataBase Manger: getText err: " + e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<HashMap<String, String>> getTextAll() {
        // return Json
        String sql = "SELECT * FROM texts";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()){

            HashMap<String, String > results = new HashMap<>();
            while (resultSet.next()) {
                String key = resultSet.getString(1);
                String value = resultSet.getString(2);
                results.put(key, value);
            }

            if (results.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(results);
            }
        } catch (SQLException e) {
            log.error("Database Manager: getTextAll error: " + e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<String> deleteText(String key) {
        String sql = "DELETE FROM texts WHERE text_id = (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, key);

            int effectedRows = preparedStatement.executeUpdate();
            if (effectedRows > 0) {
                log.info("key: " + key + " row was deleted!");
                return Optional.of("key: " + key + " row was deleted!");
            } else {
                log.info("delete failed");
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("DataBase Manger: deleteText err: " + e.getMessage());
            return Optional.empty();
        }
    }
}
