package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.PhoneDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "phone", description = "Modify phone info API")
@RequestMapping("/phone")
public interface IPhoneController {
    @Operation(summary = "Add a new phone to auth Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phone added"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, phone body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("")
    ResponseEntity<HttpStatus> addPhone(@RequestBody @Valid PhoneDTO editDTO,
                                        Authentication auth);

    @Operation(summary = "Edit an existing phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phone edited"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, phone body have errors")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PatchMapping("/{id}")
    ResponseEntity<HttpStatus> editPhone(@PathVariable("id") int id, @RequestBody @Valid PhoneDTO editDTO,
                                         BindingResult result);

    @Operation(summary = "Delete phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "phone deleted"),
            @ApiResponse(responseCode = "400", description = "Smth going wrong")})
    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deletePhone(@PathVariable("id") int id);
}
