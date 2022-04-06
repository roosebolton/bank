package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CredentialsService {
    private static final int[] MAX_FIELD_LENGTH = {45, 55};
    private static final int MIN_FIELD_LENGTH = 8;

    public static String CredentialsValid(CustomerDTO customerDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!isValidUsername(customerDTO.getEmailadres())) {
            stringBuilder.append("Verplicht veld. Er is geen correct emailadres ingevuld\n");
        }
        if (!isValidPassword(customerDTO.getWachtwoord())) {
            stringBuilder.append("Verplicht veld. Wachtwoord moet minimaal " + MIN_FIELD_LENGTH +
                    " en maximaal "+ MAX_FIELD_LENGTH[1]+ " karakters bevatten\n");
        }
        return stringBuilder.toString();
    }


    public static boolean isValidUsername(String userName) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return (pattern.matcher(userName).matches() && userName.length() > 0 && userName.length() <= MAX_FIELD_LENGTH[0]);
    }

    public static boolean isValidPassword(String password) {
        return (password.length() >= MIN_FIELD_LENGTH && password.length() <= MAX_FIELD_LENGTH[1]);
    }
}
