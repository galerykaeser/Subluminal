package tech.subluminal.shared.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import tech.subluminal.shared.records.LobbyStatus;
import tech.subluminal.shared.son.SON;
import tech.subluminal.shared.son.SONConversionError;
import tech.subluminal.shared.son.SONParsingError;
import tech.subluminal.shared.stores.records.Lobby;
import tech.subluminal.shared.stores.records.LobbySettings;

public class LobbyJoinResTest {

  @Test
  public void testStringifyAndParsing() throws SONParsingError, SONConversionError {
    Lobby lobby = new Lobby("1729", new LobbySettings("Batman", "9000"), LobbyStatus.INGAME);
    LobbyJoinRes lobbyJoinRes = new LobbyJoinRes(lobby);
    Lobby parsedLobby = null;
    LobbyJoinRes parsedLobbyJoinRes = null;

    parsedLobbyJoinRes = LobbyJoinRes.fromSON(SON.parse(lobbyJoinRes.asSON().asString()));
    parsedLobby = parsedLobbyJoinRes.getLobby();

    assertNotNull(parsedLobbyJoinRes);
    assertEquals(lobby.getID(), parsedLobby.getID());
    assertEquals(lobby.getSettings().getName(), parsedLobby.getSettings().getName());
    assertEquals(lobby.getSettings().getAdminID(), parsedLobby.getSettings().getAdminID());
    assertEquals(lobby.getStatus(), parsedLobby.getStatus());
    System.out.println(lobbyJoinRes.asSON().asString());

    boolean parsingFailed = false;
    try {
      parsedLobbyJoinRes = LobbyJoinRes.fromSON(SON.parse(lobbyJoinRes.asSON().asString().replace("lobby", "LOBBY")));
    } catch (SONParsingError | SONConversionError e) {
      parsingFailed = true;
    }

    assertTrue(parsingFailed);
    }

}
