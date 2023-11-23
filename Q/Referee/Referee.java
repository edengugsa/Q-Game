package Referee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import Common.GameCommands.QGameCommand;
import Common.QGameToJson;
import Common.RuleBook.QRuleBook;
import Common.RuleBook.RuleBook;
import Common.Scorer.Scorer;
import Common.State.GameState;
import Common.State.PlayerState;
import Common.State.PlayerStateImpl;
import Common.Tiles.Tile;
import Common.TimeUtils;
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

  static int PLAYER_RESPONSE_TIMEOUT = 6;

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
    this.ruleBook = new RuleBook();
    this.scorer = new Scorer();
    this.disqualifiedPlayers = new ArrayList<>();
    this.observers = new ArrayList<>();
    this.setNameToPlayersMap(players);
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
    this.createGameState(players);
    this.setNameToPlayersMap(players);
    System.out.println(QGameToJson.GameStateToJState(this.game));

  }

  public Referee(List<player> players) {
    this(players, new RuleBook());
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

  /**
   * Runs every player turn and sends the game state to observers after every turn.
   */
  private void runGameHelper() {
    while (!ruleBook.gameOver(game)) {
      currentPlayerTakeTurn();
      sendObserversNewGameState(game.getCopy());
      if (ruleBook.gameOver(game)) {
        break;
      }
    }
  }

  /**
   * The current player takes a turn. If the move is not legal or they took too long, they are
   * disqualified.
   */
  private void currentPlayerTakeTurn(){
    try {
      QGameCommand cmd = TimeUtils.callWithTimeOut(() -> currentPlayer().takeTurn(game.getActivePlayerKnowledge()), PLAYER_RESPONSE_TIMEOUT);
      if (cmd.isLegal(ruleBook, game)) {
        game.execute(cmd);
        game.score(cmd, this.scorer);
        if (game.currentPlayer().getHand().isEmpty()) {
          return;
        }
        this.game.renewPlayerTiles(cmd);
        this.updatePlayer(cmd);
        this.game.bump();
      }
      else {
        disqualify("taking too long on takeTurn()");
      }
    }
    catch (Exception e) {
      disqualify("throwing an exception or taking too long on takeTurn()");
    }
  }

  /**
   * Sends a new game state to the observers, kicking them out if they throw an exception.
   */
  private void sendObserversNewGameState(GameState newGs) {
    List<observer> observerCopy =  new ArrayList<>(observers);
    for (observer o : observerCopy) {
      try {
        o.receiveState(newGs);
      }
      catch (Exception e) {
        this.observers.remove(o);
//        System.out.println("Removed observer in send observer new game state");
      }
    }
  }

  /**
   * Informs the observers that the game is over, kicking them out if they throw an exception.
   */
  private void tellObserversGameOver() {
    for (observer o : observers) {
      try {
        o.gameOver();
      }
      catch (Exception e) {
        System.out.println("Removed observer in tell observer game over");
      }
    }
  }


  /**
   * Updates the AI player with their new tiles after their turn has been completed
   */
  private void updatePlayer(QGameCommand cmd) {
    if (cmd.doesPlayerGetNewTiles()) {
      try {
        int b = TimeUtils.callWithTimeOut(() -> tryCallingNewTilesOnPlayer(this.currentPlayer(), game.currentPlayerTiles()), PLAYER_RESPONSE_TIMEOUT);
      }
      catch (InterruptedException | ExecutionException | TimeoutException e) {
        throw new IllegalStateException("Player took too long on newTiles()");
      }
    }
  }

  private player currentPlayer() {
    return players.get(game.currentPlayerName());
  }

  /**
   * Informs remaining Players who lost first, then remaining winner(s). If the winning player(s)
   * throws an exception, they're removed from the game and winners list.
   */
  private void tellPlayersGameResult(List<String> winners) {
    this.tellPlayersGameResult(winners, false); //update losers
    this.tellPlayersGameResult(winners, true); //update winners
  }

  /**
   * Trys to inform a player that they won, kicking them out if they cheated in any way.
   */
  private int tryWin(player player, boolean didWin) {
    player.win(didWin);
    return 0;
  }

  /**
   * Trys to tell the players their game result and kicks them out if they take too long.
   */
  private void tellPlayersGameResult(List<String> winners, boolean didWin) {
    List<String> names = new ArrayList<>(this.game.getPlayersNames());
    for (String name : names) {
      player player = this.players.get(name);
      if (winners.contains(player.name()) == didWin) {
        try {
          int b = TimeUtils.callWithTimeOut(() -> tryWin(player, didWin), PLAYER_RESPONSE_TIMEOUT);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
          winners.remove(name);
          disqualify(name, " taking too long on win()");
        }
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
  private void setNameToPlayersMap(List<player> players) {
    this.players = new HashMap<>();
    for (player p : players) {
      this.players.put(p.name(), p);
    }
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
    List<observer> observerCopy =  new ArrayList<>(observers);
    for (observer o : observerCopy) {
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
        int b = TimeUtils.callWithTimeOut(() -> tryCallingSetupOnPlayer(actualPlayer), PLAYER_RESPONSE_TIMEOUT);
        game.bump();
      }
      catch (InterruptedException | ExecutionException | TimeoutException e) {
        disqualify("taking too long on setup()");
      }
    }
  }

  private int tryCallingSetupOnPlayer(player player) {
    player.setup(game.getActivePlayerKnowledge(), game.currentPlayerTiles());
    return 0;
  }

  private int tryCallingNewTilesOnPlayer(player player, List<Tile> tiles) {
    try {
      player.newTiles(tiles);
    }
    catch (Exception e) {
      disqualify("throwing exception on newTiles()");
    }
    return 0;
  }

  /**
   * Creates a GameState with the given list of players. The GameState will have a GameBoard
   * with a Tile and players with 6 Tiles each.
   */
  private void createGameState(List<player> players) {
    Queue<PlayerState> playerStates = new ArrayDeque<>();
    for (player p : players) {
      playerStates.add(new PlayerStateImpl(p.name()));
    }
    this.game = new GameState(playerStates);
    this.game.dealToAll(this.ruleBook.tilesPerPlayer());
  }

  /**
   * Disqualifies the current player. Goes to the next Player in State.
   */
  private void disqualify(String reason) {
    disqualify(game.currentPlayerName(), reason);
  }

  private void disqualify(String playerName, String reason) {
//    System.out.println("Disqualified " + game.currentPlayerName() + " for " + reason);
    game.addToRefDeck(game.getHand(playerName));
    disqualifiedPlayers.add(playerName);
    game.removePlayer(playerName);
  }





}
