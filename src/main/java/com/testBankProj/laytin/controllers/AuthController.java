package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.AuthDTO;
import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import com.testBankProj.laytin.security.JWTCore;
import com.testBankProj.laytin.service.CustomerService;
import com.testBankProj.laytin.utils.errors.ErrorBuilder;
import com.testBankProj.laytin.utils.validators.CustomerValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController implements IAuthController{
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JWTCore jwtCore;
    private final ModelMapper mapper;
    private final CustomerValidation customerValidation;
    @Autowired
    public AuthController(CustomerService customerService, AuthenticationManager authenticationManager, JWTCore jwtCore, ModelMapper mapper, CustomerValidation customerValidation) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.mapper = mapper;
        this.customerValidation = customerValidation;
    }
    @Override
    public Map<String, String> performLogin(AuthDTO customerDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(customerDTO.getUsername(),
                        customerDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }
        String token = jwtCore.generateToken(customerDTO.getUsername());
        return Map.of("jwt-token", token);
    }
    @Override

    public Map<String, String> register(AuthDTO authDTO, BindingResult result){
        Customer c = mapper.map(authDTO, Customer.class);
        c.setEmailList(new ArrayList<>(){{add(new Email(authDTO.getEmail(),c));}});
        c.setPhoneList(new ArrayList<>(){{add(new Phone(authDTO.getPhone(),c));}});
        customerValidation.validate(c,result);
        if(result.hasErrors())
            ErrorBuilder.buildErrorMessageForClient(result);

        customerService.register(c);
        String token = jwtCore.generateToken(c.getUsername());
        return Map.of("jwt-token", token);
    }
}
