package Client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import Common.DebugUtil;
import Common.GameCommands.QGameCommand;
import Common.JsonToQGame;
import Common.MName;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Player.player;

/**
 * Represents a proxy to the Referee for a client. This Proxy has an JsonStreamParser
 * to receive commands from a remote Referee and a JsonWriter to send its client's response.
 * <p>
 * It receives Jsons from the Referee and converts them to its data representations to forward to
 * its Client. It then converts the Client's responses into their Json representations to send back
 * to the Server's Referee.
 */
public class referee {
  JsonStreamParser readFromServerRef;
  JsonWriter writeToServerRef;
  Player.player player;
  Gson gson = new Gson();
  boolean isGameOver = false;
  boolean isQuiet;

  public referee(JsonStreamParser readFromServerRef,
  JsonWriter writeToServerRef, player player, boolean isQuiet) {
    this.readFromServerRef = readFromServerRef;
    this.writeToServerRef = writeToServerRef;
    this.player = player;
    this.isQuiet = isQuiet;
  }

  /**
   * Reads Methods Calls from the Referee, parses them, and calls the
   * appropriate method on the client.
   */
  protected void receiveInformationFromServer() {
    while(!isGameOver && this.readFromServerRef.hasNext()) {
      JsonArray methodCall = this.readFromServerRef.next().getAsJsonArray();
      MName mName = JsonToQGame.getMName(methodCall);
      switch (mName) {
        case SETUP -> this.callSetUpOnClientPlayer(methodCall);
        case TAKE_TURN -> this.callTakeTurnOnClientPlayer(methodCall);
        case NEW_TILES -> this.callNewTilesOnClientPlayer(methodCall);
        case WIN -> this.callWinOnClientPlayer(methodCall);
        default ->
                throw new IllegalArgumentException("Received invalid MethodCall from the Server");
      }
    }
  }

  protected void callTakeTurnOnClientPlayer(JsonArray methodCall) {
    ActivePlayerKnowledge apk = JsonToQGame.MethodCallToActivePlayerKnowledge(methodCall);
    QGameCommand cmd = this.player.takeTurn(apk);
    this.sendJsonToReferee(cmd.toJSON());
    DebugUtil.debug(this.isQuiet, player.name() + " took a turn");
  }

  protected void callSetUpOnClientPlayer(JsonArray methodCall) {
    ActivePlayerKnowledge apk = JsonToQGame.MethodCallToActivePlayerKnowledge(methodCall);
    List<Tile> tiles = JsonToQGame.SetupMethodCallToListOfTiles(methodCall);
    this.player.setup(apk, tiles);
    this.sendJsonToReferee(new JsonPrimitive("void"));
    DebugUtil.debug(this.isQuiet, player.name() + " setup");
  }

  protected void callNewTilesOnClientPlayer(JsonArray methodCall) {
    List<Tile> tiles = JsonToQGame.NewTilesMethodCallToListOfTiles(methodCall);
    this.player.newTiles(tiles);
    this.sendJsonToReferee(new JsonPrimitive("void"));
    DebugUtil.debug(this.isQuiet, player.name() + " got new tiles");
  }

  protected void callWinOnClientPlayer(JsonArray methodCall) {
    boolean winBool = JsonToQGame.WinMethodCallToListOfTiles(methodCall);
    this.player.win(winBool);
    this.sendJsonToReferee(new JsonPrimitive("void"));
    this.isGameOver = true;
  }

  public void sendJsonToReferee(JsonElement toSend) {
    try {
      DebugUtil.debug(true, this.player.name() + "sent: " + toSend);
      gson.toJson(toSend, this.writeToServerRef);
      this.writeToServerRef.flush();
    }
    catch (Exception e) {
      throw new IllegalStateException("Could not sent message to referee: " + e.getMessage());
    }
  }

}
