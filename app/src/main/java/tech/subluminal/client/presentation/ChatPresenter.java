package tech.subluminal.client.presentation;

import java.util.Optional;
import tech.subluminal.shared.son.SONRepresentable;

/**
 * Handles the presentation of the chat.
 */
public interface ChatPresenter {

  /**
   * Fired when a someone sends a message to all users on the server.
   *
   * @param username from the sender of the message.
   * @param message is the text of the message.
   */
  void GlobalMessageReceived(String message, String username);

  /**
   * Fired when a personal message is received.
   *
   * @param message of the received whisper.
   * @param username of the sender.
   */
  void WhisperMessageReceived(String message, String username);

  /**
   * Fired when a message from the same game is received.
   *
   * @param message of the game message.
   * @param username fo the sender.
   */
  void GameMessageReceived(String message, String username);

  /**
   *
   * @param delegate
   */
  void setChatDelegate(Delegate delegate);

  public static interface Delegate{
    void sendGlobalMessage(String message);

    void sendGameMessage(String message);

    void sendWhisperMessage(String message, String username);

  }
}
