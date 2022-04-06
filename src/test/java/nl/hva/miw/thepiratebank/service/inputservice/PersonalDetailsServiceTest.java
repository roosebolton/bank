package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersonalDetailsServiceTest {
    private static final int[] MAX_FIELD_LENGTHS = {45, 10, 45};

    @Test
    void personalDetailsValid() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setVoornaam("Jan Hëin");
        testDTO1.setAchternaam("Piraten-Möppie Biermok");
        testDTO1.setTussenvoegsel("in 't");
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setVoornaam("");
        testDTO2.setAchternaam(" ");
        testDTO2.setTussenvoegsel("10");
        String expected2 = ("Verplicht veld. Een voornaam heeft alleen letters en maximaal " + MAX_FIELD_LENGTHS[0] +
                " karakters\n"+
                "Verplicht veld. Een achternaam heeft alleen letters en maximaal " + MAX_FIELD_LENGTHS[2] +
                " karakters\n"+
                "Een tussenvoegsel heeft alleen letters en maximaal " + MAX_FIELD_LENGTHS[1] +
                " karakters\n");
        String actual1 = PersonalDetailsService.PersonalDetailsValid(testDTO1);
        String actual2 = PersonalDetailsService.PersonalDetailsValid(testDTO2);
        assertThat(actual1).isEmpty();
        assertThat(actual2).isNotEmpty().isEqualTo(expected2);
    }

    @Test
    void isValidFirstName() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setVoornaam("Jan Hëin"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setVoornaam(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setVoornaam("Wij zijn piraten onze dolken zijn van staal\n" +
                "Wij zijn de schrik der zee ja stuk voor stuk wij allemaal"); // cannot exceed 45 characters
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setVoornaam(" "); // space will be trimmed therefore not allowed
        boolean actual1 = PersonalDetailsService.isValidFirstName(testDTO1.getVoornaam());
        boolean actual2 = PersonalDetailsService.isValidFirstName(testDTO2.getVoornaam());
        boolean actual3 = PersonalDetailsService.isValidFirstName(testDTO3.getVoornaam());
        boolean actual4 = PersonalDetailsService.isValidFirstName(testDTO4.getVoornaam());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }

    @Test
    void isValidLastName() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setAchternaam("Piraten-Möppie Biermok"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setAchternaam(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setAchternaam("Wij zijn piraten onze dolken zijn van staal\n" +
                "Wij zijn de schrik der zee ja stuk voor stuk wij allemaal"); // cannot exceed 45 characters
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setAchternaam(" "); // space will be trimmed therefore not allowed
        boolean actual1 = PersonalDetailsService.isValidLastName(testDTO1.getAchternaam());
        boolean actual2 = PersonalDetailsService.isValidLastName(testDTO2.getAchternaam());
        boolean actual3 = PersonalDetailsService.isValidLastName(testDTO3.getAchternaam());
        boolean actual4 = PersonalDetailsService.isValidLastName(testDTO4.getAchternaam());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }

    @Test
    void isValidInFix() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setTussenvoegsel("in 't"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setTussenvoegsel(""); // empty allowed
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setTussenvoegsel("Wij zijn piraten onze dolken zijn van staal"); // cannot exceed 10 characters
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setTussenvoegsel("1"); // numbers not allowed
        boolean actual1 = PersonalDetailsService.isValidInFix(testDTO1.getTussenvoegsel());
        boolean actual2 = PersonalDetailsService.isValidInFix(testDTO2.getTussenvoegsel());
        boolean actual3 = PersonalDetailsService.isValidInFix(testDTO3.getTussenvoegsel());
        boolean actual4 = PersonalDetailsService.isValidInFix(testDTO4.getTussenvoegsel());
        assertTrue(actual1);
        assertTrue(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }
}