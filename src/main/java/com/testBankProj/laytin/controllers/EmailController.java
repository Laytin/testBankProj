package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.EmailDTO;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.security.CustomerDetails;
import com.testBankProj.laytin.service.EmailService;
import com.testBankProj.laytin.utils.errors.DefaultErrorException;
import com.testBankProj.laytin.utils.errors.ErrorBuilder;
import com.testBankProj.laytin.utils.errors.DefaultErrorResponce;
import com.testBankProj.laytin.utils.validators.EmailValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.util.Map;

@Tag(name = "email", description = "Modify Email info API")
@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    private final EmailValidator emailValidator;
    private final ModelMapper mapper;

    @Autowired
    public EmailController(EmailService emailService, EmailValidator emailValidator, ModelMapper mapper) {
        this.emailService = emailService;
        this.emailValidator = emailValidator;
        this.mapper = mapper;
    }
    @Operation(summary = "Add a new Email to auth Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email added"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, email body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("")
    public ResponseEntity<HttpStatus> addEmail(@RequestBody @Valid EmailDTO editDTO,
                                               Authentication auth, BindingResult result) {
        Email e = mapper.map(editDTO, Email.class);
        e.setCustomer(((CustomerDetails) auth.getPrincipal()).getCustomer());
        emailValidator.validateCreate(e, result);
        if (result.hasErrors())
            ErrorBuilder.buildErrorMessageForClient(result);
        emailService.add(e);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Operation(summary = "Edit an existing Email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email edited"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, email body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editEmail(
            @Parameter(description = "id of email to edited") @PathVariable("id") int id, @RequestBody @Valid EmailDTO editDTO,
            BindingResult result) {
        Email e = mapper.map(editDTO, Email.class);
        e.setId(id);
        emailValidator.validateEdit(e, result);
        if (result.hasErrors())
            ErrorBuilder.buildErrorMessageForClient(result);
        emailService.edit(id, e);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Operation(summary = "Delete email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email deleted"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong")})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmail(
            @Parameter(description = "id of email to deleted") @PathVariable("id") int id) {
        emailValidator.validateDelete(id);
        emailService.delete(id);
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
