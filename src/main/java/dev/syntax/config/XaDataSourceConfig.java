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

@Configuration
@EnableTransactionManagement
public class XaDataSourceConfig {
    public static final String TRANSACTION_MANAGER_BEAN_NAME = "jtaTransactionManager";

    @Bean(name = "atomikosUserTransactionManager")
    public UserTransactionManager userTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(30);
        userTransactionManager.setForceShutdown(true);

        return userTransactionManager;
    }

    @Bean(name = "atomikosUserTransaction")
    public UserTransaction userTransaction() throws SystemException {
        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(30);

        return userTransaction;
    }

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