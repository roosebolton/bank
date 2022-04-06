package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoginServiceTest {
    private LoginService loginServiceTest;
    private UserDAO userDaoMock = Mockito.mock(UserDAO.class);
    private BcryptHashService hashServiceMock = Mockito.mock(BcryptHashService.class);


  public LoginServiceTest() {
      this.loginServiceTest = new LoginService(hashServiceMock,userDaoMock);
  }

  @BeforeEach
  void setUp() {
      User testUser = new User ("test@user.nl", "password");
      Mockito.when(userDaoMock.getByUsername("test@user.nl")).thenReturn(testUser);
      Mockito.when(hashServiceMock.compareHash("password","password")).thenReturn(true);
  }

    @Test
    void getUserValidLoginTestSuccess() {
        User expected = new User ("test@user.nl", "password");
        User actual = loginServiceTest.getUserValidLogin("test@user.nl","password");
        System.out.println(actual);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getUserValidLoginTestFail() {
        User actual = loginServiceTest.getUserValidLogin("test@user.nl","password1");
        assertThat(actual).isNull();
    }


}

