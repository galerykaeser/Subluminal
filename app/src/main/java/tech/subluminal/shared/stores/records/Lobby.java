package tech.subluminal.shared.stores.records;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import tech.subluminal.shared.son.SON;
import tech.subluminal.shared.son.SONConversionError;
import tech.subluminal.shared.son.SONList;
import tech.subluminal.shared.son.SONRepresentable;

/**
 * Represents a player lobby on the server.
 */
public class Lobby extends SlimLobby {

  private static final String CLASS_NAME = Lobby.class.getSimpleName();
  private static final String GAME_SPEED_KEY = "gameSpeed";
  private static final String MAP_SIZE_KEY = "mapSize";
  private static final String USERS_KEY = "users";
  private static final String KEY = "key";
  private static final String VALUE = "value";

  private Map<String, Boolean> users = new HashMap<>();
  //private String password;
  //private boolean pwProtected;
  //TODO: Make lobbies password protected

  // Game Settings
  private double gameSpeed = 1.0;
  private double mapSize = 2.0;

  /**
   * @param id is the ID of the lobby (generated by the lobby manager).
   * @param name is the common name of the lobby.
   * @param adminID points to the user who has created the lobby and can change its settings.
   */
  public Lobby(String id, String name, String adminID) {
    super(id, name, adminID);
    users.put(adminID, false);
    //this.pwProtected = false;
    //this.password = null;
  }

  public static Lobby fromSON(SON son) throws SONConversionError {
    Lobby lobby = fromSON(son, () -> new Lobby(null, null, null));
    lobby.setGameSpeed(son.getInt(GAME_SPEED_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, GAME_SPEED_KEY)));
    lobby.setMapSize(son.getInt(MAP_SIZE_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, MAP_SIZE_KEY)));
    SONList users = son.getList(USERS_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, USERS_KEY));

    for (int i = 0; i < users.size(); i++) {
      int ii = i;
      SON entry = users.getObject(i)
          .orElseThrow(() -> SONRepresentable.error(CLASS_NAME + "." + USERS_KEY, "" + ii));
      String id = entry.getString(KEY)
          .orElseThrow(
              () -> SONRepresentable.error(CLASS_NAME + "." + USERS_KEY, KEY + "[" + ii + "]"));
      Boolean ready = entry.getBoolean(VALUE)
          .orElseThrow(
              () -> SONRepresentable.error(CLASS_NAME + "." + USERS_KEY, VALUE + "[" + ii + "]"));

      lobby.addPlayer(id, ready);
    }
    return lobby;
  }

  public void addPlayer(String id, boolean ready) {
    users.put(id, ready);
  }

  public void setPlayerReady(String id, boolean ready) {
    users.replace(id, ready);
  }

  public void removePlayer(String id) {
    users.remove(id);
  }

  public int getPlayerCount() {
    return users.size();
  }

  public double getGameSpeed() {
    return gameSpeed;
  }

  public void setGameSpeed(double gameSpeed) {
    this.gameSpeed = gameSpeed;
  }

  public double getMapSize() {
    return mapSize;
  }

  public void setMapSize(double mapSize) {
    this.mapSize = mapSize;
  }

  public Set<String> getPlayers() {
    return users.keySet();
  }

  /**
   * Creates a SON object representing this object.
   *
   * @return the SON representation.
   */
  @Override
  public SON asSON() {
    SONList userList = new SONList();
    users.forEach((key, value) -> {
      userList.add(new SON().put(key, KEY).put(value, VALUE));
    });
    return super.asSON()
        .put(gameSpeed, GAME_SPEED_KEY)
        .put(mapSize, MAP_SIZE_KEY)
        .put(userList, USERS_KEY);
  }
}