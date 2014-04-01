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
     * Prompts the user for a name and list of questions for the quiz, calls the generateQuiz method on server
     * and generates a new quiz
     * Gets the id back
     */
    public void setUpQuizManually();

    /**
     * Generates a collection of questions based on data provided in a csv file and calls generateQuiz for server to set
     * up a new quiz
     * Gets id back from the server
     *
     */
    public void setUpQuizFromFile();
    /**
     * Closes the quiz when the the set up client provides the id
     *
     */

    public void requestQuizClose();

    /**
     * Shuts down server
     */
    public void closeDownProgram();


}
