package com.testBankProj.laytin.service;

import com.testBankProj.laytin.models.Phone;
import com.testBankProj.laytin.repository.PhoneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final Logger log= LogManager.getLogger(PhoneService.class);
    @Autowired
    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }
    public Phone get(int id){
        return phoneRepository.getById(id);
    }
    public Optional<Phone> getByName(String id){
        return phoneRepository.findByPhone(id);
    }
    @Transactional
    public void edit(int id, Phone e){
        Phone oldPhone = phoneRepository.findById(id).get();
        oldPhone.setPhone(e.getPhone());
        phoneRepository.save(oldPhone);
        log.info("Phone edited:"+oldPhone.toString());
    }
    @Transactional
    public void delete(int id) {
        phoneRepository.delete(phoneRepository.findById(id).get());
        log.info("Phone deleted:"+id);
    }
    @Transactional
    public void add(Phone e) {
        phoneRepository.save(e);
        log.info("Phone added:"+e.toString());
    }
}
