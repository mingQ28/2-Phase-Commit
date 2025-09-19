package dev.syntax.service;

import dev.syntax.first.entity.Account;
import dev.syntax.first.repository.FirstRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FirstService {
    private final FirstRepository firstRepository;

    public FirstService(FirstRepository firstRepository) {
        this.firstRepository = firstRepository;
    }

    public Account findByAccountNumber(String accountNumber) {
        return firstRepository.findByAccountNumber(accountNumber);
    }

    // 출금서비스 관련 서비스 로직 - firstdb와 연동
    public void withdraw(String fromAccountNumber, BigDecimal amount) {
        var account = firstRepository.findByAccountNumber(fromAccountNumber);

        if (account == null) {
            throw new IllegalArgumentException("해당 계좌를 찾을 수 없습니다: " + fromAccountNumber);
        }

        BigDecimal currentBalance = account.getBalance();
        System.out.println("[FirstService] 출금 전 계좌 " + fromAccountNumber + " 잔액: " + currentBalance);

        if (currentBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다. 현재 잔액: " + currentBalance);
        }

        account.setBalance(currentBalance.subtract(amount));
        firstRepository.save(account);

        System.out.println("[FirstService] 출금 후 계좌 " + fromAccountNumber + " 잔액: " + account.getBalance());
    }

}
