package com.symphodia.eng.repository;

import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


@Configuration
@Profile({"dev", "sit", "prod"})
public class DatabaseConfiguration {

    @Bean
    @Profile({"sit", "prod"})
    public DataSource jndiDataSource() {
        try {
            Context ctx = new InitialContext();
            return (DataSource) ctx.lookup("java:/comp/env/jdbc/engDatabaseLocal");
        } catch (NamingException e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR,"Error during data source init", e);
        }
    }

    @Bean
    @Profile("dev")
    public DataSource localDataSource() {
        try {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("oracle.jdbc.OracleDriver");
            ds.setUrl("jdbc:oracle:thin:@//localhost:1521/xe");
            ds.setUsername("eng_test");
            ds.setPassword("test");
            return ds;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR,"Error during data source init", e);
        }
    }

}
