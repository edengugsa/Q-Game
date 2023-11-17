package Server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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

// implements player
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
    
  }


  @Override
  public String name() {
    try {
      String name = this.readFromClientPlayer.next().toString();
      return name;
    }
    catch (Exception e) {
      throw new IllegalStateException("proxy player name threw");
    }
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

    System.out.println(toSend);
  }

  private void sendToClient(JsonElement toSend) {
    gson.toJson(toSend, this.writeToClientPlayer);
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    this.sendToClient(QGameToJson.ActivePlayerKnowledgeToJPub(apk));
    QGameCommand cmd = JsonToQGame.jChoiceToQGameCommand(this.readFromClientPlayer.next());
    return cmd;
  }

  @Override
  public void win(boolean win) {
    this.sendToClient(QGameToJson.WinBooleanToJsonBool(win));
    System.out.println("win");
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    this.sendToClient(QGameToJson.ListOfTilesToJsonArray(tiles));
    System.out.println("new tiles");
  }
}
