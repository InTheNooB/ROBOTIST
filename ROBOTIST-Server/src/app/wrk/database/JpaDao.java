package app.wrk.database;

import app.exception.MyDBException;
import app.utils.SystemLib;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author dingl01
 * @param <E> The class of the object in the database
 * @param <PK> The class of the Primary Key
 */
public class JpaDao<E, PK> implements JpaDaoItf<E, PK> {

    private Class<E> cl;
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction et;

    public JpaDao(String pu, Class<E> cl) throws MyDBException {
        this.cl = cl;
        try {
            emf = Persistence.createEntityManagerFactory(pu);
            em = emf.createEntityManager();
            et = em.getTransaction();
        } catch (Exception ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    /**
     * Creates a new entry in the database.
     *
     * @param e The Entity-Bean to add in the database.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public void create(E e) throws MyDBException {
        try {
            et.begin();
            em.persist(e);
            et.commit();
        } catch (Exception ex) {
            et.rollback();
        }
    }

    /**
     * Reads an entry from the database.
     *
     * @param pk The Primary Key of the entry to read.
     * @return E The Entity-Bean corresponding to the entry in the database if
     * any was found. Null otherwise.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public E read(PK pk) throws MyDBException {
        return search("pk", pk);
    }

    /**
     * Modifies an entry from the database.
     *
     * @param e The Entity-Bean that needs to be updated in the database.
     * @return a boolean, true if the entry was successfully updated, false
     * otherwise.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public boolean modify(E e) throws MyDBException {
        try {
            et.begin();
            em.merge(e);
            et.commit();
        } catch (OptimisticLockException o) {
            et.rollback();
            throw new MyDBException("JpaDao.modify(E e)", o.getMessage());
        }

        return true;
    }

    /**
     * Removes an entry from the database using it's Primary Key.
     *
     * @param pk The Primary Key of the entry to remove from the database.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public void remove(PK pk) throws MyDBException {
        E e = read(pk);
        try {
            et.begin();
            em.remove(e);
            et.commit();
        } catch (Exception ex) {
            et.rollback();
        }
    }

    /**
     * Looks for a specified entry in the database using a prop and it's
     * required value and returns it.
     *
     * @param prop a String representing the prop that needs to correspond to a
     * specified value.
     * @param value a String representing the value that the prop needs to have.
     * @return an Entity-Bean corresponding to the entry in the database if any
     * was found, null otherwise.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public E search(String prop, Object value) throws MyDBException {
        String namedQuery = cl.getSimpleName() + ".findBy" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
        Query query = em.createNamedQuery(namedQuery);
        query.setParameter(prop, value);
        E e = (E) query.getSingleResult();
        return e;
    }

    /**
     * Reads and return every entry of a specifed table from the database.
     *
     * @return a List of a specified Entity-Bean representing every entries of
     * this specified Entity from the database.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public List<E> readList() throws MyDBException {
        String namedQuery = cl.getSimpleName() + ".findAll";
        Query query = em.createNamedQuery(namedQuery);
        return query.getResultList();
    }

    /**
     * Clears a table's content by removing every of it's entries.
     *
     * @return a int representing the number of removed entry if the operation
     * was successful, 0 otherwise.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public int removeList() throws MyDBException {
        int r;
        try {
            et.begin();
            Query query = em.createNamedQuery(cl.getSimpleName() + ".delete");
            r = query.executeUpdate();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            r = 0;
        }
        return r;
    }

    /**
     * Adds a list of Entity-Bean inside the database.
     *
     * @param list a List of Entity-Bean representing the entries to add in the
     * database.
     * @return a int representing the number of added entry if the operation was
     * successful, 0 otherwise.
     * @throws MyDBException Throws this exception if something went wrong while
     * adding the Entity-Bean.
     */
    @Override
    public int saveList(List<E> list) throws MyDBException {
        int r = 0;
        try {
            et.begin();
            for (E e : list) {
                em.persist(e);
                r++;
            }
            et.commit();
        } catch (Exception ex) {
            et.rollback();
            r = 0;
        }
        return r;
    }

    /**
     * Returns the state of the connection to the database.
     *
     * @return a boolean, true if the application is connected to the database,
     * false otherwise.
     */
    @Override
    public boolean isConnected() {
        return emf.isOpen();
    }

    /**
     * Disconnects drom the database.
     */
    @Override
    public void disconnect() {
        em.close();
        emf.close();
    }

}
