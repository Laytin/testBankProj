package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.CustomerDTO;
import com.testBankProj.laytin.dto.TransferRequest;
import com.testBankProj.laytin.dto.TransferResponce;
import com.testBankProj.laytin.security.CustomerDetails;
import com.testBankProj.laytin.service.CustomerService;
import com.testBankProj.laytin.utils.errors.DefaultErrorException;
import com.testBankProj.laytin.utils.errors.DefaultErrorResponce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "customer", description = "Customer API, including transfer req")
@RestController
@RequestMapping("/customer")
public class CustomerController implements ICustomerController{
    private final CustomerService customerService;
    private final ModelMapper mapper;
    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }
    @Override
    public List<CustomerDTO> customerSearch(LocalDate dateOfBirth, String fullname, String phone, String email,
                                            String type, String sortBy, int page){
        List<CustomerDTO> resultMapped = new ArrayList<>();
        customerService.searchCustomer(dateOfBirth,fullname,phone,email,type,sortBy,page)
                .forEach(f-> resultMapped.add(mapper.map(f,CustomerDTO.class)));
        return resultMapped;
    }

    @Override
    public CustomerDTO getUser(int id){
        return mapper.map(customerService.get(id),CustomerDTO.class);
    }

    @Override
    public TransferResponce sendMoney(TransferRequest rq, Authentication auth){
        rq.setOwnerId(((CustomerDetails)auth.getPrincipal()).getCustomer().getId());
        TransferResponce tr = customerService.transferMoney(rq);
        return tr;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<DefaultErrorResponce> handleException(DefaultErrorException e) {
        DefaultErrorResponce response = new DefaultErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
