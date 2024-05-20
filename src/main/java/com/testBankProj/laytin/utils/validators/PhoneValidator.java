package com.testBankProj.laytin.utils.validators;

import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import com.testBankProj.laytin.security.CustomerDetails;
import com.testBankProj.laytin.service.PhoneService;
import com.testBankProj.laytin.utils.errors.DefaultErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PhoneValidator implements Validator {
    private final PhoneService phoneService;
    @Autowired
    public PhoneValidator(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Phone.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
    public void validateEdit(Object target, Errors errors){
        int customer_id = ((CustomerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCustomer().getId();
        Phone e = (Phone) target;
        Customer cc = phoneService.get(e.getId()).getCustomer();
        if(cc==null || cc.getId()!=customer_id)
            errors.rejectValue("id","u are not an owner of this info");
        if(phoneService.getByName(e.getPhone()).isPresent())
            errors.rejectValue("phone","already exist");
    }

    public void validateDelete(int target) {
        Customer st = ((CustomerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCustomer();
        if(phoneService.get(target).getCustomer().getId()!=st.getId())
            throw new DefaultErrorException("u are not an owner");
        if(st.getPhoneList().size()<2)
            throw new DefaultErrorException("email list size <2");
    }
}
