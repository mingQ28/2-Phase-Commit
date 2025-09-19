package dev.syntax.first.repository;

import dev.syntax.first.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirstRepository extends JpaRepository<Account, Long> {

    // 쿼리 메서드
    // 계좌번호에 맞는 계정정보 찾기
    Account findByAccountNumber(String fromAccountNumber);
}