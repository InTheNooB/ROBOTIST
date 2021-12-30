package app.wrk.database;

import app.beans.Log;
import app.beans.Sequence;
import app.beans.User;
import app.exception.MyDBException;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfWrkDatabaseWrk {

    void connectDatabase() throws MyDBException;

    List<Log> readLogs() throws MyDBException;

    void addLog(Log l) throws MyDBException;

    Log readLog(int pk) throws MyDBException;

    void removeLog(Log l) throws MyDBException;

    List<User> readUsers() throws MyDBException;

    User readUser(int pk) throws MyDBException;

    void removeUser(User l) throws MyDBException;

    void addUser(User u) throws MyDBException;

    List<Sequence> readSequences() throws MyDBException;

    Sequence readSequence(int pk) throws MyDBException;

    void addSequence(Sequence s) throws MyDBException;

    void removeSequence(Sequence s) throws MyDBException;

    boolean isConnected();

    void disconnect();

}
