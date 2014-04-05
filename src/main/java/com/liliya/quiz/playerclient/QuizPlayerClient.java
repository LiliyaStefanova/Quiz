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
     * Main menu functionality for the player client
     */
    public void launchMainMenuPlayer();

    /**
     * The menu for players to select from active quizzes
     *
     * @return the id of the quiz selected by the player;
     */

    public int selectQuizFromMenu();

    /**
     * Launch
     *
     * @param id of the quiz
     *
     */
    public void playQuiz(int id);

    /**
     * Displays the top score of the user for all quizzes played so far
     */
    public void seeTopScore();


}
