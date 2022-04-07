package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.CustomerDAO;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.UserDAO;
import nl.hva.miw.thepiratebank.repository.rootrepositories.UserRootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    private UserService serviceUnderTest;
    private UserDAO userDAOMock = Mockito.mock(UserDAO.class);
    private CustomerDAO customerDAOMock = Mockito.mock(CustomerDAO.class);


    @MockBean
    private UserRootRepository userRootRepositoryMock;

    @Autowired
    public UserServiceTest(UserService userService){
        this.serviceUnderTest = userService;
    }

    @BeforeEach
    void setUp() {
        User testuser1 = new User ("test1@user.nl", "password1");
        User testuser2 = new User ("test2@user.nl", "password2");
        Mockito.when(userRootRepositoryMock.getByUserName("test1@User.nl")).thenReturn(testuser1);
        Mockito.when(userRootRepositoryMock.getByUserName("test2@User.nl")).thenReturn(testuser2);
        Mockito.when(userRootRepositoryMock.getUser(1)).thenReturn(testuser1);
        Mockito.when(userRootRepositoryMock.getUser(0)).thenReturn(null);
    }


    @Test
    void getUserByUserName() {
        User expectedUser1 = new User("test1@user.nl","password1");
        User expectedUser2 = new User("test2@user.nl","password2");
        User actual1 = serviceUnderTest.getUserByUserName("test1@User.nl");
        User actual2 = serviceUnderTest.getUserByUserName("test2@User.nl");
        assertThat(actual1).isNotNull().isEqualTo(expectedUser1);
        assertThat(actual2).isNotNull().isEqualTo(expectedUser2);
        assertThat(actual1).isNotEqualTo(actual2);
    }

    @Test
    void getUserByUserId() {
        User expectedUser1 = new User("test1@user.nl","password1");
        User actual1 = serviceUnderTest.getUserByUserId(1);
        User actual2 = serviceUnderTest.getUserByUserId(0);
        assertThat(actual1).isNotNull().isEqualTo(expectedUser1);
        assertThat(actual2).isNull();
    }

}