package dev.syntax.config;


import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

// atomikos 기반 jta 트랜잭션 매니저 설정 클래스
@Configuration
@EnableTransactionManagement // @Trasactional 어노테이션 기반 트랜잭션 활성화
public class XaDataSourceConfig {
    // 트랜잭션 매니저 빈 이름
    public static final String TRANSACTION_MANAGER_BEAN_NAME = "jtaTransactionManager";

    // 분산 트랜잭션을 실제로 관리하는 매니저 객체
    @Bean(name = "atomikosUserTransactionManager")
    public UserTransactionManager userTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(30);
        userTransactionManager.setForceShutdown(true);

        return userTransactionManager;
    }

    // 트랜잭션 경계를 관리
    @Bean(name = "atomikosUserTransaction")
    public UserTransaction userTransaction() throws SystemException {
        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(30);

        return userTransaction;
    }

    // spring이 사용할 jta 트랜잭션 매니저 bean 등록
    @Primary
    @Bean(name = TRANSACTION_MANAGER_BEAN_NAME)
    public JtaTransactionManager jtaTransactionManager(
            @Qualifier("atomikosUserTransactionManager") UserTransactionManager userTransactionManager,
            @Qualifier("atomikosUserTransaction") UserTransaction userTransaction
    ) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager);
        jtaTransactionManager.setUserTransaction(userTransaction);

        return jtaTransactionManager;
    }
}