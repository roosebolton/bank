package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressServiceTest {
    private static final int[] MAX_FIELD_LENGTHS = {6, 10, 10, 100, 30};

    @Test
    void addressValid() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setPostcode("1000AA");
        testDTO1.setHuisnummer("10");
        testDTO1.setHuisnummertoevoeging("");
        testDTO1.setStraat("schatkiststraat");
        testDTO1.setWoonplaats("'s-Onbewoënd Eiland");
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setPostcode("Arrr");
        testDTO2.setHuisnummer("Arrr");
        testDTO2.setHuisnummertoevoeging("WalkThePlankMaggot");
        testDTO2.setStraat("");
        testDTO2.setWoonplaats("");
        String expected2 = ("Verplicht veld. Een postcode moet vier cijfers en twee letters bevatten\n"+"Verplicht veld. " +
                "Een huisnummer bevat alleen cijfers en maximaal " + MAX_FIELD_LENGTHS[1] +
                " karakters\n"+"Een huisnummertoevoeging mag maximaal " + MAX_FIELD_LENGTHS[2] +
                "karakters bevatten\n"+"Verplicht veld. Een straat moet minimaal 1 en maximaal " + MAX_FIELD_LENGTHS[3] +
                " karakters bevatten\n"+"Verplicht veld. Een woonplaats moet minimaal 1 en maximaal " + MAX_FIELD_LENGTHS[4] +
                " karakters bevatten\n");
        String actual1 = AddressService.AddressValid(testDTO1);
        String actual2 = AddressService.AddressValid(testDTO2);
        assertThat(actual1).isEmpty();
        assertThat(actual2).isNotEmpty().isEqualTo(expected2);
    }

    @Test
    void isValidPostalCode() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setPostcode("1000AA"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setPostcode("0100AA"); // cannot start with zero
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setPostcode("1000 AA"); // max length 6 characters
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setPostcode("100012"); // characters need to be 4 numbers followed by 2 letters
        CustomerDTO testDTO5 = new CustomerDTO();
        testDTO5.setPostcode("AA1000"); // characters need to be 4 numbers followed by 2 letters
        CustomerDTO testDTO6 = new CustomerDTO();
        testDTO6.setPostcode(""); // cannot be empty
        boolean actual1 = AddressService.isValidPostalCode(testDTO1.getPostcode());
        boolean actual2 = AddressService.isValidPostalCode(testDTO2.getPostcode());
        boolean actual3 = AddressService.isValidPostalCode(testDTO3.getPostcode());
        boolean actual4 = AddressService.isValidPostalCode(testDTO4.getPostcode());
        boolean actual5 = AddressService.isValidPostalCode(testDTO5.getPostcode());
        boolean actual6 = AddressService.isValidPostalCode(testDTO6.getPostcode());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
        assertFalse(actual6);
    }

    @Test
    void isValidHouseNumber() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setHuisnummer("10"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setHuisnummer("AA"); // can be only int
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setHuisnummer("-10"); // can only be positive int
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setHuisnummer(""); // cannot be empty
        CustomerDTO testDTO5 = new CustomerDTO();
        testDTO5.setHuisnummer("1234567891011"); // cannot exceed 10 characters
        boolean actual1 = AddressService.isValidHouseNumber(testDTO1.getHuisnummer());
        boolean actual2 = AddressService.isValidHouseNumber(testDTO2.getHuisnummer());
        boolean actual3 = AddressService.isValidHouseNumber(testDTO3.getHuisnummer());
        boolean actual4 = AddressService.isValidHouseNumber(testDTO4.getHuisnummer());
        boolean actual5 = AddressService.isValidHouseNumber(testDTO5.getHuisnummer());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
    }

    @Test
    void isValidHouseNumberAddition() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setHuisnummertoevoeging(""); // empty allowed
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setHuisnummertoevoeging("A"); // letters allowed
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setHuisnummertoevoeging("1"); // numbers allowed
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setHuisnummertoevoeging("1234567891011"); // cannot exceed 10 characters
        boolean actual1 = AddressService.isValidHouseNumberAddition(testDTO1.getHuisnummertoevoeging());
        boolean actual2 = AddressService.isValidHouseNumberAddition(testDTO2.getHuisnummertoevoeging());
        boolean actual3 = AddressService.isValidHouseNumberAddition(testDTO3.getHuisnummertoevoeging());
        boolean actual4 = AddressService.isValidHouseNumberAddition(testDTO4.getHuisnummertoevoeging());
        assertTrue(actual1);
        assertTrue(actual2);
        assertTrue(actual3);
        assertFalse(actual4);
    }

    @Test
    void isValidStreet() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setStraat("schatkiststraat"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setStraat(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setStraat("Wij zijn piraten, onze dolken zijn van staal\n" +
                "Wij zijn de schrik der zee, ja stuk voor stuk wij allemaal"); // cannot exceed 100 characters
        boolean actual1 = AddressService.isValidStreet(testDTO1.getStraat());
        boolean actual2 = AddressService.isValidStreet(testDTO2.getStraat());
        boolean actual3 = AddressService.isValidStreet(testDTO3.getStraat());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
    }

    @Test
    void isValidCity() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setWoonplaats("'s-Onbewoënd Eiland"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setWoonplaats(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setWoonplaats("Wij zijn piraten, onze dolken zijn van staal"); // cannot exceed 30 characters
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setWoonplaats(" "); // space will be trimmed therefore not allowed
        boolean actual1 = AddressService.isValidCity(testDTO1.getWoonplaats());
        boolean actual2 = AddressService.isValidCity(testDTO2.getWoonplaats());
        boolean actual3 = AddressService.isValidCity(testDTO3.getWoonplaats());
        boolean actual4 = AddressService.isValidCity(testDTO3.getWoonplaats());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }
}