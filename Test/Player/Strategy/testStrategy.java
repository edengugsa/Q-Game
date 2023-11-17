package Player.Strategy;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


import Common.GameBoard.GameBoard;
import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.PassCommand;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.JsonToQGame;
import Common.QGameToJson;
import Common.RuleBook.RuleBook;
import Common.State.GameState;
import Common.State.PlayerState;
import Common.State.PlayerStateImpl;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

import static org.junit.Assert.assertEquals;

public class testStrategy {

  public static void main(String[] args) {
    GameState gs = initPlacement1();
//    gs.render();
    System.out.println(QGameToJson.HashMapToJMap(gs.getGameBoard().getMap()));
//    Queue<Placement> expectedPlacements = new ArrayDeque<>();
//    expectedPlacements.add(
//            new Placement(new Coordinate(3,1), new Tile(QColor.GREEN, QShape.diamond)));
//    QGameCommand expected = new PlacementCommand(expectedPlacements);
//    State gs2 = gs.execute(expected);
//    gs2.render();
  }

  static public GameState initPlacement1() {
    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));
    gameBoard.extend(new Coordinate(0, 1), new Tile(QColor.RED, QShape.diamond));
    gameBoard.extend(new Coordinate(0, 2), new Tile(QColor.RED, QShape.circle));
    gameBoard.extend(new Coordinate(0, 3), new Tile(QColor.RED, QShape.square));
    gameBoard.extend(new Coordinate(1, 0), new Tile(QColor.BLUE, QShape.clover));
    gameBoard.extend(new Coordinate(2, 0), new Tile(QColor.GREEN, QShape.clover));
    gameBoard.extend(new Coordinate(2, 1), new Tile(QColor.GREEN, QShape.star));

    return new GameState(initTestPlayers(), gameBoard);
  }

  static public GameState initPlacement2() {
    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));
    gameBoard.extend(new Coordinate(0, 1), new Tile(QColor.RED, QShape.diamond));
    gameBoard.extend(new Coordinate(0, 2), new Tile(QColor.RED, QShape.circle));
    gameBoard.extend(new Coordinate(0, 3), new Tile(QColor.RED, QShape.square));
    gameBoard.extend(new Coordinate(1, 0), new Tile(QColor.BLUE, QShape.clover));
    gameBoard.extend(new Coordinate(2, 0), new Tile(QColor.GREEN, QShape.clover));
    gameBoard.extend(new Coordinate(2, 1), new Tile(QColor.GREEN, QShape.star));

    return new GameState(initTestPlayers2(), gameBoard);
  }

  static public GameState initPlacement3() {
    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));
    gameBoard.extend(new Coordinate(0, 1), new Tile(QColor.RED, QShape.diamond));
    gameBoard.extend(new Coordinate(0, 2), new Tile(QColor.RED, QShape.circle));
    gameBoard.extend(new Coordinate(0, 3), new Tile(QColor.RED, QShape.square));
    gameBoard.extend(new Coordinate(1, 0), new Tile(QColor.BLUE, QShape.clover));
    gameBoard.extend(new Coordinate(2, 0), new Tile(QColor.GREEN, QShape.clover));
    gameBoard.extend(new Coordinate(2, 1), new Tile(QColor.GREEN, QShape.star));
    gameBoard.extend(new Coordinate(3, 0), new Tile(QColor.GREEN, QShape.diamond));


    return new GameState(initTestPlayers3(), gameBoard);
  }


  static public GameState initExchange() {
    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));

    return new GameState(initTestPlayersExchange(), gameBoard);
  }

  static public GameState initPass() {
    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));

    return new GameState(initTestPlayersExchange(), gameBoard, new ArrayDeque<Tile>(), new Stack<QGameCommand>());
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

  private static Queue<PlayerState> initTestPlayers2() {
    Queue<PlayerState> players = new ArrayDeque<>();
    List<Tile> player1Tiles = new ArrayList<>();
    player1Tiles.add(new Tile(QColor.PURPLE, QShape.clover));
    player1Tiles.add(new Tile(QColor.GREEN, QShape.star));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.square));
    player1Tiles.add(new Tile(QColor.BLUE, QShape.diamond));
    player1Tiles.add(new Tile(QColor.RED, QShape.eight_star));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.circle));

    PlayerState player1 = new PlayerStateImpl("bob");
    player1.newTiles(player1Tiles);
    List<Tile> player2Tiles = new ArrayList<>();
    player2Tiles.add(new Tile(QColor.BLUE, QShape.square));
    PlayerState player2 = new PlayerStateImpl("a");
    player2.newTiles(player2Tiles);
    players.add(player1);
    players.add(player2);
    return players;
  }

  private static Queue<PlayerState> initTestPlayers3() {
    Queue<PlayerState> players = new ArrayDeque<>();

    List<Tile> player1Tiles = new ArrayList<>();
    player1Tiles.add(new Tile(QColor.GREEN, QShape.diamond));
    PlayerState player1 = new PlayerStateImpl("bob");
    player1.newTiles(player1Tiles);

    List<Tile> player2Tiles = new ArrayList<>();
    player2Tiles.add(new Tile(QColor.BLUE, QShape.square));
    PlayerState player2 = new PlayerStateImpl("a");
    player2.newTiles(player2Tiles);

    players.add(player1);
    players.add(player2);
    return players;
  }


  private static Queue<PlayerState> initTestPlayersExchange() {
    Queue<PlayerState> players = new ArrayDeque<>();

    List<Tile> player1Tiles = new ArrayList<>();
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.diamond));
    player1Tiles.add(new Tile(QColor.GREEN, QShape.star));
    player1Tiles.add(new Tile(QColor.ORANGE, QShape.square));
    PlayerState player1 = new PlayerStateImpl("bob");
    player1.newTiles(player1Tiles);

    List<Tile> player2Tiles = new ArrayList<>();
    player2Tiles.add(new Tile(QColor.BLUE, QShape.square));
    PlayerState player2 = new PlayerStateImpl("a");
    player2.newTiles(player2Tiles);

    players.add(player1);
    players.add(player2);
    return players;
  }


  @Test
  public void testDagStrategyPlacements1() {
    GameState gs = initPlacement1();

    Strategy dag = new DagStrategy(new RuleBook());
    Queue<Placement> expectedPlacements = new ArrayDeque<>();
    expectedPlacements.add(
            new Placement(new Coordinate(3,1), new Tile(QColor.YELLOW, QShape.star)));
    expectedPlacements.add(
            new Placement(new Coordinate(4,1), new Tile(QColor.YELLOW, QShape.eight_star)));
    expectedPlacements.add(
            new Placement(new Coordinate(5,1), new Tile(QColor.YELLOW, QShape.square)));
    expectedPlacements.add(
            new Placement(new Coordinate(6,1), new Tile(QColor.YELLOW, QShape.circle)));
    expectedPlacements.add(
            new Placement(new Coordinate(7,1), new Tile(QColor.YELLOW, QShape.clover)));
    expectedPlacements.add(
            new Placement(new Coordinate(-1,1), new Tile(QColor.YELLOW, QShape.diamond)));
    QGameCommand expected = new PlacementCommand(expectedPlacements);
    Assert.assertEquals(expected.toString(), dag.compute(gs.getActivePlayerKnowledge()).toString());
  }

  @Test
  public void testDagStrategyPlacements2() {
    GameState gs = initPlacement2();

    Strategy dag = new DagStrategy(new RuleBook());
    Queue<Placement> expectedPlacements = new ArrayDeque<>();
    expectedPlacements.add(
            new Placement(new Coordinate(2,-1), new Tile(QColor.GREEN, QShape.star)));
    expectedPlacements.add(
            new Placement(new Coordinate(-1,-1), new Tile(QColor.RED, QShape.eight_star)));
    QGameCommand expected = new PlacementCommand(expectedPlacements);
    Assert.assertEquals(expected.toString(), dag.compute(gs.getActivePlayerKnowledge()).toString());
  }

  @Test
  public void testDagStrategyExchange() {
    GameState gs = initExchange();
    Strategy dag = new DagStrategy(new RuleBook());
    Assert.assertEquals(new ExchangeCommand(), dag.compute(gs.getActivePlayerKnowledge()));
  }

  @Test
  public void testDagStrategyPass() {
    GameState gs = initPass();
    Strategy dag = new DagStrategy(new RuleBook());
    Assert.assertEquals(new PassCommand(), dag.compute(gs.getActivePlayerKnowledge()));
  }

  @Test
  public void testLdasgStrategyPlacements() {
    GameState gs = initPlacement3();
    Strategy ldasg = new LdasgStrategy(new RuleBook());
    Queue<Placement> expectedPlacements = new ArrayDeque<>();
    expectedPlacements.add(
            new Placement(new Coordinate(3,1), new Tile(QColor.GREEN, QShape.diamond)));
    QGameCommand expected = new PlacementCommand(expectedPlacements);
    Assert.assertEquals(expected.toString(), ldasg.compute(gs.getActivePlayerKnowledge()).toString());
  }



}
