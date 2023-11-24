The commit we tagged for your submission is 2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414.
**If you use GitHub permalinks, they must refer to this commit or your self-eval will be rejected.**
Navigate to the URL below to create permalinks and check that the commit hash in the final permalink URL is correct:

https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/tree/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414

## Self-Evaluation Form for Milestone 9

Indicate below each bullet which file/unit takes care of each task.

For `Q/Server/player`,

- explain how it implements the exact same interface as `Q/Player/player`<br>
Our ProxyPlayer (`Q/Server/player`) class implements our player interface
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Server/player.java#L33

- explain how it receives the TCP connection that enables it to communicate with a client<br>
Our ServerReferee calls accept() which returns a socket that we use to instantiate the PlayerProxy with.
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Server/ServerReferee.java#L51-L53
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Server/ServerReferee.java#L98

- point to unit tests that check whether it writes (proper) JSON to a mock output device<br>
we do not have that

For `Q/Client/referee`,

- explain how it implements the same interface as `Q/Referee/referee`<br>
It does not. 
Our /Client/referee receives method calls from the Server's referee and calls the appropriate method on the client.

- explain how it receives the TCP connection that enables it to communicate with a server
Our ClientPlayer (Q/Client/client) creates a new socket to the Server and instiates a new ProxyReferee (`Q/Client/referee`) with the socket.
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Client/ClientPlayer.java#L64
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Client/ClientPlayer.java#L43
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Client/referee.java#L39

- point to unit tests that check whether it reads (possibly broken) JSON from a mock input device<br>
we do not have unit tests.

For `Q/Client/client`, explain what happens when the client is started _before_ the server is up and running:

- does it wait until the server is up (best solution) <br> no
- does it shut down gracefully (acceptable now, but switch to the first option for 10)<br> no<br>
The client throws an exception saying "Could not join the server" 



For `Q/Server/server`, explain how the code implements the two waiting periods. <br>
We have a method called run() that collects players and runs an entire game. trySignup() runs at most two waiting periods, but will break early if it gets enough players the first time. Whenever a player requests to join, we call a helper caled signup() that waits 3 seconds for them to provide a name.
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Server/server.java#L110-L111
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Server/server.java#L69-L89
https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/2dc2ef1a5494c4a3bfab2daa0b7d4a9271863414/Q/Server/server.java#L39-L67

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

