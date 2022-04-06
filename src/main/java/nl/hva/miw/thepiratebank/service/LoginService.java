package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final BcryptHashService bcryptHashService;
    private final UserDAO userDAO;

    public LoginService(BcryptHashService hashService, UserDAO userDAO) {
        this.bcryptHashService = hashService;
        this.userDAO = userDAO;
    }

    public User getUserValidLogin(String username, String password) {
        User userFromDB = userDAO.getByUsername(username);
        if (userFromDB != null && bcryptHashService.compareHash(password,userFromDB.getPassword())) {
            return userFromDB;
        } else {
            return null;
        }
    }

}
