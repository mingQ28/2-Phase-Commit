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

//    @Transactional
//    public void transfer(String fromAccount, String toAccount, BigDecimal amount, boolean simulateError) {
//        System.out.println("[TransferService] 송금 시작: " + fromAccount + " -> " + toAccount + " 금액: " + amount);
//
//        firstService.withdraw(fromAccount, amount);
//        secondService.deposit(toAccount, amount);
//
//        if (simulateError) {
//            System.out.println("[TransferService] 의도적 오류 발생, 트랜잭션 롤백");
//            throw new RuntimeException("트랜잭션 실패 시뮬레이션");
//        }
//
//        System.out.println("[TransferService] 송금 완료");
//    }
    @Transactional
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        System.out.println("[TransferService] 송금 시작: " + fromAccount + " -> " + toAccount + " 금액: " + amount);

        firstService.withdraw(fromAccount, amount);
        secondService.deposit(toAccount, amount);

//        if (simulateError) {
//            System.out.println("[TransferService] 의도적 오류 발생, 트랜잭션 롤백");
//            throw new RuntimeException("트랜잭션 실패 시뮬레이션");
//        }

        System.out.println("[TransferService] 송금 완료");
    }
}
