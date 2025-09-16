package dev.syntax.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DatabaseConfig.class)
@EnableJpaRepositories(
        basePackages = "dev.syntax.first.repository",
        entityManagerFactoryRef = FirstDatasourceConfig.ENTITY_MANAGER_BEAN_NAME,
        transactionManagerRef = XaDataSourceConfig.TRANSACTION_MANAGER_BEAN_NAME
)
public class FirstDatasourceConfig {

    public static final String ENTITY_MANAGER_BEAN_NAME = "firstEntityManger";
    private static final String DATASOURCE_BEAN_NAME = "firstDataSource";
    private static final String DATASOURCE_PROPERTIES_PREFIX = "spring.datasource.firstdb";
    private static final String HIBERNATE_PROPERTIES = "firstHibernateProperties";

    @Primary
    @Bean(name = ENTITY_MANAGER_BEAN_NAME)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(DATASOURCE_BEAN_NAME) DataSource dataSource,
            @Qualifier(HIBERNATE_PROPERTIES) DatabaseConfig.Hibernate hibernateProperties
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("dev.syntax.first.entity");
        em.setJpaPropertyMap(DatabaseConfig.Hibernate.propertiesToMap(hibernateProperties));
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = HIBERNATE_PROPERTIES)
    @ConfigurationProperties(DATASOURCE_PROPERTIES_PREFIX + ".hibernate")
    public DatabaseConfig.Hibernate hibernateProperties() {
        return new DatabaseConfig.Hibernate();
    }

    @Primary
    @Bean(name = DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = DATASOURCE_PROPERTIES_PREFIX)
    public DataSource dataSource() {
        return new AtomikosDataSourceBean();
    }
}