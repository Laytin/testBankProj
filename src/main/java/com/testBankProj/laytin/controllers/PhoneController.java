package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.PhoneDTO;
import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Phone;
import com.testBankProj.laytin.service.PhoneService;
import com.testBankProj.laytin.utils.errors.DefaultErrorException;
import com.testBankProj.laytin.utils.errors.ErrorBuilder;
import com.testBankProj.laytin.utils.errors.DefaultErrorResponce;
import com.testBankProj.laytin.utils.validators.PhoneValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/phone")
public class PhoneController implements IPhoneController{
    private final PhoneService phoneService;
    private final ModelMapper mapper;
    private final PhoneValidator phoneValidator;
    @Autowired
    public PhoneController(PhoneService phoneService, ModelMapper mapper, PhoneValidator phoneValidator) {
        this.phoneService = phoneService;
        this.mapper = mapper;
        this.phoneValidator = phoneValidator;
    }
    @Override
    public ResponseEntity<HttpStatus> addPhone(PhoneDTO editDTO, Authentication auth){
        Phone e = mapper.map(editDTO, Phone.class);
        e.setCustomer((Customer) auth.getPrincipal());
        phoneService.add(e);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Override
    public ResponseEntity<HttpStatus> editPhone(int id, PhoneDTO editDTO, BindingResult result){
        Phone e = mapper.map(editDTO, Phone.class);
        e.setId(id);

        phoneValidator.validateEdit(e,result);
        if(result.hasErrors())
            ErrorBuilder.buildErrorMessageForClient(result);
        phoneService.edit(id,e);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Override
    public ResponseEntity<HttpStatus> deletePhone(int id){
        phoneValidator.validateDelete(id);
        phoneService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<DefaultErrorResponce> handleException(DefaultErrorException e) {
        DefaultErrorResponce response = new DefaultErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
