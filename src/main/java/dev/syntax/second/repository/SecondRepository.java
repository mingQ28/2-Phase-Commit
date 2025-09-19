package dev.syntax.second.repository;


import dev.syntax.second.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondRepository extends JpaRepository<Account, Long> {
    // 쿼리메서드
    // 계좌번호에 맞는 계좌정보 찾기
    Account findByAccountNumber(String toAccountNumber);
}
