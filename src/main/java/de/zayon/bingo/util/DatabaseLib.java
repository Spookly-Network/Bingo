package de.zayon.bingo.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.zayon.bingo.Bingo;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;


public class DatabaseLib implements Closeable {
    private final Bingo bingo;

    private final HikariDataSource hikariDataSource;

    public HikariDataSource getHikariDataSource() {
        return this.hikariDataSource;
    }

    public DatabaseLib(Bingo bingo) {
        this.bingo = bingo;
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl((String) bingo.getGeneralConfig().getOrSetDefault("config.database.url", "jdbc:mysql://localhost:3306/database"));
        hikariConfig.setUsername((String) bingo.getGeneralConfig().getOrSetDefault("config.database.username", "username"));
        hikariConfig.setPassword((String) bingo.getGeneralConfig().getOrSetDefault("config.database.password", "password"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        bingo.getGeneralConfig().save();
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void execute(String query, Object... args) {
        try (Connection connection = this.hikariDataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                addArguments(preparedStatement, args);
                preparedStatement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void executeUpdate(String query, Consumer<Integer> callback, Object... args) {
        try (Connection connection = this.hikariDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            addArguments(preparedStatement, args);
            callback.accept(Integer.valueOf(preparedStatement.executeUpdate()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void executeQuery(String query, Consumer<ResultSet> callback, Object... args) {
        try (Connection connection = this.hikariDataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                addArguments(preparedStatement, args);
                callback.accept(preparedStatement.executeQuery());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void executeAsync(String query, Object... args) {
        (new Thread(() -> {
            try (Connection connection = this.hikariDataSource.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    addArguments(preparedStatement, args);
                    preparedStatement.execute();
                    this.hikariDataSource.getConnection().close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        })).start();
    }

    public void executeUpdateAsync(String query, Consumer<Integer> callback, Object... args) {
        (new Thread(() -> {
            try (Connection connection = this.hikariDataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                addArguments(preparedStatement, args);
                callback.accept(Integer.valueOf(preparedStatement.executeUpdate()));
                this.hikariDataSource.getConnection().close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        })).start();
    }

    public void executeQueryAsync(String query, Consumer<ResultSet> callback, Object... args) {
        (new Thread(() -> {
            try (Connection connection = this.hikariDataSource.getConnection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    addArguments(preparedStatement, args);
                    callback.accept(preparedStatement.executeQuery());
                    this.hikariDataSource.getConnection().close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        })).start();
    }

    public Object get(String query, String arg, String selection) {
        try (Connection connection = this.hikariDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            addArguments(preparedStatement, new Object[]{arg});
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getObject(selection);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void addArguments(PreparedStatement preparedStatement, Object... args) {
        try {
            int position = 1;
            for (Object arg : args) {
                preparedStatement.setObject(position, arg);
                position++;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            this.hikariDataSource.getConnection().close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
