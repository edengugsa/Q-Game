package Server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import Common.GameCommands.QGameCommand;
import Common.JsonToQGame;
import Common.QGameToJson;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Common.TimeUtils;

/**
 * Represents a proxy to a remote client that the Referee uses.
 * The Referee will call methods in the client interface on this player when needed. This
 * class will convert our data definitions to their Json counterpart and send it to the remote
 * client over its socket.
 */
public class player implements Player.player {
  private final Socket socket; // used to send
  private final JsonStreamParser readFromClientPlayer;
  private final JsonWriter writeToClientPlayer;
  private final Gson gson = new Gson();
  private final String playerName;
  static final int RESPONSE_TIMEOUT = 6;

  public player(Socket playerSocket) {
    this.socket = playerSocket;
    try {
      this.readFromClientPlayer = new JsonStreamParser(new BufferedReader(new InputStreamReader(playerSocket.getInputStream())));
      this.writeToClientPlayer = new JsonWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
    }
    catch (Exception e) {
      throw new IllegalStateException("couldn't set up input/output streams");
    }
    try {
      JsonReader reader = new JsonReader(new InputStreamReader(playerSocket.getInputStream()));
      this.playerName = JsonToQGame.JsonToJName(reader.nextString());
    }
    catch (Exception e) {
      throw new IllegalStateException("didn't receive player's name");
    }
  }

  @Override
  public String name() {
    return this.playerName;
  }

  /**
   * Send the ActivePlayer's Knowledge and Hand to this Proxy's outputstream.
   */
  @Override
  public void setup(ActivePlayerKnowledge apk, List<Tile> hand) {
    JsonObject jPub = QGameToJson.ActivePlayerKnowledgeToJPub(apk);
    JsonArray tiles = QGameToJson.ListOfTilesToJsonArray(hand);
    JsonArray toSend = QGameToJson.FormatJson("setup", new ArrayList<>(Arrays.asList(jPub, tiles)));
    this.sendToClient(toSend);
    this.handlePlayerAcknowledgement();
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    JsonArray toSend = QGameToJson.FormatJson("new-tiles",
            new ArrayList<>(List.of(QGameToJson.ListOfTilesToJsonArray(tiles))));
    this.sendToClient(toSend);
    this.handlePlayerAcknowledgement();
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    JsonObject jPub = QGameToJson.ActivePlayerKnowledgeToJPub(apk);
    JsonArray toSend = QGameToJson.FormatJson("take-turn", new ArrayList<>(List.of(jPub)));
    this.sendToClient(toSend);

    return this.getCommandFromClientPlayer();
  }

  /**
   * Returns this player's client's requested QGameCommand. If the remote client doesn't
   * respond within the RESPONSE_TIMEOUT, their socket is shut down.
   */
  private QGameCommand getCommandFromClientPlayer() {
    try {
      JsonElement res = TimeUtils.callWithTimeOut(this.readFromClientPlayer::next, RESPONSE_TIMEOUT);
      return JsonToQGame.jChoiceToQGameCommand(res);
    }
    catch(InterruptedException | ExecutionException | TimeoutException e) {
      throw new IllegalArgumentException("couldn't get command " + e.getMessage());
    }
  }

  @Override
  public void win(boolean win) {
    JsonArray toSend = QGameToJson.FormatJson("win", new ArrayList<>(List.of(QGameToJson.WinBooleanToJsonBool(win))));
    this.sendToClient(toSend);
    this.handlePlayerAcknowledgement();
  }

  /**
   * Did this client respond with "void" within the RESPONSE_TIMEOUT? If not, they are disqualified.
   */
  private void handlePlayerAcknowledgement() {
    try {
      String res = TimeUtils.callWithTimeOut(this.readFromClientPlayer::next, RESPONSE_TIMEOUT).getAsString();
      if (!res.equals("void")) { throw new IllegalArgumentException("invalid acknowledge Json");}
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Proxy did not receive acknowledgement in time");
    }
  }

  /**
   * Sends the given JsonElement to the client.
   */
  private void sendToClient(JsonElement toSend) {
    try {
      gson.toJson(toSend, this.writeToClientPlayer);
      this.writeToClientPlayer.flush();
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Could not send json message to client: " + e.getMessage());
    }
  }

  @Override
  public String toString() {
    return this.playerName;
  }
}
