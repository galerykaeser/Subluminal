package tech.subluminal.server.logic;

//TODO: Imports

/**
 * Represents a player lobby on the server.
 */
public class Lobby {

  // Lobby properties
  private String id;
  private String name;
  private InMemoryLobbyUserStore store;
  private boolean allReady;
  private String adminID;
  private boolean pwProtected;
  private String password;

  // Game Settings
  private double gameSpeed = 1.0f;
  private double mapSize = 2.0f;
  private int minPlayers = 2;
  private int maxPlayers = 8;

  /**
   *
   * @param id is the ID of the lobby (generated by the lobby manager).
   * @param name is the common name of the lobby.
   * @param adminID points to the user who has created the lobby and can change its settings.
   */
  public Lobby(String id, String name, String adminID) {
    this.id = id;
    this.name = name;
    this.store = new InMemoryLobbyUserStore();
    this.allReady = false;
    this.adminID = adminID;
    this.pwProtected = false;
    this.password = null;
  }

  //TODO: Getters and setters
}
