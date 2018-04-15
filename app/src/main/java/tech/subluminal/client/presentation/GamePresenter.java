package tech.subluminal.client.presentation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import tech.subluminal.client.stores.records.game.Player;
import tech.subluminal.shared.stores.records.game.Star;

public interface GamePresenter {

  public void displayMap(Collection<Star> stars);

  public void updateStar(Collection<Star> stars);

  public void updateFleet(List<Player> players);

  public void addFleet(List<Player> players);

  public void removeFleet(
      Map<String, List<String>> removedFleets);

  public void updateMothership(List<Player> players);

  public void addMothership(List<Player> players);

  public void removeMothership(List<String> playerID);

  public void setGameDelegate(Delegate delegate);

  interface Delegate {

    public void sendShips(List<Star> stars, int amount);

    public void sendMothership(List<Star> star);
  }
}
