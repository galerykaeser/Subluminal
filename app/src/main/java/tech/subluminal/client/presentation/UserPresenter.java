package tech.subluminal.client.presentation;

import tech.subluminal.client.presentation.ChatPresenter.Delegate;
import tech.subluminal.shared.records.User;

/**
 * Presenter for the user interatcion.
 */
public interface UserPresenter {


  /**
   * Function that should be called when login succeeded.
   */
  void loginSucceeded();

  void setUserDelegate(Delegate delegate);

  /**
   * Delegate the UserStore can subscribe to.
   */
  public static interface Delegate {

    /**
     * Fired when a user has to be logged out.
     *
     */
    void logout();
  }

}
