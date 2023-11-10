package Referee;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.RuleBook.BadAskForTilesRuleBook;
import Common.RuleBook.NoFitRuleBook;
import Common.RuleBook.NonAdjacentCoordinateRuleBook;
import Common.RuleBook.NotALineRuleBook;
import Common.RuleBook.RuleBook;
import Common.RuleBook.TileNotOwnedRuleBook;
import Common.State.ActivePlayerKnowledge;
import Common.State.GameState;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;
import Player.Strategy.DagStrategy;
import Player.Strategy.LdasgStrategy;
import Player.player;
import Player.playerImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class testJCheat {


  public Referee initRef() {
    List<player> players = new ArrayList<>();
    players.add(new playerImpl("Allen", new LdasgStrategy(new NonAdjacentCoordinateRuleBook())));
    players.add(new playerImpl("Bethany", new DagStrategy(new TileNotOwnedRuleBook())));
    return new Referee(players, new RuleBook());
  }

  @Test
  public void testJCheat() {
    List<player> players = new ArrayList<>();
    player allen = new playerImpl("Allen", new LdasgStrategy(new NonAdjacentCoordinateRuleBook()));
    player bethany = new playerImpl("Bethany", new DagStrategy(new TileNotOwnedRuleBook()));
    player cindy = new playerImpl("Cindy", new DagStrategy(new NotALineRuleBook()));
    player darryl = new playerImpl("Darryl", new DagStrategy(new NoFitRuleBook()));
    players.add(allen);
    players.add(bethany);
    Referee ref = new Referee(players, new RuleBook());

    // Non adjacent JCheat
    QGameCommand cmdAllen = allen.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacements = new ArrayDeque<>();
    expPlacements.add(new Placement(new Coordinate(0, -2), new Tile(QColor.BLUE, QShape.star)));
    QGameCommand expected = new PlacementCommand(expPlacements);
    assertEquals(expected.toString(), cmdAllen.toString());

    // Tile not owned Jcheat
    QGameCommand cmdBeth = bethany.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacementsBeth = new ArrayDeque<>();
    expPlacementsBeth.add(new Placement(new Coordinate(0, -1), new Tile(QColor.YELLOW, QShape.star)));
    QGameCommand expectedBeth = new PlacementCommand(expPlacementsBeth);
    assertEquals(expectedBeth.toString(), cmdBeth.toString());
    assertFalse(ref.getGameState().getActivePlayerKnowledge().getActivePlayerTiles().contains(new Placement(new Coordinate(0, -1), new Tile(QColor.YELLOW, QShape.star))));

    // Not in a line
    QGameCommand cmdCindy = cindy.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacementsCindy = new ArrayDeque<>();
    expPlacementsCindy.add(new Placement(new Coordinate(0, -1), new Tile(QColor.BLUE, QShape.eight_star)));
    expPlacementsCindy.add(new Placement(new Coordinate(0, -2), new Tile(QColor.BLUE, QShape.star)));
    QGameCommand expectedCindy = new PlacementCommand(expPlacementsCindy);
    assertEquals(expectedCindy.toString(), cmdCindy.toString());

    // Doesn't match neighbors
    QGameCommand cmdDarryl = darryl.takeTurn(ref.getGameState().getActivePlayerKnowledge());
    Queue<Placement> expPlacementsDarryl = new ArrayDeque<>();
    expPlacementsDarryl.add(new Placement(new Coordinate(0, -1), new Tile(QColor.BLUE, QShape.star)));
    QGameCommand expectedDarryl = new PlacementCommand(expPlacementsDarryl);
    assertEquals(expectedDarryl.toString(), cmdDarryl.toString());

  }

  @Test
  public void testJCheat2() {
    player allen = new playerImpl("Allen", new LdasgStrategy(new BadAskForTilesRuleBook()));
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
    player allen = new playerImpl("Allen", new LdasgStrategy(new BadAskForTilesRuleBook()));
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
}
