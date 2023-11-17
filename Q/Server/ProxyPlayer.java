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

  ProxyPlayer(Socket playerSocket) throws IOException {
    this.socket = playerSocket;
    this.readFromClientPlayer = new JsonStreamParser(new BufferedReader(new InputStreamReader(playerSocket.getInputStream())));
    this.writeToClientPlayer = new JsonWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
    try {
      this.playerName = this.readFromClientPlayer.next().toString();
    }
    catch (Exception e) {
      throw new IllegalStateException("ProxyPlayer::ProxyPlayer could not read name.");
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

//    System.out.println("Proxy Player setup: " + toSend);
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    JsonObject jPub = QGameToJson.ActivePlayerKnowledgeToJPub(apk);
    JsonArray toSend = QGameToJson.FormatJson("take-turn", new ArrayList<>(Arrays.asList(jPub)));
    this.sendToClient(toSend);

    //    System.out.println("take turn player QGameCommand: " + cmd);
    return JsonToQGame.jChoiceToQGameCommand(this.readFromClientPlayer.next());
  }

  @Override
  public void win(boolean win) {
    JsonArray toSend = QGameToJson.FormatJson("win", new ArrayList<>(Arrays.asList(QGameToJson.WinBooleanToJsonBool(win))));
    this.sendToClient(toSend);
//    System.out.println("win");
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    JsonArray toSend = QGameToJson.FormatJson("new-tiles", new ArrayList<>(Arrays.asList(QGameToJson.ListOfTilesToJsonArray(tiles))));
    this.sendToClient(toSend);
//    System.out.println("new tiles");
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
      throw new IllegalStateException("Could not sent message to player");
    }
  }
}
