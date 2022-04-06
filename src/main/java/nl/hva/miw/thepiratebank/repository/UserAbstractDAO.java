package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class UserAbstractDAO implements GenericDAO<User, Integer> {

    abstract User getByUsername(String username);

}
