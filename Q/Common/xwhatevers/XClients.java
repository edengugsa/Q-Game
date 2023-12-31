package Common.xwhatevers;

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
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
    JsonObject JActorsB = parser.next().getAsJsonObject();

    ClientConfig cc = JsonToQGame.ClientConfigJSONToClientConfig(JActorsB, port);
    List<player> players = JsonToQGame.ClientConfigJSONToListPlayers(JActorsB.getAsJsonObject());
    ExecutorService executor = Executors.newFixedThreadPool(players.size());

    for (player p : players) {
      client c = new client(cc, p);
      executor.execute(c::run);
      TimeUtils.catchBreath(cc.getWait());
    }
    executor.shutdown();
  }
}







