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

public class XClients {
  public static void main(String[] args) {

    int port = Integer.parseInt(args[0]);
    JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    ClientConfig cc = JsonToQGame.ClientConfigJSONToClientConfig(parser.next().getAsJsonObject());
    ExecutorService executor = Executors.newCachedThreadPool();

    for (player p : cc.getPlayers()) {
      executor.execute(() -> {
        client c = new client(cc.getHost(), port, p, cc.isQuiet());
        c.run();
      });
      TimeUtils.catchBreath(cc.getWait());

    }

    executor.shutdown();
  }
}
