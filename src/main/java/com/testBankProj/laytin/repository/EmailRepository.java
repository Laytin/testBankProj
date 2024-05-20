package com.testBankProj.laytin.repository;

import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailRepository extends JpaRepository<Email,Integer> {
    Optional<Email> findByEmail(String email);
}
