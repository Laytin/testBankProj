package com.testBankProj.laytin.utils.validators;

import com.testBankProj.laytin.dao.CustomerDAO;
import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerValidation implements Validator {
    private final CustomerDAO customerDAO;
    @Autowired
    public CustomerValidation(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer targetC = (Customer) target;
        //max size = 3 (cuz username, phone and email - uniq, there can be found only 3 same obj)
        List<Customer> sameObjects = customerDAO.preCreateQuery(targetC.getUsername(),targetC.getPhoneList().get(0).getPhone(),targetC.getEmailList().get(0).getEmail());
        if(!sameObjects.isEmpty()){
            boolean sameUsername = false,samePhone=false, sameEmail =false;
            for(Customer cs: sameObjects){
                if(cs.getUsername().equals(targetC.getUsername()))
                    errors.rejectValue("username", "username already exist");
                if(cs.getEmailList().stream().anyMatch(f-> f.getEmail().equals(targetC.getEmailList().get(0).getEmail())))
                    errors.rejectValue("email", "email already exist");
                if(cs.getPhoneList().stream().anyMatch(f-> f.getPhone().equals(targetC.getPhoneList().get(0).getPhone())))
                    errors.rejectValue("phone", "phone already exist");
            }
        }
    }
}
