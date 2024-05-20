package com.testBankProj.laytin.service;

import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.repository.EmailRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmailService {
    private final EmailRepository emailRepository;
    private final Logger log= LogManager.getLogger(EmailService.class);

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Email get(int id){
        return emailRepository.getById(id);
    }
    public Optional<Email> getByName(String id){
        return emailRepository.findByEmail(id);
    }
    @Transactional
    public void edit(int id, Email e){
        Email oldEmail = emailRepository.findById(id).get();
        oldEmail.setEmail(e.getEmail());
        emailRepository.save(oldEmail);
        log.info("Email edited:"+oldEmail.toString());
    }
    @Transactional
    public void delete(int id) {
        emailRepository.delete(emailRepository.findById(id).get());
        log.info("Email deleted:"+id);
    }
    @Transactional
    public void add(Email e) {
        emailRepository.save(e);
        log.info("Email added:"+e.toString());
    }
}
