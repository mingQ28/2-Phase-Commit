package dev.syntax.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final FirstService firstService;
    private final SecondService secondService;

    public TransferService(FirstService firstService, SecondService secondService) {
        this.firstService = firstService;
        this.secondService = secondService;
    }



    // Transacational을 통해 firstservice(참여자), secondservice(참여자)을
    // 하나의 글로벌 트랜잭션으로 묶음
    @Transactional
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        System.out.println("[TransferService] 송금 시작: " + fromAccount + " -> " + toAccount + " 금액: " + amount);

        firstService.withdraw(fromAccount, amount);
        secondService.deposit(toAccount, amount);

        System.out.println("[TransferService] 송금 완료");
    }
}
