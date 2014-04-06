package com.liliya.quiz.playerclient;

import java.util.List;

/**
 * Public interface for the player client
 */
public interface QuizPlayerClient {

    /**
     * Looking up and connecting to the remote service
     */

    public void connectToServer();

    /**
     * Displays menu and launches functionality for the player client
     */
    public void mainMenu();

    /**
     * Retrieves a list of currently available quizzes from the server
     * Displays list to client and accepts player choice
     *
     * @return the id of the quiz selected by the player;
     */

    public int selectQuizToPlay();

    /**
     * Takes a quiz id and launches a new quiz game for the player to play
     * Displays the total score achieved for the quiz at the end of the game
     *
     * @param id of the quiz
     */
    public void playQuiz(int id);

    /**
     * Displays the top three scores of the user for all quizzes played so far
     */
    public void viewHighScores();

    /**
     * Shuts down the player client process
     */
    public void closeDownProgram();


}
