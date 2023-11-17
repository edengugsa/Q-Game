package Client;

import java.net.Socket;
import java.util.List;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Player.Strategy.LdasgStrategy;
import Player.playerImpl;
import Player.player;


public class ClientPlayer implements player {
  Socket server;
  Player.player player;

  ClientPlayer(String ip, int port) {
    connectToSever(ip, port);
    this.player = new playerImpl("Alice", new LdasgStrategy());
    try {
      new ProxyReferee(this.server, this);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Cannot connect to proxy referee");
    }
  }

  private void connectToSever(String ip, int port) {
    try {
      this.server = new Socket(ip, port);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Could not join the server");
    }
  }

  @Override
  public String name() {
    return null;
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
