package nl.hva.miw.thepiratebank.repository;

import java.util.List;
import java.util.Optional;

/** Generic DAO interface
 *
 * @param <T> Type of Object
 * @param <PK> Type of primary Key (String/Integer etc.)
 */

public interface GenericDAOWithOptional<T, PK> {

    List<T> findAll();

    void save(T t);

    Optional<T> find(PK id);

    void update(T t);

    void delete (PK id);

}



