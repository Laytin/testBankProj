package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.EmailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Tag(name = "email", description = "Modify Email info API")
@RequestMapping("/email")
public interface IEmailController {
    @Operation(summary = "Add a new Email to auth Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email added"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, email body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("")
    ResponseEntity<HttpStatus> addEmail(@RequestBody @Valid EmailDTO editDTO,
                                        Authentication auth,
                                        BindingResult result);

    @Operation(summary = "Edit an existing Email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email edited"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, email body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PatchMapping("/{id}")
    ResponseEntity<HttpStatus> editEmail(
            @Parameter(description = "id of email to edited") @PathVariable("id") int id,
            @RequestBody @Valid EmailDTO editDTO,
            BindingResult result);

    @Operation(summary = "Delete email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email deleted"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong")})
    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteEmail(@Parameter(description = "id of email to deleted") @PathVariable("id") int id);
}
