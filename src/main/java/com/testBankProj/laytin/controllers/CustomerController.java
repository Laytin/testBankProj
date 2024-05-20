package com.testBankProj.laytin.controllers;

import com.testBankProj.laytin.dto.CustomerDTO;
import com.testBankProj.laytin.dto.AuthDTO;
import com.testBankProj.laytin.dto.TransferRequest;
import com.testBankProj.laytin.dto.TransferResponce;
import com.testBankProj.laytin.models.Customer;
import com.testBankProj.laytin.models.Email;
import com.testBankProj.laytin.models.Phone;
import com.testBankProj.laytin.security.CustomerDetails;
import com.testBankProj.laytin.security.JWTCore;
import com.testBankProj.laytin.service.CustomerService;
import com.testBankProj.laytin.utils.errors.DefaultErrorException;
import com.testBankProj.laytin.utils.errors.ErrorBuilder;
import com.testBankProj.laytin.utils.validators.CustomerValidation;
import com.testBankProj.laytin.utils.errors.DefaultErrorResponce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Tag(name = "customer", description = "Customer API, including transfer req")
@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JWTCore jwtCore;
    private final ModelMapper mapper;
    private final CustomerValidation customerValidation;
    private final Logger log= LogManager.getLogger(CustomerController.class);
    @Autowired
    public CustomerController(CustomerService customerService, AuthenticationManager authenticationManager, JWTCore jwtCore, ModelMapper mapper, CustomerValidation customerValidation) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.mapper = mapper;
        this.customerValidation = customerValidation;
    }
    @Operation(summary = "Login customer")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return token if login ok, either - error message",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthDTO customerDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(customerDTO.getUsername(),
                        customerDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            log.info("Login failed. He's try login as:"+customerDTO.getUsername());
            return Map.of("message", "Incorrect credentials!");
        }
        log.info(customerDTO.getUsername()+" enter");
        String token = jwtCore.generateToken(customerDTO.getUsername());
        return Map.of("jwt-token", token);
    }
    @Operation(summary = "Register (add new) customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer registered. Token return",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) }),
            @ApiResponse(responseCode = "400", description = "Smth going wrong, register form have errors",
                    content = @Content)})
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid AuthDTO authDTO,BindingResult result){
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
    @Operation(summary = "Search customer with pagination and sorting")
    @ResponseBody
    @ApiResponse(responseCode = "200", description = "Return result of search. Empty list if nothing found.",
            content = { @Content(mediaType = "application/json") })
    @GetMapping("/search")
    public List<CustomerDTO> customerSearch( @Parameter(name = "dateOfBirth", description = "if search by DOB")
            @RequestParam(value = "dateOfBirth" , required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
                                             @Parameter(name = "fullname", description = "if search by fullname")
                                         @RequestParam(value = "fullname" , required = false) String fullname,
                                             @Parameter(name = "phone", description = "if search by phone")
                                         @RequestParam(value = "phone" , required = false) String phone,
                                             @Parameter(name = "email", description = "if search by email")
                                         @RequestParam(value = "email" , required = false) String email,
                                             @Parameter(name = "type", description = "type of search", examples = {})
                                         @RequestParam(value = "type" ) String type,
                                             @Parameter(name = "sortBy", description = "sorting", example = "DESC")
                                         @RequestParam(value = "sortBy", required = false, defaultValue = "DESC") String sortBy,
                                             @Parameter(name = "page", description = "page of result, start from 1", example = "1")
                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        List<Customer> result = new ArrayList<>();
        List<CustomerDTO> resultMapped = new ArrayList<>();
        log.info("Voided search with type:" + type);
        switch (type.toLowerCase()){
            case "email":
                result.add(customerService.searchByEmail(email));
                break;
            case "phone":
                result.add(customerService.searchByPhone(phone));
                break;
            case "dateofbirth":
                result = customerService.searchByDateOfBirth(page,sortBy,dateOfBirth);
                break;
            case "fullname":
                result = customerService.searchByFullName(page,sortBy,fullname);
                break;
        }
        if(!result.isEmpty())
            result.forEach(f-> resultMapped.add(mapper.map(f,CustomerDTO.class)));
        return resultMapped;
    }
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
    public CustomerDTO getUser(@Parameter(description = "id of customer to be searched") @PathVariable("id") int id){
        return mapper.map(customerService.get(id),CustomerDTO.class);
    }
    @Operation(summary = "Transfer money")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Return result of transfer with boolean state (true/false) and message",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TransferResponce.class)) })
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    @PatchMapping("/trade")
    public TransferResponce sendMoney(@RequestBody @Valid TransferRequest rq, Authentication auth){
        log.info("Trying to send money");
        rq.setOwnerId(((CustomerDetails)auth.getPrincipal()).getCustomer().getId());
        TransferResponce tr = customerService.transferMoney(rq);
        if(!tr.isOk())
            log.info("Transfer failed, message:"+tr.getMessage());
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
