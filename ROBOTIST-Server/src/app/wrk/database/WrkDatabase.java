package app.wrk.database;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import static app.consts.Constants.DATABASE_PU;
import app.exception.MyDBException;
import java.util.List;

/**
 *
 * @author dingl01
 */
public class WrkDatabase implements ItfWrkDatabaseWrk {

    private JpaDaoItf<Log, Integer> logJpa;
    private JpaDaoItf<User, Integer> userJpa;
    private JpaDaoItf<Sequence, Integer> seqJpa;

    // *** DB *** //
    /**
     * Inits a connection to the database using the default persistence unit.
     *
     * @throws MyDBException Throw a custom exception if an error has occured
     * during the connection.
     */
    @Override
    public void connectDatabase() throws MyDBException {
        logJpa = new JpaDao(DATABASE_PU, Log.class);
        userJpa = new JpaDao(DATABASE_PU, User.class);
        seqJpa = new JpaDao(DATABASE_PU, Sequence.class);
    }

    /**
     * @return A boolean, true if the database is connected, false otherwise.
     */
    @Override
    public boolean isConnected() {
        boolean connected;
        connected = (logJpa != null) && (userJpa != null) && (seqJpa != null);
        connected = (connected) && (logJpa.isConnected()) && (userJpa.isConnected()) && (seqJpa.isConnected());
        return connected;
    }

    /**
     * Disconnects from the database.
     */
    @Override
    public void disconnect() {
        if (logJpa != null) {
            logJpa.disconnect();
        }
        if (userJpa != null) {
            userJpa.disconnect();
        }
        if (seqJpa != null) {
            seqJpa.disconnect();
        }
    }

    /**
     * Reads every log from the database.
     *
     * @return A List containing every Log of the database if the database is
     * connected, null if not.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public List<Log> readLogs() throws MyDBException {
        if (isConnected()) {
            return logJpa.readList();
        } else {
            return null;
        }
    }

    /**
     * Looks for a log in the database using it's Primary Key
     *
     * @param pk the Primary Key of the log.
     * @return A Log corresponding to the Primary Key if there's one, null
     * otherwise.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public Log readLog(int pk) throws MyDBException {
        if (isConnected()) {
            return logJpa.read(pk);
        } else {
            return null;
        }
    }

    /**
     * Removes a log from the database.
     *
     * @param l A Log to remove from the database.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public void removeLog(Log l) throws MyDBException {
        if (isConnected()) {
            logJpa.remove(l.getPkLog());
        }
    }

    /**
     * Adds a log to the database.
     *
     * @param l The log to add to the database.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public void addLog(Log l) throws MyDBException {
        if (isConnected()) {
            logJpa.create(l);
        }
    }

    /**
     * Looks for a user in the database using it's Primary Key
     *
     * @param pk The Primary Key of the user.
     * @return The User corresponding to the Primary Key if there's one, null
     * otherwise.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public User readUser(int pk) throws MyDBException {
        if (isConnected()) {
            return userJpa.read(pk);
        } else {
            return null;
        }
    }

    /**
     * Reads every user from the database.
     *
     * @return A List containing every User of the database if the database is
     * connected, null if not.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public List<User> readUsers() throws MyDBException {
        if (isConnected()) {
            return userJpa.readList();
        } else {
            return null;
        }
    }

    /**
     * Removes a user from the database.
     *
     * @param u The User to remove from the database.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public void removeUser(User u) throws MyDBException {
        if (isConnected()) {
            userJpa.remove(u.getPkUser());
        }
    }

    /**
     * Adds a user to the database.
     *
     * @param u The User to add to the database.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public void addUser(User u) throws MyDBException {
        if (isConnected()) {
            userJpa.create(u);
        }
    }

    /**
     * Reads every sequence from the database.
     *
     * @return a List containing every Sequence from the database if the database
     * is connected, null if not.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public List<Sequence> readSequences() throws MyDBException {
        if (isConnected()) {
            return seqJpa.readList();
        } else {
            return null;
        }
    }

    /**
     * Looks for a sequence in the database using it's Primary Key
     *
     * @param pk The Primary Key of the sequence.
     * @return The sequence corresponding to the Primary Key if there's one, null
     * otherwise.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public Sequence readSequence(int pk) throws MyDBException {
        if (isConnected()) {
            return seqJpa.read(pk);
        } else {
            return null;
        }
    }
    /**
     * Adds a sequence to the database.
     *
     * @param s The sequence to add to the database.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public void addSequence(Sequence s) throws MyDBException {
        if (isConnected()) {
            seqJpa.create(s);
        }
    }
    /**
     * Removes a sequence from the database.
     *
     * @param s The sequence to remove from the database.
     * @throws MyDBException Throw a custom exception if an error has occured.
     */
    @Override
    public void removeSequence(Sequence s) throws MyDBException {
        if (isConnected()) {
            seqJpa.remove(s.getPkSequence());
        }
    }

}
