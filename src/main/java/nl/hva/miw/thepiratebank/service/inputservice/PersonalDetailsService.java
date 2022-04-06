package nl.hva.miw.thepiratebank.service.inputservice;


import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.stereotype.Service;

@Service
public class PersonalDetailsService {
    private static final int[] MAX_FIELD_LENGTHS = {45, 10, 45};

    public static String PersonalDetailsValid (CustomerDTO customerDTO){
        StringBuilder stringBuilder = new StringBuilder();
        if (! isValidFirstName(customerDTO.getVoornaam())) {
            stringBuilder.append("Verplicht veld. Een voornaam heeft alleen letters en maximaal " + MAX_FIELD_LENGTHS[0] +
                    " karakters\n");
        }
        if (! isValidLastName(customerDTO.getAchternaam())){
            stringBuilder.append("Verplicht veld. Een achternaam heeft alleen letters en maximaal " + MAX_FIELD_LENGTHS[2] +
                    " karakters\n");
        }
        if (! isValidInFix(customerDTO.getTussenvoegsel())){
            stringBuilder.append("Een tussenvoegsel heeft alleen letters en maximaal " + MAX_FIELD_LENGTHS[1] +
                    " karakters\n");
        }
        return stringBuilder.toString();
    }

    public static boolean isValidFirstName(String firstName){
        return (firstName.matches("[A-Za-zÀ-ȕ' -]+") && firstName.length() >0 && firstName.length()
                <= MAX_FIELD_LENGTHS[0]);
}

    public static boolean isValidLastName (String lastName){
        return (lastName.matches("[A-Za-zÀ-ȕ' -]+") && lastName.length() >0 && lastName.length()
                <= MAX_FIELD_LENGTHS[2]);
    }

    public static boolean isValidInFix (String inFix){
        return (inFix.isEmpty() || inFix.matches("[A-Za-zÀ-ȕ' -]+") && inFix.length()
                <= MAX_FIELD_LENGTHS[1]);
    }

}
