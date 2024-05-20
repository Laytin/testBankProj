package com.testBankProj.laytin.repository;

import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByUsername(String username);
    List<Customer> findCustomersByFullnameLike(String fullname, PageRequest pg);
    List<Customer> findCustomersByDateOfBirthIsGreaterThan(Timestamp date, PageRequest pg);
}
