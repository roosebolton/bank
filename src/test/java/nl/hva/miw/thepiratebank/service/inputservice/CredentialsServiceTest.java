package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CredentialsServiceTest {
    private static final int MIN_FIELD_LENGTH = 8;
    private static final int[] MAX_FIELD_LENGTH = {45, 55};

    @Test
    void credentialsValid() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setEmailadres("piet@piraat.nl");
        testDTO1.setWachtwoord("geheimeschatkaart");
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setEmailadres("piet.piraat.nl");
        testDTO2.setWachtwoord("");
        String expected2 = ("Verplicht veld. Er is geen correct emailadres ingevuld\n"+"Verplicht veld. Wachtwoord " +
                "moet minimaal " + MIN_FIELD_LENGTH + " en maximaal "+ MAX_FIELD_LENGTH[1]+ " karakters bevatten\n");
        String actual1 = CredentialsService.CredentialsValid(testDTO1);
        String actual2 = CredentialsService.CredentialsValid(testDTO2);
        assertThat(actual1).isEmpty();
        assertThat(actual2).isNotEmpty().isEqualTo(expected2);
    }

    @Test
    void isValidUsername() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setEmailadres("piet@piraat.nl"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setEmailadres("piet.piraat@zwarte-parel.nl"); // correct format
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setEmailadres("pietpiraat.nl"); // no @ symbol
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setEmailadres("piet@piraat..nl"); // extra dot
        CustomerDTO testDTO5 = new CustomerDTO();
        testDTO5.setEmailadres(""); // cannot be empty
        CustomerDTO testDTO6 = new CustomerDTO();
        testDTO6.setEmailadres("WijzijnpiratenonzedolkezijnvanstaalWijzijndeschrikderzeejastukvoorstukwijallemaal" +
                "WelustenjewelrauwenliefstnogbijhetochtendmaalKnoophetinjeorenpiratentaalWijzijnpiratenenonsleven" +
                "iseenfeestWedrinkenenwezingenmaarwevechtennoghetmeestVannoordtotzuidvanwesttotoostzijnwijalom" +
                "gevreesdHoudemensenbangisdepiratengeest@piratenlied.nl"); //cannot exceed 320 characters
        boolean actual1 = CredentialsService.isValidUsername(testDTO1.getEmailadres());
        boolean actual2 = CredentialsService.isValidUsername(testDTO2.getEmailadres());
        boolean actual3 = CredentialsService.isValidUsername(testDTO3.getEmailadres());
        boolean actual4 = CredentialsService.isValidUsername(testDTO4.getEmailadres());
        boolean actual5 = CredentialsService.isValidUsername(testDTO3.getEmailadres());
        boolean actual6 = CredentialsService.isValidUsername(testDTO4.getEmailadres());
        assertTrue(actual1);
        assertTrue(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
        assertFalse(actual6);
    }

    @Test
    void isValidPassword() {
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setWachtwoord("geheimeschatkaart"); // correct format
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setWachtwoord(""); // cannot be empty
        CustomerDTO testDTO3 = new CustomerDTO();
        testDTO3.setWachtwoord("geheim"); // needs minimum 8 characters
        CustomerDTO testDTO4 = new CustomerDTO();
        testDTO4.setWachtwoord("WijzijnpiratenonzedolkezijnvanstaalWijzijndeschrikderzeejastukvoorstukwijallemaal");
        // cannot exceed 55 characters
        boolean actual1 = CredentialsService.isValidPassword(testDTO1.getWachtwoord());
        boolean actual2 = CredentialsService.isValidPassword(testDTO2.getWachtwoord());
        boolean actual3 = CredentialsService.isValidPassword(testDTO3.getWachtwoord());
        boolean actual4 = CredentialsService.isValidPassword(testDTO4.getWachtwoord());
        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }
}