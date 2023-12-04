The commit we tagged for your submission is a96c0f3c631bd07fac49f9d19ada7f4562e7edc8.
**If you use GitHub permalinks, they must refer to this commit or your self-eval will be rejected.**
Navigate to the URL below to create permalinks and check that the commit hash in the final permalink URL is correct:

https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/tree/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8

## Self-Evaluation Form for Milestone 10

Indicate below each bullet which file/unit takes care of each task.

The data representation of configurations clearly needs the following
pieces of functionality. Explain how your chosen data representation 

- implements creation within programs _and_ from JSON specs 
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Server/server.java#L38-L50
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Common/Scorer/Scorer.java#L38-L51
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Referee/Referee.java#L49-L76
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Client/client.java#L27-L52
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Common/xwhatevers/XClients.java#L24-L33

- enforces that each configuration specifies a fixed set of properties (no more, no less)
  - We do not enforce the configuration properties.

- supports the retrieval of properties 
  - We did not make getters for server. We just retreive the fields of the configuration by directly accessing them. The values are final, therefore they cannot be altered.
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Referee/RefereeConfig.java#L33-L53
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Client/ClientConfig.java#L23-L42
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Common/Scorer/ScorerConfig.java#L15-L23

- sets properties (what happens when the property shouldn't exist?) 
  - We did not make setter methods for any the configuration classes. This is because the consr=tructor sets the fields for each class. We do not want to alter the configurations after they are set in the constructors.
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Client/ClientConfig.java#L16-L22
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Referee/RefereeConfig.java#L26-L32
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Common/Scorer/ScorerConfig.java#L11-L14
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Server/ServerConfig.java#L18-L25
  - If the properties should not exist...

- unit tests for these pieces of functionality
  - We do not have unit tests for this functionality.

Explain how the server, referee, and scoring functionalities are abstracted
over their respective configurations.
- Each component can be constructed by theur respective configurations.

Does the server touch the referee or scoring configuration, other than
passing it on?
  - No, it does not.
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Server/server.java#L85-L92
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Referee/Referee.java#L53

Does the referee touch the scoring configuration, other than passing
it on?
  - No, it does not.
 https://github.khoury.northeastern.edu/CS4500-F23/surprising-lions/blob/a96c0f3c631bd07fac49f9d19ada7f4562e7edc8/Q/Referee/Referee.java#L53

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

