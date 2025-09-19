package dev.syntax;


import dev.syntax.first.repository.FirstRepository;
import dev.syntax.second.repository.SecondRepository;
import dev.syntax.service.FirstService;
import dev.syntax.service.SecondService;
import dev.syntax.service.TransferService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TwoPhaseCommitApplicationTests {

    @Autowired
    private TransferService transferService;

    @Autowired
    private FirstService firstService;   // A DB
    @Autowired
    private SecondService secondService;  // B DB

    @Autowired
    @Qualifier("firstDataSource")
    DataSource firstDataSource;

    @Autowired
    @Qualifier("secondDataSource")
    DataSource secondDataSource;

    @Test
    @DisplayName("MySQL DBMS 연동 여부 테스트, JDBC 드라이버 MySQL 관련 드라이버인지?")
    void contextLoads() throws SQLException {
        // Hint. DataSource
        // -> DataSource를 통해 드라이버 이름을 확인할 수 있음
        // DataSource의 드라이버 이름이 내가 기대하는 이름과 같으면?

        // 실제 로딩된 드라이버 이름
        String loadedDriverName
                = firstDataSource.getConnection().getMetaData().getDriverName();
        System.out.println("loadedDriverName = " + loadedDriverName);

        // 내가 기대하는 드라이버 이름
        String expectedDriverName = "MySQL Connector/J";

        assertThat(loadedDriverName).isEqualTo(expectedDriverName);
    }


    @Test
    @DisplayName("2PC 커밋 테스트 - 출력 확인")
    void testTwoPhaseCommitWithPrint() {
        BigDecimal amount = BigDecimal.valueOf(100);

        var fromAccountB = firstService.findByAccountNumber("1001");
        var toAccountB = secondService.findByAccountNumber("2001");

        System.out.println("[Test] 커밋 전 출금 계좌 잔액: " + fromAccountB.getBalance());
        System.out.println("[Test] 커밋 전 입금 계좌 잔액: " + toAccountB.getBalance());


        try {
            transferService.transfer("1001", "2001", amount);
        } catch (RuntimeException e) {
            System.out.println("[Test] 예외 발생: " + e.getMessage());
        }

        // 커밋 후 계좌 잔액 확인
        var fromAccountA = firstService.findByAccountNumber("1001");
        var toAccountA = secondService.findByAccountNumber("2001");

        System.out.println("[Test] 커밋 후 출금 계좌 잔액: " + fromAccountA.getBalance());
        System.out.println("[Test] 커밋 후 입금 계좌 잔액: " + toAccountA.getBalance());
    }
    @Test
    @DisplayName("2PC 롤백 테스트 - 출력 확인")
    void testTwoPhaseRollbackWithPrint() {
        BigDecimal amount = BigDecimal.valueOf(100);

        var fromAccountB = firstService.findByAccountNumber("1001");
        var toAccountB = secondService.findByAccountNumber("2001");

        System.out.println("[Test] 롤백 전 출금 계좌 잔액: " + fromAccountB.getBalance());
        System.out.println("[Test] 롤백 전 입금 계좌 잔액: " + toAccountB.getBalance());


        try {
            // 유효하지 않은 계좌번호 전달
            // 서비스 로직에서 오류가 발생했기 때문에
            // prepare 단계 전에 오류가 발생해서
            // 로그에 prepare 전에 end가 출력됨
            transferService.transfer("1001", "3001", amount);
        } catch (RuntimeException e) {
            System.out.println("[Test] 예외 발생: " + e.getMessage());
        }

        // 롤백 후 계좌 잔액 확인
        var fromAccountA = firstService.findByAccountNumber("1001");
        var toAccountA = secondService.findByAccountNumber("2001");

        System.out.println("[Test] 롤백 후 출금 계좌 잔액: " + fromAccountA.getBalance());
        System.out.println("[Test] 롤백 후 입금 계좌 잔액: " + toAccountA.getBalance());
    }

}
