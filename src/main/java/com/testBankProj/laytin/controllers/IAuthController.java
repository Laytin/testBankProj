package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
@RequestMapping("/auth")
public interface IAuthController {
    @Operation(summary = "Login customer")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return token if login ok, either - error message", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)) })})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("/login")
    Map<String, String> performLogin(@RequestBody AuthDTO customerDTO);
    @Operation(summary = "Register (add new) customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer registered. Token return", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)) }),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, register form have errors", content = @Content)})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("/register")
    Map<String, String> register(@RequestBody @Valid AuthDTO authDTO, BindingResult result);
}
