package Server;

public class StartQGame {
  public static void main(String[] args) {
    try {
      ServerReferee server = new ServerReferee(33331);
      server.run();
    }
    catch (Exception e) {
      System.out.println("Start QGame server failed");
    }
  }
}
