package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AddressService {
    private static final int[] MAX_FIELD_LENGTHS = {6, 10, 10, 100, 30};

    public static String AddressValid (CustomerDTO customerDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!isValidPostalCode(customerDTO.getPostcode())) {
            stringBuilder.append("Verplicht veld. Een postcode moet vier cijfers en twee letters bevatten\n");
        }
        if (!isValidHouseNumber(customerDTO.getHuisnummer())) {
            stringBuilder.append("Verplicht veld. Een huisnummer bevat alleen cijfers en maximaal " + MAX_FIELD_LENGTHS[1] +
                    " karakters\n");
        }
        if (!isValidHouseNumberAddition(customerDTO.getHuisnummertoevoeging())) {
            stringBuilder.append("Een huisnummertoevoeging mag maximaal " + MAX_FIELD_LENGTHS[2] +
                    "karakters bevatten\n");
        }
        if (!isValidStreet(customerDTO.getStraat())) {
            stringBuilder.append("Verplicht veld. Een straat moet minimaal 1 en maximaal " + MAX_FIELD_LENGTHS[3] +
                    " karakters bevatten\n");
        }
        if (!isValidCity(customerDTO.getWoonplaats())) {
            stringBuilder.append("Verplicht veld. Een woonplaats moet minimaal 1 en maximaal " + MAX_FIELD_LENGTHS[4] +
                    " karakters bevatten\n");
        }
        return stringBuilder.toString();
    }

    public static boolean isValidPostalCode(String postcode) {
        String postalCodeRegex = "[1-9]{1}[0-9]{3}[a-zA-Z]{2}";
        Pattern pattern = Pattern.compile(postalCodeRegex);
        return pattern.matcher(postcode).matches();
    }

    public static boolean isValidHouseNumber(String houseNumber) {
        if (houseNumber == null) {
            throw new NullPointerException();
        }
        try {
            return (Integer.parseInt(houseNumber) >= 0 && houseNumber.length() <= MAX_FIELD_LENGTHS[1]);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isValidHouseNumberAddition(String houseNumberAddition) {
        return (houseNumberAddition.length() <= MAX_FIELD_LENGTHS[2]);
    }

    public static boolean isValidStreet(String street) {
        return (street.length() >0 && street.length()
                <= MAX_FIELD_LENGTHS[3]);
    }

    public static boolean isValidCity(String city) {
        return (city.matches("[A-Za-zÀ-ȕ' -]+") && city.length() >0 && city.length()
                < MAX_FIELD_LENGTHS[4]);
    }









}
