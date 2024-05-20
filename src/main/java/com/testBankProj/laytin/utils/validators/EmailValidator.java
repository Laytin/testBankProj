package com.testBankProj.laytin.utils.validators;

import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.security.CustomerDetails;
import com.testBankProj.laytin.service.EmailService;
import com.testBankProj.laytin.utils.errors.DefaultErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class EmailValidator implements Validator {
    private final EmailService emailService;
    @Autowired
    public EmailValidator(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Email.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
    public void validateEdit(Object target, Errors errors){
        int customer_id = ((CustomerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCustomer().getId();
        Email e = (Email) target;
        Customer cc = emailService.get(e.getId()).getCustomer();
        if(cc==null || cc.getId()!=customer_id)
            errors.rejectValue("id","u are not an owner of this info");
        if(emailService.getByName(e.getEmail()).isPresent())
            errors.rejectValue("email","already exist");
    }

    public void validateDelete(int target) {
        Customer st = ((CustomerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCustomer();
        if(emailService.get(target).getCustomer().getId()!=st.getId())
            throw new DefaultErrorException("u are not an owner");
        if(st.getEmailList().size()<2)
            throw new DefaultErrorException("email list size <2");
    }
    public void validateCreate(Object target, Errors errors) {
        if(emailService.getByName(((Email)target).getEmail()).isPresent())
            errors.rejectValue("email","already exist");
    }
}
