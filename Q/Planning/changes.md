## List of Changes and their Predicted Implementation Difficulty

# Changing the bonus points again
- Difficulty: 1
- Reasoning: Our Referee class stores a Scorer object, which has methods for setting the number of points
rewarded. For instance, there's a public method in Scorer called setPTS_FOR_PLACING_ALL(int) which can change 
the default 6 pts to the given amount. This flexiblity
allows our Refereee to manipulate the amount of bonus points rewarded before the game starts.

# Allowing more players to participate in the game
- Difficulty: 1
- Reasoning: The only change required would be an update to the condition in the Referee's constructor,
which checks that the number of given players is between 2 and 4. None of our other classes are 
structurally obligated to serving at most 4 players, and can very easily handle more.

# Adding wildcard tiles
- Difficulty: 3
- Reasoning: This would require adding a boolean to our Tile class specifying if the tile is 'WILDCARD'
or not. Then, we'd have to alter deck generation in GameState in order to create a specific amount of these tiles.
Finally, our RuleBook's allowsPlacement() method would need an update to account for placing a WILDCARD 
(i.e. it supercedes the preexisting rules).

# Enforcing the rules of Qwirkle and not Q
- Difficulty: 2
- Reasoning: We abstracted and isolated the functionality for enforcing the rules in an interface called
QRuleBook. To allow this change, we'd need to write a new class that implements QRuleBook, enforces the rules of Qwirkle,
and pass that object to our Referee upon instantiation.