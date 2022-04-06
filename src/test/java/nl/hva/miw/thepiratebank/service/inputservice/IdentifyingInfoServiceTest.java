package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IdentifyingInfoServiceTest {

    @Test
    void identifyingInfoValid() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setBsnnummer("123456789");
        testDTO1.setIbannummer("NL20INGB0001234567");
        testDTO1.setGeboortedatum("1990-02-01");
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setBsnnummer("1234567");
        testDTO2.setIbannummer("NL14TP1569856621");
        testDTO2.setGeboortedatum("1990-24-01");
        String expected2 = ("Verplicht veld. Er is geen correct BSN nummer ingevuld\n"+"Verplicht veld. Er is geen correct IBAN ingevuld\n"+
                "Verplicht veld. Er is geen correcte geboortedatum ingevuld (format: yyyy-MM-dd)\n");
        String actual1 = IdentifyingInfoService.IdentifyingInfoValid(testDTO1);
        String actual2 = IdentifyingInfoService.IdentifyingInfoValid(testDTO2);
        assertThat(actual1).isEmpty();
        assertThat(actual2).isNotEmpty().isEqualTo(expected2);
    }

    @Test
    void isValidBsn() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setBsnnummer("123456789"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setBsnnummer(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setBsnnummer("geheim"); // only int allowed
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setBsnnummer("12345"); // too short min 8 characters
        CustomerDTO testDTO5 = new CustomerDTO();
        testDTO5.setBsnnummer("12345678910"); // too long max 9 characters
        boolean actual1 = IdentifyingInfoService.isValidBsn(testDTO1.getBsnnummer());
        boolean actual2 = IdentifyingInfoService.isValidBsn(testDTO2.getBsnnummer());
        boolean actual3 = IdentifyingInfoService.isValidBsn(testDTO3.getBsnnummer());
        boolean actual4 = IdentifyingInfoService.isValidBsn(testDTO4.getBsnnummer());
        boolean actual5 = IdentifyingInfoService.isValidBsn(testDTO5.getBsnnummer());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
    }

    @Test
    void isValidIban() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setIbannummer("NL20INGB0001234567"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setIbannummer(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setIbannummer("DE23INGB0001236556"); // only Dutch account
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setIbannummer("NL2399990001236556"); // too short min 8 characters
        CustomerDTO testDTO5 = new CustomerDTO();
        testDTO5.setIbannummer("23INGB0001236556"); // NL in front forgotten
        CustomerDTO testDTO6 = new CustomerDTO();
        testDTO6.setIbannummer("NL23INGB00012365567"); // too long max 9 characters
        CustomerDTO testDTO7 = new CustomerDTO();
        testDTO7.setIbannummer("NL35INGB0001234567"); // wrong checkNumber "35"
        boolean actual1 = IdentifyingInfoService.isValidIban(testDTO1.getIbannummer());
        boolean actual2 = IdentifyingInfoService.isValidIban(testDTO2.getIbannummer());
        boolean actual3 = IdentifyingInfoService.isValidIban(testDTO3.getIbannummer());
        boolean actual4 = IdentifyingInfoService.isValidIban(testDTO4.getIbannummer());
        boolean actual5 = IdentifyingInfoService.isValidIban(testDTO5.getIbannummer());
        boolean actual6 = IdentifyingInfoService.isValidIban(testDTO6.getIbannummer());
        boolean actual7 = IdentifyingInfoService.isValidIban(testDTO7.getIbannummer());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
        assertFalse(actual6);
        assertFalse(actual7);
    }

    @Test
    void isValidDateOfBirth() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setGeboortedatum("1990-01-01"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setGeboortedatum(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setGeboortedatum("1990-24-02"); // YYYY-DD-MM instead of YYYY-MM-DD
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setGeboortedatum("90-01-01"); // YY-MM-DD instead of YYYY-MM-DD
        CustomerDTO testDTO5 = new CustomerDTO();
        testDTO5.setGeboortedatum("1990/01/01"); // YYYY/MM/DD instead of YYYY-MM-DD
        boolean actual1 = IdentifyingInfoService.isValidDateOfBirth(testDTO1.getGeboortedatum());
        boolean actual2 = IdentifyingInfoService.isValidDateOfBirth(testDTO2.getGeboortedatum());
        boolean actual3 = IdentifyingInfoService.isValidDateOfBirth(testDTO3.getGeboortedatum());
        boolean actual4 = IdentifyingInfoService.isValidDateOfBirth(testDTO4.getGeboortedatum());
        boolean actual5 = IdentifyingInfoService.isValidDateOfBirth(testDTO5.getGeboortedatum());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
    }
}