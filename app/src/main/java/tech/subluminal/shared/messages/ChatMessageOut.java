package tech.subluminal.shared.messages;

import tech.subluminal.shared.son.SON;
import tech.subluminal.shared.son.SONConversionError;
import tech.subluminal.shared.son.SONRepresentable;

/**
 * The ChatMessage object which was built by the client for the server. A ChatMessageOut message, converted to SON and then to
 * string, might look like this:
 * <pre>
 *  {
 *    "global":bfalse,
 *    "message":s"Who let the dogs out?"
 *  }
 * </pre>
 * or like this:
 * <pre>
 * {
 *   "receiverID":s"1234",
 *   "message":s"Who let the dogs out?"
 * }
 * </pre>
 */
public class ChatMessageOut implements SONRepresentable {

  private static final String MESSAGE_KEY = "message";
  private static final String GLOBAL_KEY = "global";
  private static final String RECEIVER_ID_KEY = "receiverID";
  private static final String CLASS_NAME = ChatMessageOut.class.getSimpleName();
  private String message;
  private String receiverID;
  private boolean global;

  /**
   * ChatMessage object which is built by the sender.
   *
   * @param message is the text to send.
   * @param global defines if a message is for the whole server or ingame.
   */
  public ChatMessageOut(String message, String receiverID, boolean global) {
    this.message = message;
    this.global = global;
    this.receiverID = receiverID;
  }

  /**
   * Creates a ChatMessageOut from a SON object.
   *
   * @param son which gets converted to the ChatMessageOut.
   * @return the ChatMessageOut object.
   * @throws SONConversionError when conversion fails.
   */
  public static ChatMessageOut fromSON(SON son) throws SONConversionError {
    String message = son.getString(MESSAGE_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, MESSAGE_KEY));

    String receiverID = son.getString(RECEIVER_ID_KEY)
        .orElse(null);

    boolean global = receiverID != null ? false : son.getBoolean(GLOBAL_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, GLOBAL_KEY));

    return new ChatMessageOut(message, receiverID, global);

  }

  /**
   * @return the chatmessage.
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return the ID of the receiver of the message
   */
  public String getReceiverID() {
    return receiverID;
  }

  /**
   * @return true if a chat message is global, and false otherwise.
   */
  public boolean isGlobal() {
    return global;
  }

  /**
   * Creates a SON object representing the ChatMessageOut.
   *
   * @return the SON representation.
   */
  @Override
  public SON asSON() {
    SON son = new SON().put(message, MESSAGE_KEY);
    if (receiverID == null) {
      return son.put(global, GLOBAL_KEY);
    } else {
      return son.put(receiverID, RECEIVER_ID_KEY);
    }
  }
}
