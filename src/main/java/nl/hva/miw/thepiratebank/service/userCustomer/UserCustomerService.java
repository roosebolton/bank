package nl.hva.miw.thepiratebank.service.userCustomer;

import nl.hva.miw.thepiratebank.service.CustomerService;
import nl.hva.miw.thepiratebank.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserCustomerService {

    private UserService userService;
    private CustomerService customerService;

    public UserCustomerService(UserService userService, CustomerService customerService) {
        this.userService = userService;
        this.customerService = customerService;
    }

    public UserService getUserService() {
        return userService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }
}
