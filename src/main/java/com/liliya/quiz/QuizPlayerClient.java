package com.liliya.quiz;

import java.util.List;

/**
 * Public interface for the player client
 */
public interface QuizPlayerClient {

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
     * @return a list of answers by the player to be scored
     */
    public List<String> playQuiz(int id);


}
