package com.liliya.quiz;

/**
 * Public interface for the set up client
 */

public interface QuizSetUpClient {

    /**
     * Displays menu with options for actions the set up user can perform
     */
    public void menu();

    /**
     * Provides a name and list of questions for the quiz, calls the generateQuiz method on server
     * and generates a new quiz
     * Gets the id back
     * ids of quizzes set up to be stored by set up client locally to retrieve later
     */
    public void setUpQuiz();

    /**
     * Closes the quiz when the the set up client provides the id
     *
     * @param quizID will display a notification of player with highest score for this game instance
     */

    public void closeQuiz(int quizID);

    /**
     * takes in the value returned from the server after the quiz is closed and displays it in a formatted
     * version to the client
     *
     * @param playerWithHighestScore
     */
    public void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore);

}
