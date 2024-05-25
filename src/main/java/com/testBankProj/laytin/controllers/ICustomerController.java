package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.CustomerDTO;
import com.testBankProj.laytin.dto.TransferRequest;
import com.testBankProj.laytin.dto.TransferResponce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface ICustomerController {

    @Operation(summary = "Search customer with pagination and sorting")
    @ResponseBody
    @ApiResponse(responseCode = "200", description = "Return result of search. Empty list if nothing found.",
            content = { @Content(mediaType = "application/json") })
    @GetMapping("/search")
    List<CustomerDTO> customerSearch(@Parameter(name = "dateOfBirth", description = "if search by DOB")
                                     @RequestParam(value = "dateOfBirth", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
                                     @Parameter(name = "fullname", description = "if search by fullname")
                                     @RequestParam(value = "fullname", required = false) String fullname,
                                     @Parameter(name = "phone", description = "if search by phone")
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @Parameter(name = "email", description = "if search by email")
                                     @RequestParam(value = "email", required = false) String email,
                                     @Parameter(name = "type", description = "type of search", examples = {})
                                     @RequestParam(value = "type") String type,
                                     @Parameter(name = "sortBy", description = "sorting", example = "DESC")
                                     @RequestParam(value = "sortBy", required = false, defaultValue = "DESC") String sortBy,
                                     @Parameter(name = "page", description = "page of result, start from 1", example = "1")
                                     @RequestParam(value = "page", required = false, defaultValue = "1") int page);

    @Operation(summary = "Get exist user inof")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customer",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
    @GetMapping("/{id}")
    CustomerDTO getUser(@Parameter(description = "id of customer to be searched") @PathVariable("id") int id);

    @Operation(summary = "Transfer money")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Return result of transfer with boolean state (true/false) and message",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TransferResponce.class)) })
    @io.swagger.v3.oas.annotations.parameters.RequestBody

    @PatchMapping("/trade")
    TransferResponce sendMoney(@RequestBody @Valid TransferRequest rq, Authentication auth);
}
