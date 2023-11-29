package Common.State;

import java.util.*;

import Common.GameBoard.GameBoard;
import Common.GameCommands.QGameCommand;
import Common.PublicKnowledge;
import Common.Rendering.RenderGameState;
import Common.RuleBook.QRuleBook;
import Common.RuleBook.RuleBook;
import Common.Scorer.Scorer;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * Represents the state of The Q Game. Provides functionality for initializing
 * the Game, querying the status, executing GameActions, and creating new GameStates.
 */
public class GameState {

  private final Queue<PlayerState> players;
  private GameBoard board;

  private Deque<Tile> deck; // dealer's deck of tiles
  private final QRuleBook rules = new RuleBook();
  public Stack<QGameCommand> roundHistory; // store all the commands in the current round
  public boolean playerPlacedAllTiles;


  /**
   * Public constructor for the State. Prepares the game for playing
   * by building the deck and the game board, and handing each client a given amount of tiles.
   * @param players the queue of players in turn order.
   */
  public GameState(Queue<PlayerState> players) throws IllegalStateException {
    this.players = players;
    buildDeck();
    buildBoard();
    this.roundHistory = new Stack<>();
  }

  /**
   * Private constructor used to produce a new State once a
   * QGameAction has been executed.
   */
  public GameState(Queue<PlayerState> players, GameBoard board,
                   Deque<Tile> deck, Stack<QGameCommand> history) {
    this(players, board, deck);
    this.roundHistory = history;
  }

  public GameState(Queue<PlayerState> players, GameBoard board,
                   Deque<Tile> deck) {
    this.players = players;
    this.board = board;
    this.deck = deck;
    this.roundHistory = new Stack<>();
  }

  /**
   * Executes a QGameCommand on this game state.
   */
  public void execute(QGameCommand command) {
    command.execute(this);
    this.roundHistory.add(command);
    this.currentPlayer().incrementTurnsCompleted();
  }



  public void score(QGameCommand command, Scorer scorer) {
    command.score(this, scorer);
  }

  public void takeTileFromActivePlayer(Tile t) {
    this.currentPlayer().removeTile(t);
  }

  /**
   * Bumps to the next client turn.
   * @return the active client name
   */
  public String bump() {
    players.add(players.remove());
    return currentPlayerName();
  }

  public void setNames(List<String> names) {
    for (String name : names) {
      currentPlayer().setName(name);
      bump();
    }
  }

  public void removeCurrentPlayer() {
    players.remove();
  }

  public int tilesRemaining() {
    return this.deck.size();
  }

  public Deque<Tile> getRefDeck() {
    ArrayDeque<Tile> copy  = new ArrayDeque<>(deck);
    return copy;
  }

  /**
   * @return a copy of this State's current PlayerStates.
   */
  public List<PlayerState> players() {
    return new ArrayList<>(this.players);
  }

  public GameBoard getGameBoard() {
    return new GameBoard(this.board.getMap());
  }

  /**
   * Updates the score of this State's current client with the given number of points.
   */
  public void updateScore(int points) {
    this.currentPlayer().reward(points);
  }

  /**
   * @return PublicPlayerKnowledge ->
   * (All the information about this State that the active client is allowed to know)
   */
  public PublicKnowledge publicKnowledge() {
    return new PublicKnowledge(
            new ArrayDeque<>(this.players),
            this.board,
            this.deck.size());
  }

  /**
   * @return ActivePlayerKnowledge ->
   * (All the information about this State that the active client is allowed to know)
   */
  public ActivePlayerKnowledge getActivePlayerKnowledge() {
    return new ActivePlayerKnowledge(
            new ArrayDeque<>(this.players),
            this.board,
            this.deck.size(),
            this.currentPlayer().getHand());
  }

  /**
   * Opens up a window displaying an image representing this State. The image shows
   * the current state of the map, all the players' scores and Tiles, and the number of Referee
   * tiles left.
   */
  public RenderGameState render() {
    RenderGameState renderGameState = new RenderGameState(this);
    renderGameState.setVisible(true);
    return renderGameState;
  }


  /**
   * @return the name of the Player whose turn it currently is.
   */
  public String currentPlayerName() {
    return this.currentPlayer().name();
  }

  /**
   * @return the map of this State's game board
   */
  public Map<Coordinate, Tile> board() {
    return this.board.getMap();
  }


  public void place(Queue<Placement> placements) {
    for (Placement p : placements) {
      this.currentPlayer().removeTile(p.tile());
      this.board.extend(p.coordinate(), p.tile());
    }
  }

  /**
   * @return the given amount of tiles from this State's deck
   * @throws IllegalStateException if the number of tiles in the deck is fewer than the requested amount
   */
  public List<Tile> getFromDeck(int amt) throws IllegalStateException {
    if (amt > deck.size()) {
      throw new IllegalStateException("Requested " + amt + " tiles from deck, " +
              "but deck only has " + deck.size() + " tiles remaining.");
    }
    else {
      List<Tile> tiles = new ArrayList<>();
      for (int i = 0; i < amt; i++) {
        tiles.add(this.deck.pop());
      }
      return tiles;
    }
  }

  /**
   * Renews the current Player's tiles after they requested a legal Placement Command. If they
   * placed more tiles than there are ref tiles, they get all the reftiles. If they placed all their
   * tiles, they don't get any tiles.
   *
   * @param numTiles the number of tiles placed by the current client
   */
  public void renewPlayerTiles(int numTiles) {
    playerPlacedAllTiles = this.currentPlayerTiles().size() == 0;
    if (!playerPlacedAllTiles && this.tilesRemaining() > 0) {
      this.currentPlayer().newTiles(this.getFromDeck(Math.min(numTiles, this.tilesRemaining())));
    }
  }


  /**
   * Builds the game board by placing the first tile in the deck.
   */
  private void buildBoard() {
    this.board = new GameBoard(this.deck.pop());
  }

  /**
   * Builds a shuffled deck of 1080 tiles containing 30 of each shape and color.
   */
  private void buildDeck() {
    List<Tile> deck = new ArrayList<>();
    for (QColor c : QColor.values()) {
      for (QShape s : QShape.values()) {
        for (int i = 0; i < 30; i++) {
          deck.add((new Tile(c, s)));
        }
      }
    }
    Collections.shuffle(deck, new Random(1)); // TODO REMOVE LATER
    this.deck = new ArrayDeque<>(deck);
  }

  /**
   * Deals the given amount of tiles to the current Player.
   */
  public void deal(int amt) {
    currentPlayer().newTiles(getFromDeck(amt));
  }

  /**
   * Deals the given amount of tiles to each PlayerState.
   */
  public void dealToAll(int amt) {
    for (PlayerState p : players) {
      this.deal(amt);
      this.bump();
    }
  }

  /**
   * @return a copy of the current client's tiles.
   */
  public List<Tile> currentPlayerTiles() {
    return new ArrayList<Tile>(this.currentPlayer().getHand());
  }
  
  public int numPlayers() {
    return this.players.size();
  }
  
  /**
   * @return the client whose turn it currently is.
   */
  public PlayerState currentPlayer() {
    return this.players.peek();
  }

  public List<Tile> getHand(String name) {
    for (PlayerState ps : this.players()) {
      if (ps.name().equals(name)) {
        return new ArrayList<>(ps.getHand());
      }
    }
    throw new IllegalArgumentException("No such client named: " + name);
  }

  public void removePlayer(String name) {
    for (PlayerState ps : this.players()) {
      if (ps.name().equals(name)) {
        this.players.remove(ps);
        return;
      }
    }
    throw new IllegalArgumentException("No such client: " + name);
  }

  /**
   * @return A copy of this GameState
   */
  public GameState getCopy() {
    return new GameState(this.getCopyOfPlayerStates(), this.getGameBoard(), new ArrayDeque<>(this.deck));
  }
  
  private Queue<PlayerState> getCopyOfPlayerStates() {
    Queue<PlayerState> copy = new ArrayDeque<>();
    for (PlayerState ps : this.players) {
      copy.add(new PlayerStateImpl(ps.name(), ps.getHand(), ps.score()));
    }
    return copy;
  }


  /**
   * Initializes a new State with Players and a GameBoard.
   * Used primarily in TESTING.
   */
  public GameState(Queue<PlayerState> players, GameBoard board) {
    this.players = new ArrayDeque<>();
    this.players.addAll(players);
    this.board = board;
    this.roundHistory = new Stack<>();
    buildDeck();
  }

  public List<String> getPlayersNames() {
    List<String> names = new ArrayList<>();
    for (PlayerState p : this.players) {
      names.add(p.name());
    }
    return names;
  }

  public void addToRefDeck(List<Tile> tiles) {
    this.deck.addAll(tiles);
  }

  public void renewPlayerTiles(QGameCommand cmd) {
    cmd.renewPlayerTiles(this);
  }

  public Deque<Tile> getDeck() {
    return new ArrayDeque<>(this.deck);
  }


}