package Server;

import Referee.RefereeConfig;

/**
 * This ServerConfig specifies the port to listen to, for how many rounds the server waits,
 * for how long each waiting period lasts, for how long it waits for a player’s name,
 * and a RefereeConfig.
 */
public class ServerConfig {
  final int port;
  final int serverTries;
  final int serverWait;
  final int waitForSignup;
  final boolean quiet;
  final RefereeConfig refereeConfig;

  public ServerConfig(int port, int serverTries, int serverWait, int waitForSignup, boolean quiet, RefereeConfig refereeConfig) {
    this.port = port;
    this.serverTries = serverTries;
    this.serverWait = serverWait;
    this.waitForSignup = waitForSignup;
    this.quiet = quiet;
    this.refereeConfig = refereeConfig;
  }
}
