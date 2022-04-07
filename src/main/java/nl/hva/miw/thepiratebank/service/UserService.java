package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.UserRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRootRepository userRootRepository;

    @Autowired
    public UserService(UserRootRepository userRootRepository){
        this.userRootRepository = userRootRepository;
    }

    public User getUserByUserName (String username) {
        return userRootRepository.getByUserName(username);
    }

    public User getUserByUserId (int userId) {
        return userRootRepository.getUser(userId);
    }


}
