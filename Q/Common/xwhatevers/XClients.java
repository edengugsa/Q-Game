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

/**
 * ./xserver 33331 < sample/4-server-config.json &
 *
 * ./xclients 33331 < sample/4-client-config.json  &
 */

public class XClients {
  public static void main(String[] args) {

    int port = Integer.parseInt(args[0]);
    JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    ClientConfig cc = JsonToQGame.ClientConfigJSONToClientConfig(parser.next().getAsJsonObject());
    ExecutorService executor = Executors.newCachedThreadPool();

      for (player p : cc.getPlayers()) {
        client c = new client(cc.getHost(), port, p, cc.isQuiet());
        c.sendName();
        executor.execute(c::proxyRefereeReceiveInfoFromServer);
        TimeUtils.catchBreath(cc.getWait());
      }

    executor.shutdown();
  }
}







