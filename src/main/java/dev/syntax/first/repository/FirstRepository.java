package dev.syntax.first.repository;

import dev.syntax.first.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirstRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String fromAccountNumber);
}