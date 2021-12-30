package app.wrk.database;

import app.exception.MyDBException;
import java.util.List;

/**
 *
 * @author dingl01
 * @param <E> The class of the object in the database
 * @param <PK> The class of the Primary Key
 */
public interface JpaDaoItf<E, PK> {

    void create(E e) throws MyDBException;

    E read(PK pk) throws MyDBException;

    boolean modify(E e) throws MyDBException;

    void remove(PK pk) throws MyDBException;

    E search(String prop, Object valeur) throws MyDBException;

    List<E> readList() throws MyDBException;

    int removeList() throws MyDBException;

    int saveList(List<E> list) throws MyDBException;

    boolean isConnected();

    void disconnect();

}
