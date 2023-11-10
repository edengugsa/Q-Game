The commit we tagged for your submission is f287a6a8f15e7c2d4d145025ea8802ea88a81581.
**If you use GitHub permalinks, they must refer to this commit or your self-eval will be rejected.**
Navigate to the URL below to create permalinks and check that the commit hash in the final permalink URL is correct:

https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/tree/f287a6a8f15e7c2d4d145025ea8802ea88a81581

## Self-Evaluation Form for Milestone 8

Indicate below each bullet which file/unit takes care of each task:

- concerning the modifications to the referee: 

  - is the referee programmed to the observer's interface
    or is it hardwired?
    - The referee has two constructors. One takes in a list of observers and one does not. It also has a field which is a list of observers. If observers are wanted, the constructor that takes in observers is used and sets the referees list of observers to the list of observers passed in. If the constructor without observers is used, the referee's observers is set as an empty list.
    - https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/f287a6a8f15e7c2d4d145025ea8802ea88a81581/Q/Referee/Referee.java#L60-L78

  - if an observer is desired, is every state per player turn sent to
    the observer? Where? 
    - Yes, every state per player turn is sent to the observer.
    - https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/f287a6a8f15e7c2d4d145025ea8802ea88a81581/Q/Referee/Referee.java#L102-L110

  - if an observer is not desired, how does the referee avoid calls to
    the observer?
    - If an oberserver is not desired, the referee is initialized with an empty list for the observers. This means when the referee sends the game states to the observers, there are no observers to send to and the loop will not run. The two types of referee constructors is linked below. If an observer is desired, the construtor that takes in a list of observers is used to initialize the referee's observers.
    - https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/f287a6a8f15e7c2d4d145025ea8802ea88a81581/Q/Referee/Referee.java#L60-L78
- concerning the implementation of the observer:

  - does the purpose statement explain how to program to the
    observer's interface? 
    - We did not explain how to program to the observer's interface. This is what was in our observer's purpose statement:  https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/f287a6a8f15e7c2d4d145025ea8802ea88a81581/Q/Referee/observer.java#L22-L34

  - does the purpose statement explain how a user would use the
    observer's view? Or is it explained elsewhere? 
    - We did not include very descriptive purpose statements explaining how a user would use the observer's view. We had the below purpse statement in the RenderObserverButtons class.
    - https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/f287a6a8f15e7c2d4d145025ea8802ea88a81581/Q/Common/Rendering/RenderObserverGameStates.java#L90-L98

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

