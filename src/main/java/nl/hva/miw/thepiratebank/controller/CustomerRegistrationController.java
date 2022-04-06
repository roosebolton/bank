package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.service.CustomerService;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CustomerRegistrationController {
    private CustomerService customerService;

    @Autowired
    public CustomerRegistrationController(CustomerService customerService) {
        super();
        this.customerService = customerService;
    }
    @CrossOrigin(origins = "*", allowCredentials = "false", allowedHeaders = "*")
    @PostMapping(value = "/user/register")
    public ResponseEntity<String> registerUser(@RequestBody CustomerDTO customerDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Customer newCustomer = customerService.attemptCustomerRegistration(customerDTO);
        if (newCustomer == null) {
            String errorString = customerService.buildInputFieldErrorString(customerDTO);
            if (customerService.isUserAlreadyRegistered(errorString))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorString);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorString);
        }
        httpHeaders.add(HttpHeaders.LOCATION, "user/" + newCustomer.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body("Klant geregistreerd");
    }
}
