package nl.hva.miw.thepiratebank.service.inputservice;


import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Service
public class IdentifyingInfoService {
    private static final int MINIMUM_BSN_LENGTH = 8;
    private static final int MAXIMUM_BSN_LENGTH = 9;
    private static final BigInteger IBAN_CHECK_VALUE = new BigInteger("97");

    public static String IdentifyingInfoValid (CustomerDTO customerDTO){
        StringBuilder stringBuilder = new StringBuilder();
        if (! isValidBsn(customerDTO.getBsnnummer())) {
            stringBuilder.append("Verplicht veld. Er is geen correct BSN nummer ingevuld\n");
        }
        if (! isValidIban(customerDTO.getIbannummer())){
            stringBuilder.append("Verplicht veld. Er is geen correct IBAN ingevuld\n");
        }
        if (! isValidDateOfBirth(customerDTO.getGeboortedatum())){
            stringBuilder.append("Verplicht veld. Er is geen correcte geboortedatum ingevuld (format: yyyy-MM-dd)\n");
        }
        return stringBuilder.toString();
    }

    public static boolean isValidBsn(String bsnNummer) {
        return (bsnNummer.length() >= MINIMUM_BSN_LENGTH && bsnNummer.length() <= MAXIMUM_BSN_LENGTH);
    }

    public static boolean isValidIban(String IBAN) {
        String ibanRegex = "^NL\\d{2}[A-Z]{4}0\\d{9}$";
        Pattern pattern = Pattern.compile(ibanRegex);
        if (pattern.matcher(IBAN).matches()){
            return isValidCheckNumber(IBAN);
        } return false;
    }

    // helper methode to verify the check number ("controlegetal")
    public static boolean isValidCheckNumber(String IBAN){
        // move first 4 characters to the end;
        IBAN = IBAN.substring(4) + IBAN.substring(0,4);
        // replace letters with numbers where A=10 and Z=35
        StringBuilder numericIBAN = new StringBuilder();
        int numericValue;
        for (int index = 0; index < IBAN.length(); index++){
            numericValue = Character.getNumericValue(IBAN.charAt(index));
            if (numericValue < 0){ // this should not happen because of the regex
                return false;
            } else {
                numericIBAN.append(numericValue);
            }
        }
        // make an integer of the iban with all numeric values
        BigInteger IBANDigits = new BigInteger(numericIBAN.toString());
        // valid check number when this condition is true
        return (IBANDigits.mod(IBAN_CHECK_VALUE).intValue() ==1);
    }


    public static boolean isValidDateOfBirth(String dateOfBirth) {
        try {
            LocalDate.parse(dateOfBirth);
            return true;
        } catch (DateTimeParseException dateTimeParseException) {
            return false;
        }
    }
}
