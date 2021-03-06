package com.liliya.quiz.adminclient;

/**
 * Public interface for the set up client
 */

public interface QuizSetUpClient {

    /**
     * Look up the remote service
     */
    public void connectToService();

    /**
     * Displays menu with options for actions the set up user can perform
     */
    public void menu();

    /**
     * Prompts the user for a name and list of questions for the quiz, calls the createNewQuiz method on server
     * to generate a new quiz
     * Gets the id back the server
     */
    public void setUpNewQuizManually();

    /**
     * Prompts for a quiz name
     * Generates a collection of questions based on data provided in a csv file and calls createNewQuiz for server to
     * set up a new quiz
     * Gets id back from the server
     */
    public void setUpNewQuizFromFile();

    /**
     * Closes the quiz when the the set up client provides the id
     * Gets player details and score for the winner of the game from server
     * Displays a notification of the winner and their score to the user
     */

    public void requestQuizClose();

    /**
     * Writes server contents to file
     * Shuts down server process
     */
    public void closeDownServer();

    /**
     * Shuts down the set up client process (also referred to as admin client)
     * Server will continue running if it has not been shut down
     */
    public void closeDownClient();


}
