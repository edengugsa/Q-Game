package Client;

import com.google.gson.JsonPrimitive;

import java.net.Socket;
import java.util.List;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Player.Strategy.LdasgStrategy;
import Player.Strategy.Strategy;
import Player.playerImpl;
import Player.player;


public class ClientPlayer implements player {
  Socket server;
  player player;
  ProxyReferee proxyReferee;

  public ClientPlayer(String ip, int port, String name) {
    connectToServer(ip, port);
    this.player = new playerImpl(name, new LdasgStrategy());
  }

  public ClientPlayer(String ip, int port, String name, Strategy strategy) {
    this(ip, port, name);
    this.player = new playerImpl(name, strategy);
  }

  public void run() {
    try {
      this.proxyReferee =  new ProxyReferee(this.server, this);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Could not create Proxy Referee " + e.getMessage());
    }
    try {
      this.proxyReferee.sendInformationToReferee(new JsonPrimitive(this.name()));
    }
    catch (Exception e) {
      throw new IllegalArgumentException("could not send name: " + e.getMessage());
    }
    try {
      this.proxyReferee.receiveInformationFromReferee();
    }
    catch (Exception e) {
      throw new IllegalArgumentException("could not receive information from the ref: " + e.getMessage());
    }
  }

  private void connectToServer(String ip, int port) {
    try {
      this.server = new Socket(ip, port);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Could not join the server");
    }
  }

  @Override
  public String name() {
    return this.player.name();
  }

  @Override
  public void setup(ActivePlayerKnowledge apk, List<Tile> hand) {
    this.player.setup(apk, hand);
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    return this.player.takeTurn(apk);
  }

  @Override
  public void win(boolean w) {
    this.player.win(w);
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    this.player.newTiles(tiles);
  }
}
