package Server;

import Referee.RefereeConfig;

public class ServerConfig {
  final int serverTries;
  final int serverWait;
  final int waitForSignup;
  final boolean quiet;
  final RefereeConfig refereeConfig;

  public ServerConfig(int serverTries, int serverWait, int waitForSignup, boolean quiet, RefereeConfig refereeConfig) {
    this.serverTries = serverTries;
    this.serverWait = serverWait;
    this.waitForSignup = waitForSignup;
    this.quiet = quiet;
    this.refereeConfig = refereeConfig;
  }
}
