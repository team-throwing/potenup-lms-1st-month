package com.lms.repository.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * <h2>작성 예시(/src/main/resources/application.properties)</h2>
 * <pre>
 *      db.driver-class-name=com.mysql.cj.jdbc.Driver
 *      db.url=jdbc:mysql://localhost:3306/{데이터베이스 이름}
 *      db.username={사용자 이름}
 *      db.password={비밀번호}
 *
 *      hikari.maximumPoolSize=10
 *      hikari.minimumIdle=5
 *      hikari.connectionTimeout=2000
 *      hikari.idleTimeout=30000
 *      hikari.maxLifetime=1800000
 * </pre>
 */
public class DataSourceFactory {

    private static DataSource dataSource;

    static {
        initHikari();
    }

    private static void initHikari() {
        try {
            Properties props = new Properties();
            props.load(DataSourceFactory.class.getClassLoader().getResourceAsStream("application.properties"));

            HikariConfig config = new HikariConfig();

            // DB 연결정보 명시
            // Essentials(https://github.com/brettwooldridge/HikariCP?tab=readme-ov-file#gear-configuration-knobs-baby)
            config.setDriverClassName(props.getProperty("db.driver-class-name"));
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));

            // hikari 커넥션 풀 설정
            config.setMaximumPoolSize(
                    Integer.parseInt(props.getProperty("hikari.maximumPoolSize"))
            );
            config.setMinimumIdle(
                    Integer.parseInt(props.getProperty("hikari.minimumIdle"))
            );
            config.setIdleTimeout(
                    Integer.parseInt(props.getProperty("hikari.connectionTimeout"))
            );
            config.setIdleTimeout(
                    Integer.parseInt(props.getProperty("hikari.idleTimeout"))
            );
            config.setMaxLifetime(
                    Integer.parseInt(props.getProperty("hikari.maxLifetime"))
            );
            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            System.err.println("DataSourceFactory 의 DataSource 구성 중 예외 발생");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static DataSource get() {
        return dataSource;
    }
}
