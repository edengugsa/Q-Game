package Referee;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.RuleBook.RuleBook;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;
import Player.PlayerSetupInf;
import Player.PlayerTakeTurnInf;
import Player.PlayerWinInf;
import Player.Strategy.BadAskForTilesStrategy;
import Player.Strategy.DagStrategy;
import Player.Strategy.LdasgStrategy;
import Player.Strategy.NoFitStrategy;
import Player.Strategy.NonAdjacentCoordinateStrategy;
import Player.Strategy.NotALineStrategy;
import Player.Strategy.TileNotOwnedStrategy;
import Player.player;
import Player.playerImpl;
import Player.playerNewTilesInf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class testJCheat {

  @Test
  public void testJCheat() {
    List<player> players = new ArrayList<>();
    player allen = new playerImpl("Allen", new NonAdjacentCoordinateStrategy(new LdasgStrategy()));
    player bethany = new playerImpl("Bethany", new TileNotOwnedStrategy(new DagStrategy()));
    player cindy = new playerImpl("Cindy", new NotALineStrategy(new DagStrategy()));
    player darryl = new playerImpl("Darryl", new NoFitStrategy(new DagStrategy()));
    players.add(allen);
    players.add(bethany);
    Referee ref = new Referee(players, new RuleBook());

    // Non adjacent JCheat
    QGameCommand cmdAllen = allen.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacements = new ArrayDeque<>();
    expPlacements.add(new Placement(new Coordinate(0, -2), new Tile(QColor.YELLOW, QShape.diamond)));
    QGameCommand expected = new PlacementCommand(expPlacements);
    assertEquals(expected.toString(), cmdAllen.toString());

    // Tile not owned Jcheat
    QGameCommand cmdBeth = bethany.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    assertFalse(ref.getGameState().getActivePlayerKnowledge().getActivePlayerTiles().contains(((PlacementCommand)cmdBeth).getPlacements().remove()));

    // Not in a line
    QGameCommand cmdCindy = cindy.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacementsCindy = new ArrayDeque<>();
    expPlacementsCindy.add(new Placement(new Coordinate(0, -1), new Tile(QColor.YELLOW, QShape.diamond)));
    expPlacementsCindy.add(new Placement(new Coordinate(1, 0), new Tile(QColor.BLUE, QShape.eight_star)));
    QGameCommand expectedCindy = new PlacementCommand(expPlacementsCindy);
    assertEquals(expectedCindy.toString(), cmdCindy.toString());

    // Doesn't match neighbors
    QGameCommand cmdDarryl = darryl.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacementsDarryl = new ArrayDeque<>();
    expPlacementsDarryl.add(new Placement(new Coordinate(0, -1), new Tile(QColor.ORANGE, QShape.square)));
    QGameCommand expectedDarryl = new PlacementCommand(expPlacementsDarryl);
    assertEquals(expectedDarryl.toString(), cmdDarryl.toString());
  }

  @Test
  public void testJCheat2() {
    player allen = new playerImpl("Allen", new BadAskForTilesStrategy(new LdasgStrategy()));
    List<Tile> allenTiles = new ArrayList<>();
    allenTiles.add(new Tile(QColor.GREEN, QShape.star));
    allenTiles.add(new Tile(QColor.GREEN, QShape.circle));
    allenTiles.add(new Tile(QColor.GREEN, QShape.circle));

    // Requests exchange
    ActivePlayerKnowledge apk = new ActivePlayerKnowledge(new ArrayDeque<>(), new GameBoard(new Tile(QColor.BLUE, QShape.star)),2, allenTiles);
    QGameCommand cmdAllen = allen.takeTurn(apk);
    assertEquals(new ExchangeCommand(), cmdAllen);
  }

  @Test
  public void testJCheat3() {
    player allen = new playerImpl("Allen", new BadAskForTilesStrategy(new LdasgStrategy()));
    List<Tile> allenTiles = new ArrayList<>();
    allenTiles.add(new Tile(QColor.BLUE, QShape.circle));
    allenTiles.add(new Tile(QColor.GREEN, QShape.circle));
    allenTiles.add(new Tile(QColor.GREEN, QShape.diamond));

    // Non adjacent JCheat
    ActivePlayerKnowledge apk = new ActivePlayerKnowledge(new ArrayDeque<>(), new GameBoard(new Tile(QColor.BLUE, QShape.star)),3, allenTiles);
    QGameCommand cmdAllen = allen.takeTurn(apk);
    Queue<Placement> exp = new ArrayDeque<>();
    exp.add(new Placement(new Coordinate(0, -1), new Tile(QColor.BLUE, QShape.circle)));
    exp.add(new Placement(new Coordinate(0, -2), new Tile(QColor.GREEN, QShape.circle)));
    exp.add(new Placement(new Coordinate(0, -3), new Tile(QColor.GREEN, QShape.diamond)));

    assertEquals(new PlacementCommand(exp).toString(), cmdAllen.toString());
  }

  @Test
  public void testInfPlayer1() {
    List<player> players = new ArrayList<>();
    player allen = new PlayerTakeTurnInf("Allen", new LdasgStrategy(), 1);
    player bethany = new PlayerSetupInf("Bethany", new LdasgStrategy(), 2);
    player cindy = new PlayerSetupInf("Cindy", new DagStrategy(), 1);
    player darryl = new PlayerWinInf("Darryl", new DagStrategy(), 1);

    players.add(allen);
    players.add(bethany);
    players.add(cindy);
    players.add(darryl);
    Referee ref = new Referee(players, new RuleBook());
    List<String> winners = new ArrayList<>(Arrays.asList("Bethany"));
    List<String> disqualified = new ArrayList<>(Arrays.asList("Cindy", "Allen", "Darryl"));

    WinnersAndCheaters expected = new WinnersAndCheaters(winners, disqualified);
    assertEquals(ref.runGame(), expected);
  }

  @Test
  public void testInfPlayer2() {
    List<player> players = new ArrayList<>();
    player allen = new PlayerTakeTurnInf("Allen", new LdasgStrategy(), 1);
    player bethany = new playerNewTilesInf("Bethany", new LdasgStrategy(), 2);
    player cindy = new PlayerSetupInf("Cindy", new DagStrategy(), 1);
    player darryl = new PlayerWinInf("Darryl", new DagStrategy(), 1);

    players.add(allen);
    players.add(bethany);
    players.add(cindy);
    players.add(darryl);
    Referee ref = new Referee(players, new RuleBook());
    List<String> winners = new ArrayList<>();
    List<String> disqualified = new ArrayList<>(Arrays.asList("Cindy", "Allen", "Bethany", "Darryl"));

    WinnersAndCheaters expected = new WinnersAndCheaters(winners, disqualified);
    assertEquals(ref.runGame(), expected);
  }
}
