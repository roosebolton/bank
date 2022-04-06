package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExistingUserService {

    private RootRepository rootRepository;

    @Autowired
    public ExistingUserService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;

    }

    public String isExistingUser(CustomerDTO customerDTO) {
        String result = "";
        User user = rootRepository.getByUserName(customerDTO.getEmailadres());
        if (user != null) {
            result = "Dit e-mailadres is al geregistreerd";
        } return result;
    }
}
