package Client;
import java.util.List;

import Player.player;

/**
 * Represents a configuration used by XClients to run players that play a QGame on a server.
 */
public class ClientConfig {
  final int port;
  final String host;
  final int wait;
  final boolean quiet;

  public ClientConfig(int port, String host, int wait, boolean quiet) {
    this.port = port;
    this.host = host;
    this.wait = wait;
    this.quiet = quiet;
  }
  public int getPort() {
    return port;
  }

  public String getHost() {
    return host;
  }

  public int getWait() {
    return wait;
  }

  public boolean isQuiet() {
    return quiet;
  }

}
