package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.*;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.WalletDAO;
import nl.hva.miw.thepiratebank.repository.rootrepositories.CustomerRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.UserRootRepository;
import nl.hva.miw.thepiratebank.service.dtomapper.CustomerDTOtoCustomerMapper;
import nl.hva.miw.thepiratebank.service.inputservice.*;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CustomerService {
    private final BcryptHashService hashService;
    private final ExistingUserService existingUserService;
    private final AccountService accountService;
    private final WalletDAO walletDAO;
    private final CustomerRootRepository customerRootRepository;
    private final UserRootRepository userRootRepository;

    @Autowired
    public CustomerService(CustomerRootRepository customerRootRepository, BcryptHashService hashService,
                           ExistingUserService existingUserService, AccountService accountService,
                           WalletDAO walletDAO,UserRootRepository userRootRepository) {
        super();
        this.hashService = hashService;
        this.existingUserService = existingUserService;
        this.accountService = accountService;
        this.walletDAO = walletDAO;
        this.customerRootRepository = customerRootRepository;
        this.userRootRepository = userRootRepository;
    }

    public Customer getCustomerById (int customerId) {
        return customerRootRepository.getCustomer(customerId);
    }

    public Customer attemptCustomerRegistration (CustomerDTO customerDTO) {
        if (buildInputFieldErrorString(customerDTO).isEmpty()) {
            return registerCustomer(customerDTO);
        }
        return null;
    }

    private Customer registerCustomer(CustomerDTO customerDTO) {
            Customer customer = CustomerDTOtoCustomerMapper.mapDTOToCustomer(customerDTO);
            customer.setPassword(hashService.hash(customer.getPassword()));
            userRootRepository.createUser(customer);
            customerRootRepository.createCustomer(customer);
            User correspondingUser = userRootRepository.getByUserName(customer.getUserName());
            createAccountNewCustomer(customerRootRepository.getCustomer(correspondingUser.getUserId()));
            Customer customerSetWallet= customerRootRepository.getCustomer(correspondingUser.getUserId());
            walletDAO.addSingleAssetToWallet(customerSetWallet.getUserId(), "bitcoin", BigDecimal.valueOf(0));
            return customerRootRepository.getCustomer(correspondingUser.getUserId());
    }

    public String buildInputFieldErrorString(CustomerDTO customerDTO) {
        StringBuilder stringBuilder = new StringBuilder();
            try {
                stringBuilder.append(CredentialsService.CredentialsValid(customerDTO));
                stringBuilder.append(PersonalDetailsService.PersonalDetailsValid(customerDTO));
                stringBuilder.append(AddressService.AddressValid(customerDTO));
                stringBuilder.append(IdentifyingInfoService.IdentifyingInfoValid(customerDTO));
                stringBuilder.append(existingUserService.isExistingUser(customerDTO));
            } catch (NullPointerException nullPointerException) {
                return "JSON format incorrect, zie de documentatie voor het juiste format";
            }
        return stringBuilder.toString();
    }


    public boolean isUserAlreadyRegistered(String errorString) {
        return errorString.contains("Dit e-mailadres is al geregistreerd");
    }

    public void createAccountNewCustomer(Customer customer){
        Account accountCustomer = new Account();
        accountCustomer.setCustomer(customer);
        accountCustomer.setBalance(accountCustomer.getSTARTING_BALANCE());
        accountService.createAccount(accountCustomer);
    }

}

