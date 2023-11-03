import java.util.List;

/**
 * Represents the rules for the game 'Qwirkle'.
 */
public interface QRuleBook {

  /**
   * @return true if this QRuleBook allows the given PlacementCommand to be executed on the current QGameBoardState.
   */
  boolean allows(PlacementCommand cmd, QGameBoardState board);

  /**
   * @return true if this QRuleBook allows an ExchangeCommand to be executed given the ActivePlayerKnowledge.
   */
   boolean allows(ExchangeCommand cmd, int numRefTiles, int numPlayerTiles);

  /**
   * @return true if this QRuleBook allows a PassCommand to be executed.
   */
   boolean allows(PassCommand cmd);

  /**
   * @return true if this RuleBook says the game is over.
   * A game is over if...
   *    * 1. all remaining players pass or replace their tiles
   *    * 2. a player has placed all tiles in its possession
   *    * 3. there are no players left after a turn
   */
  boolean gameOver (GameState g);

  boolean roundComplete(GameState game);

  /**
   * @return the winners of the game given the states of all players.
   */
  List<String> determineWinners(List<PlayerState> players);

  /**
   * @return the number of tiles dealt to each player at the start of the game.
   */
  int tilesPerPlayer();
}
