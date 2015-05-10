package net.alextwelshie.minedrop.ranks;



import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariGFXDPool
{

    private static HikariGFXDPool instance = null;
    private HikariDataSource ds = null;


    static
    {
        try
        {
            instance = new HikariGFXDPool();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private HikariGFXDPool()
    {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(5);
        config.setMaxLifetime(259170000);
        config.setIdleTimeout(259170000);
        config.setAutoCommit(true);

        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        config.addDataSourceProperty("portNumber", 3306);
        config.addDataSourceProperty("serverName", "localhost");
        config.addDataSourceProperty("databaseName", "survivalmc");
        config.addDataSourceProperty("user", "db_user19910526");
        config.addDataSourceProperty("password", "44ap9cDj@LR6?QEM74HWYUt@T");

        ds = new HikariDataSource(config);
    }

    public static HikariGFXDPool getInstance ()
    {
        return instance;
    }

    public Connection getConnection()  throws SQLException
    {
        return ds.getConnection();
    }

}
