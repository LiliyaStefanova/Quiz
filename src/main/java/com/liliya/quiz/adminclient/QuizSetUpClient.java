package com.liliya.quiz.adminclient;

import com.liliya.quiz.model.PlayerQuizInstance;

/**
 * Public interface for the set up client
 */

public interface QuizSetUpClient {

    /**
     * Look up the remote service
     */
    public void connectToServer();

    /**
     * Displays menu with options for actions the set up user can perform
     */
    public void menu();

    /**
     * Provides a name and list of questions for the quiz, calls the generateQuiz method on server
     * and generates a new quiz
     * Gets the id back
     */
    public void setUpQuizManually();

    /**
     * Generates a collection of questions based on data provided in a csv file
     * Gets id back from the server
     *
     */
    public void setUpQuizFromFile();
    /**
     * Closes the quiz when the the set up client provides the id
     *
     * @param quizID will display a notification of player with highest score for this game instance
     */

    public void requestQuizClose(int quizID);

    /**
     * takes in the value returned from the server after the quiz is closed and displays it in a formatted
     * version to the client
     *
     * @param playerWithHighestScore
     */
    public void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore);

    /**
     * Shuts down server
     */
    public void closeDownProgram();


}
