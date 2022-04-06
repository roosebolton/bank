package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private RootRepository repository;

    @Autowired
    public UserService(RootRepository repository){
        this.repository = repository;
    }

    public User getUserByUserName (String username) {
        return repository.getByUserName(username);
    }

    public User getUserByUserId (int userId) {
        return repository.getUser(userId);
    }


}
