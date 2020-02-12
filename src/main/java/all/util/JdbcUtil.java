package all.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @ClassName JdbcUtil
 * @Description TODO
 * @Author Liyihe
 * @Date 2019/08/25 上午1:20
 * @Version 1.0
 */
public class JdbcUtil {
    private static volatile HikariDataSource ds;
    private static Properties properties=new Properties();
    public static Connection getConnection() {
        Connection con = null;
        if (ds == null) {
            synchronized (HikariDataSource.class) {
                if (ds == null) {
                    InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("mysql-config.properties");
                    try {
                        properties.load(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    HikariConfig config = new HikariConfig();//192.168.0.35 localhost
                    config.setJdbcUrl(properties.getProperty("jdbc.url"));
                    config.setUsername(properties.getProperty("jdbc.username"));
                    config.setPassword(properties.getProperty("jdbc.password"));
                    config.setDriverClassName(properties.getProperty("jdbc.driver"));
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    ds = new HikariDataSource(config);
                }
            }
        }
        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void close_1(Connection con, PreparedStatement pre, ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pre != null) {
            try {
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close_2(Connection con, Statement sta, ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (sta != null) {
            try {
                sta.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
