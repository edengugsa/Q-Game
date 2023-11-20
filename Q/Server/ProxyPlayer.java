package Server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Common.GameCommands.QGameCommand;
import Common.JsonToQGame;
import Common.QGameToJson;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Common.TimeUtils;
import Player.player;

/**
 * Represents a proxy to a remote player that the ServerReferee uses.
 */
public class ProxyPlayer implements player {
  Socket socket; // used to send
  JsonStreamParser readFromClientPlayer;
  JsonWriter writeToClientPlayer;
  Gson gson = new Gson();
  String playerName;
  static final int RESPONSE_TIMEOUT = 6;

  ProxyPlayer(Socket playerSocket) {
    this.socket = playerSocket;
    try {
      this.readFromClientPlayer = new JsonStreamParser(new BufferedReader(new InputStreamReader(playerSocket.getInputStream())));
      this.writeToClientPlayer = new JsonWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
      this.playerName = this.readFromClientPlayer.next().getAsString();
    }
    catch (Exception e) {
      this.shutDown();
      // Unable to create a new ProxyPlayer because we didn't
//      throw new IllegalStateException("ProxyPlayer::ProxyPlayer could not read name.");
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

  /**
   * Did this player respond with "void" within the RESPONSE_TIMEOUT?
   */
  private void handlePlayerAcknowledgement() {
    try {
      String res = TimeUtils.callWithTimeOut(() -> this.readFromClientPlayer.next(), RESPONSE_TIMEOUT).getAsString();
      if (!res.equals("void")) { this.shutDown();}
    }
    catch(Exception ignored) {
      this.shutDown();
    }
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    JsonObject jPub = QGameToJson.ActivePlayerKnowledgeToJPub(apk);
    JsonArray toSend = QGameToJson.FormatJson("take-turn", new ArrayList<>(Arrays.asList(jPub)));
    this.sendToClient(toSend);

    //    System.out.println("take turn player QGameCommand: " + cmd);
    return this.getCommandFromClientPlayer();
  }

  /**
   * Returns this ProxyPlayer's client's requested QGameCommand. If the remote player doesn't
   * respond within the RESPONSE_TIMEOUT, their socket is shut down.
   */
  private QGameCommand getCommandFromClientPlayer() {
    try {
      JsonElement res = TimeUtils.callWithTimeOut(() -> this.readFromClientPlayer.next(), RESPONSE_TIMEOUT);
      return JsonToQGame.jChoiceToQGameCommand(res);
    }
    catch(Exception ignored) {
      this.shutDown();
    }
    throw new IllegalArgumentException("Shouldn't get here");
  }

  @Override
  public void win(boolean win) {
    JsonArray toSend = QGameToJson.FormatJson("win", new ArrayList<>(Arrays.asList(QGameToJson.WinBooleanToJsonBool(win))));
    this.sendToClient(toSend);
    this.handlePlayerAcknowledgement();
    this.shutDown();
  }

  protected void shutDown() {
    try {
      this.socket.close();
    }
    catch(IOException e) {
      System.out.println("Could not shut down communication with " + this.name());
    }
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    JsonArray toSend = QGameToJson.FormatJson("new-tiles", new ArrayList<>(Arrays.asList(QGameToJson.ListOfTilesToJsonArray(tiles))));
    this.sendToClient(toSend);
    this.handlePlayerAcknowledgement();
  }

  /**
   * Sends the given JsonElement to the player.
   */
  private void sendToClient(JsonElement toSend) {
    try {
      gson.toJson(toSend, this.writeToClientPlayer);
      this.writeToClientPlayer.flush();
    }
    catch (Exception e) {
      this.shutDown();
    }
  }
}
