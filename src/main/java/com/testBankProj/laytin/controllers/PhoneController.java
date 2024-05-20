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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Tag(name = "phone", description = "Modify phone info API")
@RestController
@RequestMapping("/phone")
public class PhoneController {
    private final PhoneService phoneService;
    private final ModelMapper mapper;
    private final PhoneValidator phoneValidator;
    @Autowired
    public PhoneController(PhoneService phoneService, ModelMapper mapper, PhoneValidator phoneValidator) {
        this.phoneService = phoneService;
        this.mapper = mapper;
        this.phoneValidator = phoneValidator;
    }
    @Operation(summary = "Add a new phone to auth Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phone added"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, phone body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("")
    public ResponseEntity<HttpStatus> addPhone(@RequestBody @Valid PhoneDTO editDTO,
                                                Authentication auth){
        Phone e = mapper.map(editDTO, Phone.class);
        e.setCustomer((Customer) auth.getPrincipal());
        phoneService.add(e);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Operation(summary = "Edit an existing phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phone edited"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, phone body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPhone(@PathVariable("id") int id, @RequestBody @Valid PhoneDTO editDTO,
                                                BindingResult result){
        Phone e = mapper.map(editDTO, Phone.class);
        e.setId(id);

        phoneValidator.validateEdit(e,result);
        if(result.hasErrors())
            ErrorBuilder.buildErrorMessageForClient(result);
        phoneService.edit(id,e);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Operation(summary = "Delete phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phone deleted"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong")})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePhone(@PathVariable("id") int id){

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
