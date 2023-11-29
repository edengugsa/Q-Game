package Server;

import Referee.RefereeConfig;

/**
 * { "port"___________ : Natural (between 10000 and 60000),
 *
 *        { "server-tries"___ : Natural (less than 10),
 *
 *        { "server-wait"____ : Natural (less than 30[s]),
 *
 *        { "wait-for-signup" : Natural (less than 10),
 *
 *        { "quiet"__________ : Boolean,
 *
 *        { "ref-spec"_______ : RefereeConfig }
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
