package com.symphodia.eng.repository;

import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class DatabaseConfigurationTest {

    @Bean
    @Profile("test")
    public DataSource localDataSource() {
        try {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setUrl("jdbc:h2:mem:AZ;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            ds.setUsername("sa");
            ds.setPassword("sa");
            return ds;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR,"Error during data source init", e);
        }
    }

}