package Referee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import Common.GameCommands.QGameCommand;
import Common.RuleBook.QRuleBook;
import Common.RuleBook.RuleBook;
import Common.Scorer.Scorer;
import Common.State.GameState;
import Common.State.PlayerState;
import Common.State.PlayerStateImpl;
import Player.playerImpl;

/**
 * A Referee operates the ongoings of a game, from setup/initialization to taking turns, and from
 * validating moves and determining the winner.
 * It will remove Players if they produce abnormal behavior. This includes:
 * 1. Requesting a GameCommand that violates the rules. This means the Player submitted a sequence
 *     of Placements that are invalid, or they request an Exchange when there aren't enough tiles
 *     left.
 * 2. Raising an error when this Referee calls one of their methods.
 * These checks will be implemented in Phase 2 when remote communication is implemented.
 * 3. Taking more than 2 minutes to run takeTurn()
 * 4. Requesting a GameCommand when it is not their turn
 * 5. Sending invalid Jsons or Jsons containing bad messages
 */
public class Referee {

  private final QRuleBook ruleBook;
  private final Scorer scorer;
  public GameState game; // TODO PRIVATE
  private Map<String, playerImpl> players;
  private final List<String> disqualifiedPlayers;

  /**
   * FOR TESTING. Resumes a Game from a previous state.
   */
  public Referee(List<playerImpl> players, GameState game) {
    if (players.size() != game.numPlayers()) {
      throw new IllegalStateException("Players and State aren't consistent");
    }
    this.game = game;
    this.game.setNames(this.setPlayers(players)); // Give the PlayerStates a name
    this.ruleBook = new RuleBook();
    this.scorer = new Scorer();
    this.disqualifiedPlayers = new ArrayList<>();
    this.configureAI();
  }

  public Referee(List<playerImpl> players, QRuleBook ruleBook) {
    if (players.size() != game.numPlayers()) {
      throw new IllegalStateException("Players and State aren't consistent");
    }
    this.ruleBook = ruleBook;
    this.scorer = new Scorer();
    this.disqualifiedPlayers = new ArrayList<>();
    this.setPlayers(players);
    this.setGame();
    this.configureAI();
  }

  /**
   * Runs a single game to completion according to the rules of The Q Game.
   */
  public List<List<String>> runGame() {
    this.runGameHelper();
    List<List<String>> winnersAndCheaters = this.generateResults();
    this.tellPlayersGameResult(winnersAndCheaters.get(0));
    return winnersAndCheaters;
  }

  private void runGameHelper() {
//    RenderGameState renderedGame = game.render();
//    catchBreath(0);
    while (!ruleBook.gameOver(game)) {
      try {
        this.currentPlayerTakeTurn();
        if (ruleBook.gameOver(game)) {
          break;
        }
        game.bump();
      }
      catch (Exception e) {
        disqualify();
      }
//      renderedGame.repaint();
//      catchBreath(1);
    }
//    renderedGame.repaint();
  }

  private void currentPlayerTakeTurn() throws Exception {
    QGameCommand cmd = currentPlayer().takeTurn(game.getActivePlayerKnowledge());
    if (cmd.isLegal(ruleBook, game)) {
      game.execute(cmd);
      game.score(cmd, this.scorer);
      updateAI();
    }
    else {
      disqualify();
    }
  }

  /**
   * Updates the AI player with their new tiles after their turn has been completed
   */
  private void updateAI() {
    this.currentPlayer().newTiles(game.currentPlayerTiles());
  }

  private playerImpl currentPlayer() {
    return players.get(game.currentPlayerName());
  }

  /**
   * Tell AIPlayers if they won or not.
   */
  private void tellPlayersGameResult(List<String> winners) {
    for (playerImpl p : this.players.values()) {
      try {p.win(winners.contains(p.name()));}
      catch (Exception e) {
//        winners.remove(p.name()); TODO: is a Player who throws in win() still considered a winner?
      }
    }
  }

  private List<List<String>> generateResults() {
    List<List<String>> results = new ArrayList<>();
    List<String> winners = ruleBook.determineWinners(game.players());
    Collections.sort(winners);
    results.add(winners);
    results.add(disqualifiedPlayers);
    return results;
  }

  /**
   * Sets this Referee's storage of player into a name->player map using the given list of player.
   */
  private List<String> setPlayers(List<playerImpl> players) {
    this.players = new HashMap<>();
    List<String> names = new ArrayList<>();
    for (playerImpl p : players) {
      this.players.put(p.name(), p);
      names.add(p.name());
    }
    return names;
  }

  /**
   * Configures all AIPlayers with the initial game board and the tiles they own.
   */
  private void configureAI() {
    for (playerImpl p : this.players.values()) {
      try {
        p.setup(game.getGameBoard(), game.currentPlayerTiles());
        game.bump();
      }
      catch (Exception e) {
        disqualify();
      }
    }
  }

  /**
   * Builds this Referee's representation of the State
   * This must be called after the createPlayers() method has been called.
   */
  private void setGame() {
    Queue<PlayerState> playerStates = new ArrayDeque<>();
    for (playerImpl p : this.players.values()) {
      playerStates.add(new PlayerStateImpl(p.name()));
    }
    this.game = new GameState(playerStates);
    game.dealToAll(ruleBook.tilesPerPlayer());
  }

  /**
   * Disqualifies the current player. Goes to the next Player in State.
   */
  private void disqualify() {
    disqualifiedPlayers.add(game.currentPlayerName());
    game.removeCurrentPlayer();
  }

  /**
   * Pauses runtime for a given number of seconds.
   */
  private void catchBreath(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    }
    catch (Exception e) {
      e.getMessage();
    }
  }

}
