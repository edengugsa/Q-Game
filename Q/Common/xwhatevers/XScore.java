package Common.xwhatevers;

import com.google.gson.JsonStreamParser;
import Common.GameBoard.GameBoard;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import Common.JsonToQGame;
import Common.Tiles.Placement;

import Common.Scorer.Scorer;

/**
 * Consumes two JSON inputs from STDIN: a JMAP object and a JPlacements array. Computes the score (a natural
 * number) that this set of placements gathers.
 */
public class XScore {

    public static void main(String[] args) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    JsonStreamParser jsonStreamParser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    while (jsonStreamParser.hasNext()) {
      try {
        GameBoard gm = JsonToQGame.JMapToGameBoard(jsonStreamParser.next().getAsJsonArray());
        Queue<Placement> placements = JsonToQGame.JPlacementsToPlacements(jsonStreamParser.next().getAsJsonArray());
//        new GameBoardPainter(gm.getMap()).saveImage("png", "board");
        System.out.println(new Scorer().scorePlacement(placements, gm, false));
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    reader.close();
  }
}
