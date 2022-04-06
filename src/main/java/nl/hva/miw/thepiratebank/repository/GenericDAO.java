package nl.hva.miw.thepiratebank.repository;

import java.util.List;

/** Generic DAO interface
 *
 * @param <T> Type of Object
 * @param <PK> Type of primary Key (String/Integer etc.)
 */

public interface GenericDAO<T, PK> {

    List<T> getAll();

    void create (T t);

    T get(PK id);

    void update(T t);

    void delete (PK id);

}



