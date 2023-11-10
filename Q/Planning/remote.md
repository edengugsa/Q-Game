## TO: Matthias Felleisen
## FROM: Alina Geng and Eden Gugsa
## CC: Michael Ballantyne
## DATE: 11/9/23
## SUBJECT: Referee-Server Protocol

The diagram below shows the interactions between our Referee (running on our server)
and external players while gathering players, launching a game, and reporting the result.
External Players will request to join a game by sending their JName. If accepted, the Referee
will send the starting map and tiles to the player. When turn taking happens, the Referee will 
follow the Sequences in Logical Interactions. If a Player cheats, then the Referee will stop communicating
with them. When a game ends, the Referee tells the remaining Players if they won or not.

The PlayerProxy represents an object that the Referee can interact with inplace of a player in order
to communicate over a remote connection. The RefereeProxy represents an object that the Player can 
interact with in place of a Referee. The Referee and Player will call methods on the other's Proxy 
to send information over tcp. The Proxys
convert the Referee and Player's information to Json to send to the other Proxy. The other Proxy will 
convert the Json to the component's internal data representations. This will allow two components to communicate
with one another without knowledge of each other's internal data representations.

### JSON Data Definitions:
JSetup is a JSON object. <br>
 { "tiles": [JTile...JTile],
    "map": JMap}
It represents the knowledge a Player knows when the game starts.

JMove is one of:
- JsonArray ["Placement", JPlacements]
- JsonArray ["Pass"]
- JsonArray ["Replace"]
Represents a Move that a Player can make.

JWin is a JSON object.<br>
{ "result": Boolean }
Where true means a player won and false means a player lost.


![Alt text](remoteInteractionIMG.jpg?raw=true "Title")