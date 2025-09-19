package dev.syntax.service;

import dev.syntax.second.entity.Account;
import dev.syntax.second.repository.SecondRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SecondService {
    private final SecondRepository secondRepository;

    public SecondService(SecondRepository secondRepository) {
        this.secondRepository = secondRepository;
    }

    public Account findByAccountNumber(String accountNumber) {
        return secondRepository.findByAccountNumber(accountNumber);
    }

    // 입금 서비스 관련 서비스 로직 - seconddb와 연동
    public void deposit(String toAccountNumber, BigDecimal amount) {
        var account = secondRepository.findByAccountNumber(toAccountNumber);

        if (account == null) {
            throw new IllegalArgumentException("해당 계좌를 찾을 수 없습니다: " + toAccountNumber);
        }

        BigDecimal currentBalance = account.getBalance();
        System.out.println("[SecondService] 입금 전 계좌 " + toAccountNumber + " 잔액: " + currentBalance);

        account.setBalance(currentBalance.add(amount));
        secondRepository.save(account);

        System.out.println("[SecondService] 입금 후 계좌 " + toAccountNumber + " 잔액: " + account.getBalance());
    }
}
