import org.junit.Test;

import java.util.*;

/**
 * This class reads in a JMap from stdin, renders it, and saves it as a png image.
 */
public class testDrawGameBoard {
  private static final JsonUtils utils = new JsonUtils();

//  public static void main(String[] args) throws IOException {
//
//    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//    JsonStreamParser jsonStreamParser = new JsonStreamParser(reader);
//    Queue<PlayerStateImpl> testPlayers = new ArrayDeque<>();
//
//    while (jsonStreamParser.hasNext()) {
//      try {
//        Map<Coordinate, Tile> map = utils.jMapToHashmap(jsonStreamParser.next().getAsJsonArray());
//        testPlayers = initTestPlayers();
//        GameState gamestate = new GameState(new ArrayDeque<>(initTestPlayers()), new GameBoard(map));
//        gamestate.render();
//
//      } catch (Exception e) {
//        System.out.println(e.getMessage());
//      }
//    }
//    reader.close();
//
//  }

  /**
   * Returns 2 Players for testing.
   */
  private static ArrayDeque<PlayerState> initTestPlayers() {
    List<PlayerState> players = new ArrayList();
    List<Tile> player1Tiles = new ArrayList();
    player1Tiles.add(new Tile(QColor.GREEN, QShape.diamond));
    player1Tiles.add(new Tile(QColor.RED, QShape.eight_star));
    player1Tiles.add(new Tile(QColor.PURPLE, QShape.star));
    player1Tiles.add(new Tile(QColor.YELLOW, QShape.square));
    player1Tiles.add(new Tile(QColor.GREEN, QShape.diamond));
    player1Tiles.add(new Tile(QColor.ORANGE, QShape.circle));
    PlayerState player1 = new PlayerStateImpl("bob");
    player1.newTiles(player1Tiles);

    List<Tile> player2Tiles = new ArrayList();
    player2Tiles.add(new Tile(QColor.BLUE, QShape.square));
    player2Tiles.add(new Tile(QColor.GREEN, QShape.star));
    player2Tiles.add(new Tile(QColor.RED, QShape.star));
    player2Tiles.add(new Tile(QColor.PURPLE, QShape.clover));
    player2Tiles.add(new Tile(QColor.GREEN, QShape.diamond));
    player2Tiles.add(new Tile(QColor.ORANGE, QShape.square));
    PlayerStateImpl player2 = new PlayerStateImpl("bob");
    player2.newTiles(player2Tiles);

    players.add(player1);
    players.add(player2);
    return new ArrayDeque<>(players);
  }

  @Test
  public void testGameStateRender() {

    GameBoard gameBoard = new GameBoard(new Tile(QColor.RED, QShape.clover));
    gameBoard.extend(new Coordinate(0, -1), new Tile(QColor.RED, QShape.eight_star));
    gameBoard.extend(new Coordinate(0, 1), new Tile(QColor.RED, QShape.diamond));
    gameBoard.extend(new Coordinate(0, 2), new Tile(QColor.RED, QShape.circle));
    gameBoard.extend(new Coordinate(0, 3), new Tile(QColor.RED, QShape.square));
    gameBoard.extend(new Coordinate(1, 0), new Tile(QColor.BLUE, QShape.clover));
    gameBoard.extend(new Coordinate(2, 0), new Tile(QColor.GREEN, QShape.clover));
    gameBoard.extend(new Coordinate(2, 1), new Tile(QColor.GREEN, QShape.star));
// TODO FIX
//    GameState gamestate = new GameState(initTestPlayers(), gameBoard);
//    gamestate.render();
  }
}
