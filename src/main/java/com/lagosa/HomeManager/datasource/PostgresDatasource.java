package com.lagosa.HomeManager.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresDatasource {

//    @Bean
//    @ConfigurationProperties("app.datasource")
//    public HikariDataSource hikariDataSource() {
//        return DataSourceBuilder
//                .create()
//                .type(HikariDataSource.class)
//                .driverClassName(dataSourceProperties.getDriverClassName())
//                .build();
//    }
//    @Bean
//    @Primary
//    @ConfigurationProperties("app.datasource")
//    public DataSourceProperties dataSourceProperties() {
//        return new DataSourceProperties();
//    }

        @Bean
        @ConfigurationProperties("app.datasource")
        public HikariDataSource dataSource(DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                    .build();
        }
}
