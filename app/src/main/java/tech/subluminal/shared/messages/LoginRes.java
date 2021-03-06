package tech.subluminal.shared.messages;

import tech.subluminal.shared.son.SON;
import tech.subluminal.shared.son.SONConversionError;
import tech.subluminal.shared.son.SONRepresentable;

/**
 * Represents the login response from the server to the client. This message, when converted to SON
 * and then to string, might look like this:
 * <pre>
 * {
 *   "userID":s"1111",
 *   "username":s"Patrick"
 * }
 * </pre>
 */
public class LoginRes implements SONRepresentable {

  public static final String USERNAME_KEY = "username";
  public static final String USER_ID_KEY = "userID";

  private String username;
  private String userID;

  /**
   * Creates response with the specified username and id.
   *
   * @param username the accepted username for the response.
   * @param id the id for the response.
   */
  public LoginRes(String username, String id) {
    this.username = username;
    this.userID = id;
  }

  /**
   * Creates a login request from a SON object.
   *
   * @param son the SON object to be converted to a login request.
   * @return the created login request.
   * @throws SONConversionError when the conversion fails.
   */
  public static LoginRes fromSON(SON son) throws SONConversionError {
    String username = son.getString(USERNAME_KEY)
        .orElseThrow(() -> new SONConversionError(
            "Login Response did not contain valid " + USERNAME_KEY + "."));
    String id = son.getString(USER_ID_KEY)
        .orElseThrow(() -> new SONConversionError(
            "Login Response did not contain valid " + USER_ID_KEY + "."));

    return new LoginRes(username, id);
  }

  /**
   * Gets the username from the response.
   *
   * @return the accepted client username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username for the response.
   *
   * @param username the accepted client username.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the username from the response.
   *
   * @return the accepted client id.
   */
  public String getUserID() {
    return userID;
  }

  /**
   * Sets the id for the response.
   *
   * @param id the accepted client id.
   */
  public void setUserID(String id) {
    this.userID = id;
  }

  /**
   * Creates a SON object representing this login response.
   *
   * @return the SON representation.
   */
  @Override
  public SON asSON() {
    return new SON()
        .put(username, USERNAME_KEY)
        .put(userID, USER_ID_KEY);
  }
}

