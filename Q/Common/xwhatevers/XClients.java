package Common.xwhatevers;

import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Client.ClientConfig;
import Client.client;
import Common.JsonToQGame;
import Common.TimeUtils;
import Player.player;

/**
 * Reads in a ClientConfig and launches players on different threads.
 */
public class XClients {
  public static void main(String[] args) {

    int port = Integer.parseInt(args[0]);
    JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    ClientConfig cc = JsonToQGame.ClientConfigJSONToClientConfig(parser.next().getAsJsonObject(), port);
    ExecutorService executor = Executors.newCachedThreadPool();

    for (player p : cc.getPlayers()) {
      client c = new client(cc.getHost(), cc.getPort(), p, cc.isQuiet());
      c.sendName();
      executor.execute(c::proxyRefereeReceiveInfoFromServer);
      TimeUtils.catchBreath(cc.getWait());
    }

    executor.shutdown();
    System.exit(0);
  }
}







