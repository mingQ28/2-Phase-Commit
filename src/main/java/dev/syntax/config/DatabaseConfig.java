package dev.syntax.config;


import lombok.Data;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

// 분산 트랜잭션을 위한 설정 클래스
@Data
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DatabaseConfig {

    private FirstDb firstDb;
    private SecondDb secondDb;

    // firstdb
    @Data
    public static class FirstDb { // 데이터베이스 연동 설정
        private XaProperties xaProperties; // XA 연결정보(url, user, password)
        private String xaDataSourceClassName; // XA 데이터소스 구현체 클래스명
        private String uniqueResourceName; // Atomikos에서 사용하는 리소스 식별자
        private int maxPoolSize; // 커넥션 풀 최대 사이즈
        private Hibernate hibernate; // hibernate 관련 설정
    }

    // seocnddb
    @Data
    public static class SecondDb { // 데이터베이스 연동 설정
        private XaProperties xaProperties; // XA 연결정보(url, user, password)
        private String xaDataSourceClassName; // XA 데이터소스 구현체 클래스명
        private String uniqueResourceName; // Atomikos에서 사용하는 리소스 식별자
        private int maxPoolSize; // 커넥션 풀 최대 사이즈
        private Hibernate hibernate; // hibernate 관련 설정
    }

    @Data
    public static class XaProperties {
        private String url; // db 접속 url
        private String user; // db 사용자명
        private String password; // db 비밀번호
    }

    @Data
    public static class Hibernate {
        private String ddlAuto;
        private String dialect;
        private Naming naming;

        public static Map<String, Object> propertiesToMap(Hibernate hibernateProperties) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
            // jta 플랫폼으로 atomikos 지정
            properties.put("javax.persistence.transactionType", "JTA");
            // 트랜잭션 타입을 jta로 지정

            if(hibernateProperties.getDdlAuto() != null) {
                properties.put("hibernate.hbm2ddl.auto", hibernateProperties.getDdlAuto());
            }
            if(hibernateProperties.getDialect() != null) {
                properties.put("hibernate.dialect", hibernateProperties.getDialect());
            }

            // 네이밍 관련 설정(선택)
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
        private String implicitStrategy; // ex) org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
        private String physicalStrategy; // ex) org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
    }

}