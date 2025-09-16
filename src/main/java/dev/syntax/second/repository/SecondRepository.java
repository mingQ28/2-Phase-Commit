package dev.syntax.second.repository;


import dev.syntax.second.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String toAccountNumber);
}
