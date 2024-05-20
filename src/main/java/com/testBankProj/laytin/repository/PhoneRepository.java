package com.testBankProj.laytin.repository;

import com.testBankProj.laytin.models.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PhoneRepository extends JpaRepository<Phone,Integer> {
    Optional<Phone> findByPhone(String phone);
}
