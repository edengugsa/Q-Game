package Referee;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;



import Common.GameBoard.GameBoard;
import Common.JsonToQGame;
import Common.QGameToJson;
import Common.RuleBook.RuleBook;
import Common.State.GameState;
import Common.State.PlayerState;
import Common.State.PlayerStateImpl;
import Common.Tiles.Coordinate;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;
import Player.Strategy.DagStrategy;
import Player.Strategy.FlawedStrategy;
import Player.Strategy.LdasgStrategy;
import Player.player;
import Player.playerImpl;

import static org.junit.Assert.assertEquals;

public class testReferee {

  Referee ref;

  public void initRef() {
    List<player> players = new ArrayList<>();
    players.add(new playerImpl("Allen", new LdasgStrategy(new RuleBook())));
    players.add(new playerImpl("Bethany", new DagStrategy(new RuleBook())));
    players.add(new playerImpl("Cindy", new LdasgStrategy(new RuleBook())));
    this.ref = new Referee(players, new RuleBook());
  }

  public static Referee initRefWithObservers() {
    List<player> players = new ArrayList<>();
    players.add(new playerImpl("Allen", new LdasgStrategy(new RuleBook())));
    players.add(new playerImpl("Bethany", new DagStrategy(new RuleBook())));
    players.add(new playerImpl("Cindy", new LdasgStrategy(new RuleBook())));

    List<observer> observers = new ArrayList<>();
    observers.add(new observer());

    return new Referee(players, observers, new RuleBook());
  }

  public Referee initRefCheaters() {
    List<player> players = new ArrayList<>();
    players.add(new playerImpl("Allen", new FlawedStrategy()));
    players.add(new playerImpl("Bethany", new FlawedStrategy()));
    players.add(new playerImpl("Cindy", new FlawedStrategy()));
    return new Referee(players, new RuleBook());
  }

  public Referee initRefWithGameState(GameState gs) {
    List<player> players = new ArrayList<>();
    players.add(new playerImpl("Allen", new FlawedStrategy()));
    players.add(new playerImpl("Bethany", new FlawedStrategy()));
    players.add(new playerImpl("Cindy", new FlawedStrategy()));
    return new Referee(players, gs);
  }

  @Test
   public void initGS() {
    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));
    gameBoard.extend(new Coordinate(0, 1), new Tile(QColor.RED, QShape.diamond));
    gameBoard.extend(new Coordinate(0, 2), new Tile(QColor.RED, QShape.circle));
    gameBoard.extend(new Coordinate(0, 3), new Tile(QColor.RED, QShape.square));
    gameBoard.extend(new Coordinate(1, 0), new Tile(QColor.BLUE, QShape.clover));
    gameBoard.extend(new Coordinate(2, 0), new Tile(QColor.GREEN, QShape.clover));
    gameBoard.extend(new Coordinate(2, 1), new Tile(QColor.GREEN, QShape.star));
    System.out.println(QGameToJson.GameStateToJState(new GameState(initTestPlayers(), gameBoard)));
    assertEquals(0,0);
//    return new State(initTestPlayers(), gameBoard);
  }

  private static Queue<PlayerState> initTestPlayers() {
    List<PlayerState> players = new ArrayList<>();
    List<Tile> player1Tiles = new ArrayList<>();
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.clover));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.star));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.square));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.diamond));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.eight_star));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.circle));
    PlayerState player1 = new PlayerStateImpl("bob");
    player1.newTiles(player1Tiles);
    List<Tile> player2Tiles = new ArrayList<>();
    player2Tiles.add(new Tile(QColor.BLUE, QShape.square));
    PlayerState player2 = new PlayerStateImpl("a");
    player2.newTiles(player2Tiles);
    players.add(player1);
    players.add(player2);
    return new ArrayDeque<>(players);
  }

  @Test
  public void generateTests() {

  }

  @Test
  public void testRunGameResults() {
    WinnersAndCheaters output = this.ref.runGame();
    List<String> winners = new ArrayList<>();
    winners.add("Cindy");
    assertEquals(new WinnersAndCheaters(winners, new ArrayList<>()), output);
  }


  @Test
  public void testFailState() {
    Referee ref = this.initRefCheaters();
    WinnersAndCheaters res = ref.runGame();

    List<String> winners = new ArrayList<>();
    List<String> disqualified = new ArrayList<>();
    disqualified.add("Allen");
    disqualified.add("Bethany");
    disqualified.add("Cindy");
    assertEquals(new WinnersAndCheaters(winners, disqualified), res);
  }

  @Test
  public void testRefereeWithObservers() {
    this.initRefWithObservers();
    WinnersAndCheaters output = this.ref.runGame();
    List<String> winners = new ArrayList<>();
    winners.add("Cindy");

    assertEquals(new WinnersAndCheaters(winners, new ArrayList<>()), output);
  }


  public static void main(String[] args) {
    Referee ref = initRefWithObservers();
    WinnersAndCheaters output = ref.runGame();
    List<String> winners = new ArrayList<>();
    winners.add("Cindy");

  }


}
