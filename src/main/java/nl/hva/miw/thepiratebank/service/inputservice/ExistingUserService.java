package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import nl.hva.miw.thepiratebank.repository.rootrepositories.UserRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExistingUserService {

    private UserRootRepository userRootRepository;

    @Autowired
    public ExistingUserService(UserRootRepository userRootRepository) {
        super();
        this.userRootRepository = userRootRepository;

    }

    public String isExistingUser(CustomerDTO customerDTO) {
        String result = "";
        User user = userRootRepository.getByUserName(customerDTO.getEmailadres());
        if (user != null) {
            result = "Dit e-mailadres is al geregistreerd";
        } return result;
    }
}
