package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
import nl.hva.miw.thepiratebank.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOTest {
    //Necessary to prevent postconstruct annotation from running do not delete
    @MockBean
    private AssetConsumerController assetConsumerController;

    private final UserDAO userDAO;
    List<User> userListActual;

    @BeforeAll
    void setup() {
        userListActual = userDAO.getAll();
    }

    @Autowired
    public UserDAOTest(UserDAO userdao) {
        super();
        this.userDAO = userdao;
    }


    @Test
    void getAll() {
        int expected = 2;
        int actual = userListActual.size();
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void create() {
        User newUser = new User("Anja","pw1");
        userDAO.create(newUser);
        List<User> userListActual = userDAO.getAll();
        assertThat(userListActual.size()).isEqualTo(3);
    }

    @Test
    void get() {
        User actual = userDAO.get(2);
        User expected = new User("tjardy@tjardy.com","password");
        expected.setUserId(2);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void update() {
        User expected = new User("max@min.com","password");
        expected.setUserId(1);
        userDAO.update(expected);
        User actual = userDAO.get(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void delete() {
        User newUser = new User("henk@henk.nl","pw1");
        newUser.setUserId(4);
        userDAO.create(newUser);
        assertThat(userDAO.get(4)).isEqualTo(newUser);
        userDAO.delete(4);
        User user = userDAO.get(4);
        assertThat(user).isEqualTo(null);
    }

    @Test
    void getByUsername() {
        User newUser = new User("five@five.nl","pw1");
        newUser.setUserId(5);
        userDAO.create(newUser);
        User actual = userDAO.getByUsername("five@five.nl");
        assertThat(actual).isNotNull().isEqualTo(newUser);
    }
}
