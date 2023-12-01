package Common.xwhatevers;

import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Common.JsonToQGame;
import Common.QGameToJson;
import Server.ServerConfig;
import Server.server;

public class XServer {
  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);
    JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    ServerConfig serverConfig = JsonToQGame.ServerConfigJSONToServerConfig(parser.next().getAsJsonObject());
    server s = new server(port, serverConfig);
    System.out.println(QGameToJson.WinnersAndCheatersToJson(s.run()));
  }
}
