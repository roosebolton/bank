package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.repository.UserDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRootRepository {

    UserDAO userDAO;

    public UserRootRepository(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    ///Users

    /**
     * Get a list of all users
     * @return A list of all users
     */
    public List<User> getAllUsers(){
        return userDAO.getAll();
    }

    /**
     * Creates a new user, based on a given user
     * @param user
     */
    public void createUser(User user){
        userDAO.create(user);
    }

    /**
     * Gets an existing user by its given id
     * @param id
     * @return An existing user by id, null if userid does not exist
     */
    public User getUser(Integer id){
        return userDAO.get(id);
    }

    /**
     * Updates an existing user based on a given user
     * @param user
     */
    public void updateUser(User user){
        userDAO.update(user);
    }

    /**
     * Deletes an existing user, based on its id
     * @param id
     */
    public void deleteUser(Integer id){
        userDAO.delete(id);
    }

    public  User getByUserName(String username) {
        return userDAO.getByUsername(username);
    }

}
