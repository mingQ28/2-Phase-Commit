package dev.syntax.config;


import lombok.Data;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DatabaseConfig {

    private FirstDb firstDb;
    private SecondDb secondDb;

    @Data
    public static class FirstDb {
        private XaProperties xaProperties;
        private String xaDataSourceClassName;
        private String uniqueResourceName;
        private int maxPoolSize;
        private Hibernate hibernate;
    }

    @Data
    public static class SecondDb {
        private XaProperties xaProperties;
        private String xaDataSourceClassName;
        private String uniqueResourceName;
        private int maxPoolSize;
        private Hibernate hibernate;
    }

    @Data
    public static class XaProperties {
        private String url;
        private String user;
        private String password;
    }

    @Data
    public static class Hibernate {
        private String ddlAuto;
        private String dialect;
        private Naming naming;

        public static Map<String, Object> propertiesToMap(Hibernate hibernateProperties) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
            properties.put("javax.persistence.transactionType", "JTA");

            if(hibernateProperties.getDdlAuto() != null) {
                properties.put("hibernate.hbm2ddl.auto", hibernateProperties.getDdlAuto());
            }
            if(hibernateProperties.getDialect() != null) {
                properties.put("hibernate.dialect", hibernateProperties.getDialect());
            }

            DatabaseConfig.Naming hibernateNaming = hibernateProperties.getNaming();
            if(hibernateNaming != null) {
                if (hibernateNaming.getImplicitStrategy() != null) {
                    properties.put("hibernate.implicit_naming_strategy",  hibernateNaming.getImplicitStrategy());
                }
                if (hibernateNaming.getPhysicalStrategy() != null) {
                    properties.put("hibernate.physical_naming_strategy", hibernateNaming.getPhysicalStrategy());
                }
            }

            return properties;
        }
    }

    @Data
    public static class Naming {
        private String implicitStrategy;
        private String physicalStrategy;
    }

}