package app.ihm.panels;

import app.beans.User;
import java.util.List;

/**
 *
 * @author dingl01
 */
public interface ItfIhmUsersIhm {

     void getUserList();

     List<User> addUser(String username, String password);

     List<User> deleteUser(User u);

     void navigateBetweenIhm(String ihmName);

}
