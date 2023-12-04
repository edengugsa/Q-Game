# TO: Matthias Felleisen
# FROM: Alina Geng and Eden Gugsa
# CC: Michael Ballantyne
# DATE: 12/03/23
# SUBJECT: Changes to client, server, referee, and scoring functionality

Task: After modifying the client, server, referee, and scoring functionality in your code, 
document the changes in configurations.md

1. We created new constructors:
- Client(ClientConfig cc)
- Server(ServerConfig sc)
- Referee(RefereeConfig rc)
- Scorer(ScorerConfig sc)

2. Prior to this milestone, we printed debug output to Standard out and would comment them out
we didn't want them. This milestone, we created a helper method that would print debug output
to standard error if the quiet flag was false in its component's config class.
3. We add a isQuiet field to Client, Server, and Referee
4. Our Server had final fields for server-tries, server-wait, wait-for-signup from the previous
milestone, but we got rid of final so that we could set their values in Server's constructor. 



