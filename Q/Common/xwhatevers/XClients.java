package Common.xwhatevers;

import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

    List<client> clients = new ArrayList<>(); // clients ordered by their turn
    for (player p : cc.getPlayers()) {
      client c = new client(cc.getHost(), port, p, cc.isQuiet());
      clients.add(c);
    }

    for (client c : clients) {
      System.out.println("looping through clients " + c.player.name());
      while (!c.isConnected()) {
        System.out.println("not connected " + c.player.name());
      }
      System.out.println("calling run " + c.player.name());
      executor.execute(c::run);
      System.out.println("finished calling run and going to start waiting " + c.player.name());
      TimeUtils.catchBreath(cc.getWait());
      System.out.println("done waiting " + c.player.name());
    }

    executor.shutdown();
  }
}
