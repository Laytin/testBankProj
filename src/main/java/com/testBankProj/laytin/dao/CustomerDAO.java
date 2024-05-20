package com.testBankProj.laytin.dao;

import com.testBankProj.laytin.models.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDAO {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public CustomerDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void updateBalance() {
        Session s = entityManager.unwrap(Session.class);
        Query q = s.createQuery("update Customer bc Set bc.currentBalance =(bc.currentBalance + 0.05*bc.currentBalance)" +
                "where (bc.currentBalance + 0.05*bc.currentBalance)<bc.firstDeposit*2.07");
        q.executeUpdate();
    }

    @Transactional
    public List<Customer> preCreateQuery(String username, String phone, String email) {
        Session s = entityManager.unwrap(Session.class);
        Query q = s.createQuery("from Customer as customer join customer.phoneList as phone join customer.emailList as email " +
                "where customer.username like ?1 or phone.phone like ?2 or email.email like ?3");
        q.setParameter(1, username)
                .setParameter(2, phone)
                .setParameter(3, email);
        return q.getResultList();
    }
}
