package com.yab.multitenantERP.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@Configuration
@EnableJpaRepositories(
        basePackages = "com.yab.multitenantERP.repositories",
        entityManagerFactoryRef = "dynamicEntityManagerFactory",
        transactionManagerRef = "dynamicTransactionManager"
)
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource dataSource() {
        DynamicSchemaRoutingDataSource routingDataSource = new DynamicSchemaRoutingDataSource();

        // Default DataSource (public schema)
        HikariDataSource defaultDataSource = new HikariDataSource();
        defaultDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/erp");
        defaultDataSource.setDriverClassName("org.postgresql.Driver");
        defaultDataSource.setUsername("postgres");
        defaultDataSource.setPassword("root");

        // Schema-specific DataSources
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("company1", createDataSourceForSchema("company1"));
        targetDataSources.put("company2", createDataSourceForSchema("company2"));

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.afterPropertiesSet();  // Initialize the DataSource

        return routingDataSource;
    }

    private DataSource createDataSourceForSchema(String schema) {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/erp?currentSchema=" + schema)
                .username("postgres")
                .password("root")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "dynamicEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dynamicEntityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.yab.multitenantERP.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "dynamicTransactionManager")
    public PlatformTransactionManager dynamicTransactionManager(
            @Qualifier("dynamicEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}