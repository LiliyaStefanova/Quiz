##Quiz-Programming in Java Assignment III
--------------------------------------------
### Get Started

 1. Launching the server
-------------------------------------------
 - Run the QuizServerLauncher.java file

2. Launching the set up/administrator client
----------------------------------------------
 - Run the QuizSetUpClientImpl.java file. Run as many times as required for new clients

 - Choose option from menu as required

3. Launching the player client
-------------------------------------------------------------------------
 - Run the QuizPlayerClientImpl.java file for each new player

 - Choose option from menu as required

### Quiz Administration

The QuizSetUpClient is the administrator of the quiz. This client can create and close quizzes; and request that the
 server persists data and shuts down

**Available features:**

-**Manual quiz set up**-creates a new quiz based on data manually keyed in by the user

-**Quiz set up from file**-creates a new quiz based on data loaded from a file

-**Close a quiz**-closes and active quiz if no players are playing it; displays winner(s)

-**Save to file/ shut down server**- saves server state to file and shuts it down

-**Quit client**-closes down the client application

**Instructions for use:**

-**Manual quiz set up**-key in the data as requested; ensure correct type of information is provided

-**Quiz set up from file**-provide quiz name and absolute file path to the file. Current functionality accepts
    only csv files in the format: question,answer1,answer2,answer3,answer4,correctAnswerNumber,correctAnswerPoints. An
    exception will be thrown if the file path is incorrect or file does not exist/cannot be found

-**Close a quiz**-select a quizID from the list displayed of active quizzes. If a quiz is currently being played, it
   cannot be closed; the client will be asked to try again later.

-**Save to file/ shut down server**- the current server state will be saved to an xml file in the current directory and
   the server process shut down. When the server is restarted, data will be reloaded back from the file

-**Quit client**-Close down the client application;;the server will continue running if not shut down


### Quiz Playing

**Available features:**

-**Play quiz**-Provide a list of active quizzes to choose from; loads quiz selected; displays final score

-**View high scores**- Show the top 3 high scores or all scores(if less than 3) of the player

-**Quit**-Close down the client application

**Instructions for use:**

-**Play quiz**-key in the number of the quiz to play. A non-existent number will result in an error message; if a
   new client program is launched, player name will be requested.

-**View high scores**- the top three (or less if less than 3 games played) will be displayed. User will be asked for their
   name if a new client has been launched

-**Quit**-close down the client application