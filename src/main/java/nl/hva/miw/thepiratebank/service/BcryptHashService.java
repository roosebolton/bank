package nl.hva.miw.thepiratebank.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptHashService {
    private final PasswordEncoder passwordEncoder;
    private final PepperService pepperService;

    public BcryptHashService(PasswordEncoder passwordEncoder, PepperService pepperService) {
        this.passwordEncoder = passwordEncoder;
        this.pepperService = pepperService;
    }

    public String hash (String string) {
        return  passwordEncoder.encode(string+pepperService.getPepper());
    }

    public boolean compareHash (String password,String hashstring) {
        return passwordEncoder.matches(password+pepperService.getPepper(),hashstring);
    }


}
