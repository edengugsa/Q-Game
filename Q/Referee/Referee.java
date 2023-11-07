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
import Player.player;

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
  private GameState game;
  private Map<String, player> players;
  private final List<String> disqualifiedPlayers;
  private List<observer> observers;

  /**
   * FOR TESTING. Resumes a Game from a previous state.
   */
  public Referee(List<player> players, GameState game) {
    if (players.size() != game.numPlayers()) {
      throw new IllegalStateException("Players and State aren't consistent");
    }
    this.game = game;
    this.game.setNames(this.setNameToPlayersMap(players)); // Give the PlayerStates a name
    this.ruleBook = new RuleBook();
    this.scorer = new Scorer();
    this.disqualifiedPlayers = new ArrayList<>();
    this.observers = new ArrayList<>();
    this.setupPlayersAndObservers();
  }

  /**
   * Starts a QGame with a list of players and RuleBook.
   */
  public Referee(List<player> players, QRuleBook ruleBook) {
    this.ruleBook = ruleBook;
    this.scorer = new Scorer();
    this.disqualifiedPlayers = new ArrayList<>();
    this.observers = new ArrayList<>();
    this.setNameToPlayersMap(players);
    this.setGame();

  }

  public Referee(List<player> players, List<observer> observers, GameState game) {
    this(players, game);
    this.observers = observers;
  }


  public Referee(List<player> players, List<observer> observers, QRuleBook ruleBook) {
    this(players, ruleBook);
    this.observers = observers;
  }

  /**
   * Runs a single game to completion according to the rules of The Q Game.
   */
  public List<List<String>> runGame() {
    this.setupPlayersAndObservers();
    this.runGameHelper();
    this.tellObserversGameOver();
    List<List<String>> winnersAndCheaters = this.generateResults();
    this.tellPlayersGameResult(winnersAndCheaters.get(0));
    return winnersAndCheaters;
  }

  private void runGameHelper() {
    while (!ruleBook.gameOver(game)) {
      sendObserversNewGameState(game);
      this.currentPlayerTakeTurn();
      if (ruleBook.gameOver(game)) {
        break;
      }
      game.bump();
    }
  }

  private void sendObserversNewGameState(GameState newGs) {
    for (observer o : this.observers) {
      try {
        o.receiveState(newGs);
      }
      catch (Exception e) {
        this.observers.remove(o);
        System.out.println("Removed observer in send observer new game state");
      }
    }
  }

  private void tellObserversGameOver() {
    for (observer o : this.observers) {
      try {
        o.gameOver();
      }
      catch (Exception e) {
        System.out.println("Removed observer in tell observer game over");
      }
    }
  }


  private void currentPlayerTakeTurn(){
    try {
      QGameCommand cmd = currentPlayer().takeTurn(game.getActivePlayerKnowledge());
      if (cmd.isLegal(ruleBook, game)) {
        game.execute(cmd);
        game.score(cmd, this.scorer);
        updatePlayer();
      }
      else {
        disqualify();
      }
    }
    catch (Exception e) {
      disqualify();
    }
  }

  /**
   * Updates the AI player with their new tiles after their turn has been completed
   */
  private void updatePlayer() {
    this.currentPlayer().newTiles(game.currentPlayerTiles());
  }

  private player currentPlayer() {
    return players.get(game.currentPlayerName());
  }

  /**
   * Tell Players if they won or not. If a Player raises an exception, the winners will be
   * recalculated
   */
  private void tellPlayersGameResult(List<String> winners) {

    for (player p : this.players.values()) {
      try {
        p.win(winners.contains(p.name()));
      }
      catch (Exception e) {
        winners.remove(p.name());
        disqualify(p.name());
      }
    }
  }

  private List<List<String>> generateResults() {
    List<List<String>> results = new ArrayList<>();
    List<String> winners = ruleBook.determineWinners(game.players());
    Collections.sort(winners); // TODO Move to xgames
    results.add(winners);
    results.add(disqualifiedPlayers);
    return results;
  }

  /**
   * Sets this Referee's storage of player into a name->player map using the given list of player.
   */
  private List<String> setNameToPlayersMap(List<player> players) {
    this.players = new HashMap<>();
    List<String> names = new ArrayList<>();
    for (player p : players) {
      this.players.put(p.name(), p);
      names.add(p.name());
    }
    return names;
  }

  /**
   * Configures all Players with the initial game board and the tiles they own.
   * Configures all Observers with the initial GameState.
   */
  private void setupPlayersAndObservers() {
    this.setupPlayers();
    this.setupObservers();
  }

  private void setupObservers() {
    for (observer o : this.observers) {
      try {
        o.receiveState(this.game);
        System.out.println("calling startGui on ovserver");
        o.startGUI();
      }
      catch (Exception e) {
        this.observers.remove(o);
        System.out.println("Removed observer in setup");
      }
    }
  }

  private void setupPlayers() {
    for (player p : this.players.values()) {
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
    for (player p : this.players.values()) {
      playerStates.add(new PlayerStateImpl(p.name()));
    }
    this.game = new GameState(playerStates);
    game.dealToAll(ruleBook.tilesPerPlayer());
  }

  /**
   * Disqualifies the current player. Goes to the next Player in State.
   */
  private void disqualify() {
    game.addToRefDeck(game.currentPlayerTiles());
    disqualifiedPlayers.add(game.currentPlayerName());
    game.removeCurrentPlayer();
  }

  private void disqualify(String playerName) {
    game.addToRefDeck(game.getHand(playerName));
    disqualifiedPlayers.add(playerName);
    game.removePlayer(playerName);
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
