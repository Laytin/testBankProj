package com.testBankProj.laytin.service;

import com.testBankProj.laytin.dto.CustomerDTO;
import com.testBankProj.laytin.dto.TransferRequest;
import com.testBankProj.laytin.dto.TransferResponce;
import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import com.testBankProj.laytin.repository.CustomerRepository;
import com.testBankProj.laytin.repository.EmailRepository;
import com.testBankProj.laytin.repository.PhoneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerService{
    private final CustomerRepository customerRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger log= LogManager.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomerRepository customerRepository, EmailRepository emailRepository, PhoneRepository phoneRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.emailRepository = emailRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void register(Customer c) {
        c.setPassword(passwordEncoder.encode(c.getPassword()));
        c.setCurrentBalance(c.getFirstDeposit());
        int i  = customerRepository.save(c).getId();
        //Hibernate SAVE_UPDATE press f o_O
        emailRepository.save(c.getEmailList().get(0));
        phoneRepository.save(c.getPhoneList().get(0));
        c.setId(i);

        log.info("Customer registered:"+c.toString());
    }
    public Customer get(int id) {
        Customer c = customerRepository.getById(id);
        Hibernate.initialize(c.getPhoneList());
        Hibernate.initialize(c.getEmailList());
        return c;
    }
    public List<Customer> searchCustomer(LocalDate dateOfBirth,String fullname,String phone,String email,String type,String sortBy,int page){
        List<Customer> result = new ArrayList<>();
        log.info("Voided search with type:" + type);
        switch (type.toLowerCase()){
            case "email":
                result.add(searchByEmail(email));
                break;
            case "phone":
                result.add(searchByPhone(phone));
                break;
            case "dateofbirth":
                result = searchByDateOfBirth(page,sortBy,dateOfBirth);
                break;
            case "fullname":
                result = searchByFullName(page,sortBy,fullname);
                break;
        }
        return result;
    }
    public Customer searchByEmail(String email) {
        Optional<Email> em = emailRepository.findByEmail(email);
        if (em.isPresent())
            return em.get().getCustomer();
        return null;
    }
    public Customer searchByPhone(String phone) {
        Optional<Phone> ph = phoneRepository.findByPhone(phone);
        if (ph.isPresent())
            return ph.get().getCustomer();
        return null;
    }
    public List<Customer> searchByFullName(int page, String sortBy, String fullname) {
        return customerRepository.findCustomersByFullnameLike(fullname, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(sortBy), "id")));
    }
    public List<Customer> searchByDateOfBirth(int page, String sortBy, LocalDate date) {
        return customerRepository.findCustomersByDateOfBirthIsGreaterThan(Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)), PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(sortBy), "id")));
    }
    @Transactional
    public TransferResponce transferMoney(TransferRequest tq){
        int idOwner = tq.getOwnerId();
        String destUsername = tq.getDestUsername();
        float sum = tq.getSum();
        Customer source = customerRepository.getById(idOwner);
        if(source==null)
            return new TransferResponce("owner id is invalid");
        if(source.getCurrentBalance()<sum)
            return new TransferResponce("Not enought money");
        Optional<Customer> destintation = customerRepository.findByUsername(destUsername);
        if(!destintation.isPresent())
            return new TransferResponce("Destination user not found. Is username correct?");
        //if all is ok
        source.setCurrentBalance(source.getCurrentBalance()-sum);
        destintation.get().setCurrentBalance(destintation.get().getCurrentBalance()+sum);
        customerRepository.save(source);
        customerRepository.save(destintation.get());
        TransferResponce tr = new TransferResponce("Transfer succes");
        tr.setOk(true);
        log.info("Money transfered beetwen:"+source.getId() + "->" + destintation.get().getUsername() + " with sum:"+sum);
        return tr;
    }
}
