package nl.hva.miw.thepiratebank.service.inputservice;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ExistingUserServiceTest {

    private ExistingUserService serviceUnderTest;
    private RootRepository rootRepositoryMock = Mockito.mock(RootRepository.class);

    public ExistingUserServiceTest() {
        this.serviceUnderTest = new ExistingUserService(rootRepositoryMock);
    }

    @Test
    void isExistingUser() {
        User testUser = new User("piet@piraat.nl", "geheimeschatkaart");
        Mockito.when(rootRepositoryMock.getByUserName("blauw@baard.nl")).thenReturn(testUser);
        CustomerDTO testDTO1 = new CustomerDTO();
        testDTO1.setEmailadres("piet@piraat.nl");
        CustomerDTO testDTO2 = new CustomerDTO();
        testDTO2.setEmailadres("blauw@baard.nl");
        String actual1 = serviceUnderTest.isExistingUser(testDTO1);
        String actual2 = serviceUnderTest.isExistingUser(testDTO2);
        assertThat(actual1).isEmpty();
        assertThat(actual2).isNotEmpty().isEqualTo("Dit e-mailadres is al geregistreerd");
    }
}