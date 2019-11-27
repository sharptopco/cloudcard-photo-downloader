package com.cloudcard.photoDownloader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import java.util.Properties;

public abstract class DatabaseStorageService implements StorageService {

    protected DriverManagerDataSource dataSource;
    protected DriverManagerDataSource customerDataSource;
    @Value("${db.datasource.driverClassName}")
    String driverClassName;
    @Value("${db.datasource.url}")
    String url;
    @Value("${db.transact.customerdb.url}")
    String customerUrl;
    @Value("${db.datasource.username}")
    String username;
    @Value("${db.datasource.password}")
    String password;
    @Value("${db.datasource.schema:}")
    String schemaName;

    @PostConstruct
    public void init() {

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        Properties connectionProperties = dataSource.getConnectionProperties();
        if (connectionProperties == null)
            connectionProperties = new Properties();
        connectionProperties.setProperty("oracle.jdbc.timezoneAsRegion", "false");
        if (!schemaName.isEmpty()) dataSource.setSchema(schemaName);
        if (!customerUrl.isEmpty()) {
            customerDataSource = new DriverManagerDataSource();
            customerDataSource.setDriverClassName(driverClassName);
            customerDataSource.setUrl(customerUrl);
            customerDataSource.setUsername(username);
            customerDataSource.setPassword(password);
            connectionProperties = customerDataSource.getConnectionProperties();
            if (connectionProperties == null)
                connectionProperties = new Properties();
            connectionProperties.setProperty("oracle.jdbc.timezoneAsRegion", "false");
            if (!schemaName.isEmpty()) customerDataSource.setSchema(schemaName);
        }
    }
}
