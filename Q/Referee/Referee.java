package Referee;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
    if (players.size() < 2 || players.size() > 4) {
      throw new IllegalStateException("Must have 2-4 players to begin game.");
    }
    if (players.size() != game.numPlayers()) {
      throw new IllegalStateException("Players and State aren't consistent");
    }
    this.game = game;
    this.game.setNames(this.setNameToPlayersMap(players)); // Give the PlayerStates a name
    this.ruleBook = new RuleBook();
    this.scorer = new Scorer();
    this.disqualifiedPlayers = new ArrayList<>();
    this.observers = new ArrayList<>();
  }

  /**
   * Starts a QGame with a list of players and RuleBook.
   */
  public Referee(List<player> players, QRuleBook ruleBook) {
    if (players.size() < 2 || players.size() > 4) {
      throw new IllegalStateException("Must have 2-4 players to begin game.");
    }
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

  protected GameState getGameState() {
    return this.game.getCopy();
  }


  public Referee(List<player> players, List<observer> observers, QRuleBook ruleBook) {
    this(players, ruleBook);
    this.observers = observers;
  }

  /**
   * Runs a single game to completion according to the rules of The Q Game.
   */
  public WinnersAndCheaters runGame() {
    this.setupPlayersAndObservers();
    this.runGameHelper();
    this.tellObserversGameOver();
    WinnersAndCheaters winnersAndCheaters = this.generateResults();
    this.tellPlayersGameResult(winnersAndCheaters.winners);
    return winnersAndCheaters;
  }

  private void runGameHelper() {
    while (!ruleBook.gameOver(game)) {
      this.currentPlayerTakeTurn();
      sendObserversNewGameState(game.getCopy());
      if (ruleBook.gameOver(game)) {
        break;
      }
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
    //sendObserversNewGameState(game);
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
//        System.out.println(currentPlayer().name() + ": " + cmd);
        game.execute(cmd);
        game.score(cmd, this.scorer);
        if (game.currentPlayer().getHand().isEmpty()) {
          return;
        }
        game.renewPlayerTiles(cmd);
        updatePlayer();
        game.bump();
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
   * Tell Players if they won or not. If th winning player throws an exception, they're removed.
   */
  private void tellPlayersGameResult(List<String> winners) {
    List<String> names = new ArrayList<>(this.game.getPlayersNames());
    for (String name : names) {
      player player = this.players.get(name);
      try {
        player.win(winners.contains(player.name()));
      }
      catch (Exception e) {
        disqualify(name);
        winners.remove(name);
      }
    }
  }


  private WinnersAndCheaters generateResults() {
    List<String> winners = ruleBook.determineWinners(game.players());
    return new WinnersAndCheaters(winners, disqualifiedPlayers);
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
        o.setup(this.game.getCopy());
      }
      catch (Exception e) {
        this.observers.remove(o);
        System.out.println("Could not setup Observers: " + e.getMessage());
      }
    }
  }

  private void setupPlayers() {
    int numPlayers = this.game.numPlayers();
    for (int i = 0; i < numPlayers; i++) {
      try {
        player actualPlayer = this.players.get(game.currentPlayerName());
        actualPlayer.setup(game.getGameBoard(), game.currentPlayerTiles());
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
    disqualify(game.currentPlayerName());
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
