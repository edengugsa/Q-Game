package Server;

/**
 * Runs the Q Game on localhost:33331
 */
public class StartQGame {
  public static void main(String[] args) {
    try {
      ServerReferee server = new ServerReferee(33331);
      server.run();
      server.shutDown();
    }
    catch (Exception e) {
      System.out.println("Start QGame server failed");
    }
  }
}
