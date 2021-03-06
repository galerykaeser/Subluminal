package tech.subluminal.shared.messages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tech.subluminal.client.stores.records.game.Player;
import tech.subluminal.shared.son.SON;
import tech.subluminal.shared.son.SONConversionError;
import tech.subluminal.shared.son.SONList;
import tech.subluminal.shared.son.SONRepresentable;
import tech.subluminal.shared.stores.records.game.Star;

/**
 * Contains the changes that need to be made to the game state. A GameStateDelta message, converted
 * to SON and then to string, might look like this:
 * <pre>
 * {
 *   "tps":d10.0,
 *   "players":l[
 *     o{
 *       "motherShip":o{
 *         "movable":o{
 *           "speed":d0.2,
 *           "target":s"3",
 *           "gameObject":o{
 *             "identifiable":o{
 *               "id":s"4321"
 *             },"coordinates":o{
 *               "x":d1.0,
 *               "y":d2.3
 *             }
 *           },"targets":l[
 *             s"1",
 *             s"2",
 *             s"3"
 *           ]
 *         }
 *       },
 *       "identifiable":o{
 *         "id":s"1234"
 *       },
 *       "fleets":l[
 *         o{
 *           "amount":i17,
 *           "movable":o{
 *             "speed":d0.2,
 *             "target":s"3",
 *             "gameObject":o{
 *               "identifiable":o{
 *                 "id":s"4321321"
 *               },
 *               "coordinates":o{
 *                 "x":d1.0,
 *                 "y":d2.3
 *               }
 *             },
 *             "targets":l[
 *               s"2",
 *               s"3"
 *             ]
 *           }
 *         },
 *         o{
 *           "amount":i17,
 *           "movable":o{
 *             "speed":d0.2,
 *             "target":s"3",
 *             "gameObject":o{
 *               "identifiable":o{
 *                 "id":s"4qd321"
 *               },
 *               "coordinates":o{
 *                 "x":d0.0,
 *                 "y":d2000.13
 *               }
 *             },
 *             "targets":l[
 *               s"1",
 *               s"2",
 *               s"3"
 *             ]
 *           }
 *         },
 *         o{
 *           "amount":i17,
 *           "movable":o{
 *             "speed":d0.2,
 *             "target":s"2",
 *             "gameObject":o{
 *               "identifiable":o{
 *                 "id":s"432gte1"
 *               },
 *               "coordinates":o{
 *                 "x":d1.0,
 *                 "y":d12.3
 *               }
 *             },
 *             "targets":l[
 *               s"2"
 *             ]
 *           }
 *         }
 *       ]
 *     },
 *     o{
 *       "motherShip":o{
 *         "movable":o{
 *           "speed":d0.2,
 *           "target":s"5",
 *           "gameObject":o{
 *             "identifiable":o{
 *               "id":s"4321"
 *             },
 *             "coordinates":o{
 *               "x":d10.0,
 *               "y":d2.3
 *             }
 *           },
 *           "targets":l[
 *           ]
 *         }
 *       },
 *       "identifiable":o{
 *         "id":s"2345"
 *       },
 *       "fleets":l[
 *       ]
 *     }
 *   ],
 *   "removedFleets":l[
 *     o{
 *       "key":s"1234",
 *       "value":l[
 *         s"645",
 *         s"123"
 *       ]
 *     },
 *     o{
 *       "key":s"4321",
 *       "value":l[
 *         s"345",
 *         s"134vj"
 *       ]
 *     }
 *   ],
 *   "stars":l[
 *     o{
 *       "possession":d1.0,
 *       "jump":d0.1,
 *       "generating":btrue,
 *       "name":s"Twinkle",
 *       "ownerID":s"1234",
 *       "gameObject":o{
 *         "identifiable":o{
 *           "id":s"starid"
 *         },
 *         "coordinates":o{
 *           "x":d0.0,
 *           "y":d0.0
 *         }
 *       }
 *     },
 *     o{
 *       "possession":d0.0,
 *       "jump":d0.3,
 *       "generating":bfalse,
 *       "name":s"TwinkleTwinkle",
 *       "gameObject":o{
 *         "identifiable":o{
 *           "id":s"starid2"
 *         },
 *         "coordinates":o{
 *           "x":d0.0,
 *           "y":d42.0
 *         }
 *       }
 *     }
 *   ],
 *   "removedPlayers":l[
 *     s"ftdq"
 *   ]
 * }
 * </pre>
 */
public class GameStateDelta implements SONRepresentable {

  private static final String CLASS_NAME = GameStateDelta.class.getSimpleName();

  private static final String PLAYERS_KEY = "players";
  private static final String TPS_KEY = "tps";
  private static final String STARS_KEY = "stars";
  private static final String REMOVED_MOTHER_SHIPS = "removedMotherShips";
  private static final String REMOVED_FLEETS_KEY = "removedFleets";
  private static final String KEY = "key";
  private static final String VALUE = "value";

  private List<Player> players = new LinkedList<>();
  private List<Star> stars = new LinkedList<>();
  private List<String> removedMotherShips = new LinkedList<>();
  private Map<String, List<String>> removedFleets = new HashMap<>();
  private double tps;

  /**
   * Converts a SON of the GameStateDelta back to the GameStateDelta type itself.
   *
   * @param son the SON representation of the GameStateDelta.
   * @return the GameStateDelta.
   * @throws SONConversionError if the conversion fails.
   */
  public static GameStateDelta fromSON(SON son) throws SONConversionError {
    GameStateDelta delta = new GameStateDelta();

    SONList starList = son.getList(STARS_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, STARS_KEY));

    for (int i = 0; i < starList.size(); i++) {
      int ii = i;
      SON star = starList.getObject(i)
          .orElseThrow(() -> SONRepresentable.error(STARS_KEY, Integer.toString(ii)));

      delta.addStar(Star.fromSON(star));
    }

    SONList playerList = son.getList(PLAYERS_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, PLAYERS_KEY));

    for (int i = 0; i < playerList.size(); i++) {
      int ii = i;
      SON player = playerList.getObject(i)
          .orElseThrow(() -> SONRepresentable.error(PLAYERS_KEY, Integer.toString(ii)));

      delta.addPlayer(Player.fromSON(player));
    }

    SONList removedShipsList = son.getList(REMOVED_MOTHER_SHIPS)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, REMOVED_MOTHER_SHIPS));

    for (int i = 0; i < removedShipsList.size(); i++) {
      int ii = i;
      String ship = removedShipsList.getString(i)
          .orElseThrow(() -> SONRepresentable.error(REMOVED_MOTHER_SHIPS, Integer.toString(ii)));

      delta.addRemovedMotherShip(ship);
    }

    SONList removedFleetsList = son.getList(REMOVED_FLEETS_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, REMOVED_FLEETS_KEY));

    double tps = son.getDouble(TPS_KEY)
        .orElseThrow(() -> SONRepresentable.error(CLASS_NAME, REMOVED_FLEETS_KEY));

    delta.setTps(tps);

    for (int i = 0; i < removedFleetsList.size(); i++) {
      int ii = i;
      SON playerFleets = removedFleetsList.getObject(i)
          .orElseThrow(() -> SONRepresentable.error(REMOVED_FLEETS_KEY, Integer.toString(ii)));
      String id = playerFleets.getString(KEY)
          .orElseThrow(() -> SONRepresentable.error(REMOVED_FLEETS_KEY + "[" + ii + "]", KEY));
      SONList fleets = playerFleets.getList(VALUE)
          .orElseThrow(() -> SONRepresentable.error(REMOVED_FLEETS_KEY + "[" + ii + "]", VALUE));
      for (int j = 0; j < fleets.size(); j++) {
        int jj = j;
        String fleet = fleets.getString(j)
            .orElseThrow(() -> SONRepresentable
                .error(REMOVED_FLEETS_KEY + "[" + ii + "]." + VALUE, Integer.toString(jj)));
        delta.addRemovedFleet(id, fleet);
      }
    }

    return delta;
  }

  /**
   * @return the server tick rate for this tick.
   */
  public double getTps() {
    return tps;
  }

  /**
   * @param tps the server tick rate for this tick.
   */
  public void setTps(double tps) {
    this.tps = tps;
  }

  /**
   * @return a list of the participating players.
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * @return a list of the stars that exist in the current game.
   */
  public List<Star> getStars() {
    return stars;
  }

  /**
   * @return Returns a list of the players that have to be removed from the game state.
   */
  public List<String> getRemovedMotherShips() {
    return removedMotherShips;
  }

  /**
   * Adds a player to the game.
   *
   * @param player the player to be added to the game.
   */
  public void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * Adds a star to the game.
   *
   * @param star the star to be added to the game.
   */
  public void addStar(Star star) {
    stars.add(star);
  }

  /**
   * Adds a mother ship to the list of the mother ships to be removed from the game.
   *
   * @param id the ID of the mother ship to be removed.
   */
  public void addRemovedMotherShip(String id) {
    removedMotherShips.add(id);
  }

  /**
   * @return a map containing the fleets of each player that need to be removed.
   */
  public Map<String, List<String>> getRemovedFleets() {
    return removedFleets;
  }

  /**
   * Adds a new fleet to the list to be removed.
   */
  public void addRemovedFleet(String playerID, String fleetID) {
    List<String> fleets = removedFleets.computeIfAbsent(playerID, k -> new LinkedList<>());
    fleets.add(fleetID);
  }

  /**
   * Creates a SON object representing this object.
   *
   * @return the SON representation.
   */
  @Override
  public SON asSON() {
    SONList playerList = new SONList();
    players.stream().map(Player::asSON).forEach(playerList::add);

    SONList starList = new SONList();
    stars.stream().map(Star::asSON).forEach(starList::add);

    SONList removedPlayersList = new SONList();
    removedMotherShips.forEach(removedPlayersList::add);

    SONList removedFleetsList = new SONList();
    removedFleets.keySet().forEach(k -> {
      SONList fleets = new SONList();
      removedFleets.get(k).forEach(fleets::add);
      removedFleetsList.add(
          new SON()
              .put(k, KEY)
              .put(fleets, VALUE)
      );
    });

    return new SON()
        .put(tps, TPS_KEY)
        .put(playerList, PLAYERS_KEY)
        .put(starList, STARS_KEY)
        .put(removedPlayersList, REMOVED_MOTHER_SHIPS)
        .put(removedFleetsList, REMOVED_FLEETS_KEY);
  }
}
