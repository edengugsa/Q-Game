package Player.Strategy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import Common.GameBoard.GameBoard;
import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.PassCommand;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.RuleBook.QRuleBook;
import Common.RuleBook.RuleBook;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * This abstract class represents a lexicographic approach to forming a QGameCommand. It will choose
 * the "smallest" tile in the player's hand that can extend the current GameBoard. Classes that
 * extend this will choose how to sort the "smallest" Tile's possible placements.
 * <p />
 * If no placements are possible according to the RuleBook, it will try to Exchange before Passing.
 *
 * Tiles are sorted by their Shape then Color.
 * The ordering of tile SHAPE is as follows (from smallest to largest);
 * "star", "8star", "square", "circle", "clover", "diamond"
 * The ordering of tile COLOR is as follows (from smallest to largest);
 * "red", "green", "blue", "yellow", "orange", "purple"
 */
public abstract class LexicoStrategy implements Strategy {
  protected QRuleBook ruleBook;
  protected ActivePlayerKnowledge currentState;
  protected List<Tile> playerTiles;
  protected Queue<Placement> placementsSoFar;
  protected GameBoard originalBoard;
  protected GameBoard mockBoard;


  /**
   * Constructs this LexicoStrategy which will adhere to the provided QRuleBook, and make decisions
   * based on the player's list of tiles and the player's knowledge about the game.
   */
  public LexicoStrategy() {
    this.ruleBook = new RuleBook();
    this.placementsSoFar = new ArrayDeque<>();
  }

  public LexicoStrategy(QRuleBook ruleBook) {
    super();
    this.ruleBook = ruleBook;
  }

  @Override
  public QGameCommand compute(ActivePlayerKnowledge apk) {
    this.configure(apk);
    this.computePlacements();

    if (placementsSoFar.size() > 0) {
      return new PlacementCommand(placementsSoFar);
    }
    else if (this.ruleBook.allows(new ExchangeCommand(),
            currentState.getNumRefTilesRemaining(), currentState.getNumPlayerTiles())) {
      return new ExchangeCommand();
    }
    else {
      return new PassCommand();
    }
  }

  /**
   * Tries to compute one Placement that adheres to the Q Game rules.
   * @throws IllegalStateException if tiles cannot be placed
   */
  private void computeOnePlacement() throws IllegalStateException {
    List<Tile> playerTiles = new ArrayList<>(this.ruleBook.getPlayerTiles(this.playerTiles));
    Collections.sort(playerTiles, Tile.TileComparator);
    for (Tile tile : playerTiles) {
      List<Placement> placementOptions = this.ruleBook.getPlacementOptions(tile, this.mockBoard);
      List<Coordinate> p = this.ruleBook.getMatchingCoordinates(placementOptions, this.mockBoard);
      if (p.size() > 0) {
        this.sort(p);
        Placement smallestPlacement = new Placement(p.get(0), tile);
        placementsSoFar.add(smallestPlacement);

//        if (this.ruleBook.allows(new PlacementCommand(placementsSoFar), this.originalBoard, this.playerTiles)) {
        if (this.ruleBook.allows(new PlacementCommand(placementsSoFar), this.currentState)) {
            this.playerTiles.remove(tile);
            try {
              mockBoard.extend(smallestPlacement.coordinate(), smallestPlacement.tile());
              return;
            }
            catch (Exception e) {
              // need this try catch for nonadjacent jcheat
            }

          }
          else {
            placementsSoFar.remove(smallestPlacement);
            if (this.placementsSoFar.size() > 0) {
              break;
            }
          }
        }
      }

    throw new IllegalStateException("Cannot place any tiles.");
  }





  /**
   * @return the longest sequence of Placements based on this Strategy.
   * Guarantees adherence to the rules provided to the class.
   * Does not guarantee at least one placement.
   */
  private void computePlacements() {
    this.placementsSoFar = new ArrayDeque<>();

    while (playerTiles.size() > 0 && this.placementsSoFar.size() < this.ruleBook.maxPlacementSize()) {
      try {
        computeOnePlacement();
      }
      catch (IllegalStateException e) {
        break;
      }
    }
  }

  /**
   * Sorts a list of potential coordinates based on this LexicoStrategy's sorting algorithm.
   */
  protected abstract List<Coordinate> sort(List<Coordinate> Coordinate);

  private void configure(ActivePlayerKnowledge apk) {
    this.currentState = apk;
    this.playerTiles = currentState.getActivePlayerTiles();
    this.playerTiles.sort(Tile.TileComparator);
    this.originalBoard = new GameBoard(apk.getBoard().getMap());
    this.mockBoard = new GameBoard(apk.getBoard().getMap());
  }



  protected final Comparator<Coordinate> CoordinateComparator =
          Comparator.comparing(Coordinate::row).thenComparing(Coordinate::col);

}
