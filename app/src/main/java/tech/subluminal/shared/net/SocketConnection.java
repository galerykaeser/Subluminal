package tech.subluminal.shared.net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;
import org.pmw.tinylog.Logger;
import tech.subluminal.shared.son.SON;
import tech.subluminal.shared.son.SONConversionError;
import tech.subluminal.shared.son.SONConverter;
import tech.subluminal.shared.son.SONParsingError;
import tech.subluminal.shared.son.SONRepresentable;

/**
 * Represents a connection between two devices over a socket.
 */
public class SocketConnection implements Connection {

  private Socket socket;
  private Map<String, Set<Consumer<SON>>> handlers = new HashMap<>();
  private Set<Runnable> closeListeners = new HashSet<>();
  private PriorityBlockingQueue<String> messages = new PriorityBlockingQueue<>();
  private volatile boolean stop = false;
  private Thread readThread;
  private Thread writeThread;
  private Runnable onForceClose;

  /**
   * Creates a socket connection
   *
   * @param socket the socket to connect to.
   * @param onForceClose the action to execute when teh socket is closed forcefully.
   */
  public SocketConnection(Socket socket, Runnable onForceClose) {
    this.socket = socket;
    this.onForceClose = onForceClose;
  }

  /**
   * Creates a socket connection
   *
   * @param socket the socket to connect to.
   */
  public SocketConnection(Socket socket) {
    this(socket, () -> {
    });
  }

  private static <T extends SONRepresentable> void handleMessage(SONConverter<T> converter,
      Consumer<T> handler, SON son) {
    try {
      handler.accept(converter.convert(son));
    } catch (SONConversionError sonConversionError) {
      Logger.error(
          "Structure of packet packets was incorrect " + sonConversionError.getMessage());
    }
  }

  private void inStreamLoop() {
    try {
      Scanner scanner = new Scanner(socket.getInputStream());

      while (!stop) {
        try {
          //Get the next message and separate type and SON
          String message = scanner.nextLine();
          Logger.trace(message);
          String[] parts = message.split(" ", 2);
          if (parts.length == 2) {
            //parse in the message to SON
            SON son = SON.parse(parts[1]);
            Set<Consumer<SON>> consumers = handlers.get(parts[0]);
            if (consumers != null) {
              //if consumer does not exist create new one
              consumers.forEach(c -> c.accept(son));
            }
          }
        } catch (SONParsingError e) {
          //wrong format
          Logger.error("Parsing of failed: " + e.getMessage());
          Logger.error(e);
        } catch (NoSuchElementException e) {
          Logger.error("Socket was forcefully closed.");
          stop = true;
          onForceClose.run();
        }
      }
    } catch (IOException e) {
      Logger.error(e);
      System.exit(1);
    }
    closeListeners.forEach(Runnable::run);
  }

  /**
   * Registers a handler which is called when a provided type of SONRepresentable is received. A
   * call to this function should ideally look something like this:
   * <pre>
   *     connection.registerHandler(MyMessage.class, MyMessage::fromSON, this::handleMyMessage)
   * </pre>
   *
   * @param type the class of the type of SONRepresentable this handler responds to.
   */
  @Override
  public <T extends SONRepresentable> void registerHandler(Class<T> type, SONConverter<T> converter,
      Consumer<T> handler) {
    String method = type.getSimpleName();
    if (handlers.get(method) == null) {
      //new handler for SONRepresentable type
      handlers.put(method, new HashSet<>());
    }
    handlers.get(method).add(son -> handleMessage(converter, handler, son));
  }

  /**
   * Sends a message over the connection.
   *
   * @param message the message to send.
   */
  @Override
  public void sendMessage(SONRepresentable message) {
    String typeName = message.getClass().getSimpleName();
    String msg = message.asSON().asString();
    sendMessage(typeName, msg);
  }

  /**
   * Sends a message over the connection.
   *
   * @param type the type of the message.
   * @param message the message to send.
   */
  @Override
  public void sendMessage(String type, String message) {
    messages.add(type + " " + message);
  }

  private void outStreamLoop() {
    try {
      PrintStream out = new PrintStream(socket.getOutputStream());
      while (!stop) {
        out.println(messages.take());
      }
    } catch (IOException e) {
      throw new IllegalStateException("Could not get the output stream of a socket.");
    } catch (InterruptedException e) {
      // This is expected.
    }
  }

  /**
   * Adds a listener, which can react to this connection closing.
   *
   * @param listener will be run when the connection is closed.
   */
  @Override
  public void addCloseListener(Runnable listener) {
    closeListeners.add(listener);
  }

  /**
   * Tells the connection that it can start listening for messages.
   */
  @Override
  public void start() {
    readThread = new Thread(this::inStreamLoop);
    readThread.start();
    writeThread = new Thread(this::outStreamLoop);
    writeThread.start();
  }

  /**
   * Closes this stream and releases any system tech.subluminal.resources associated with it. If the
   * stream is already closed then invoking this method has no effect.
   *
   * <p>As noted in {@link AutoCloseable#close()}, cases where the close may fail require careful
   * attention. It is strongly advised to relinquish the underlying tech.subluminal.resources and to
   * internally <em>mark</em> the {@code Closeable} as closed, prior to throwing the {@code
   * IOException}.
   *
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void close() throws IOException {
    stop = true;
    readThread.interrupt();
    writeThread.interrupt();
    socket.close();
  }
}
