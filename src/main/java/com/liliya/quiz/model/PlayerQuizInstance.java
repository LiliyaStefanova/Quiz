package com.liliya.quiz.model;

/**
 * Player Quiz Instance represents every instance of a quiz played by a player
 * It reflects the many to many relationship between players and quizzes
 * Each instance is denoted by its total score and whether the player is actively playing or not
 */

public interface PlayerQuizInstance {

    /**
     * Returns the total score for this particular attempt of the player
     *
     * @return totalScore
     */

    public int getTotalScore();

    /**
     * Denotes if the quiz is currently being played or not
     * Used to control closing quizzes which are actively played
     *
     * @return true or false
     */

    public boolean isQuizPlayed();

    /**
     * Sets a quiz as an actively played by the player for this instance to prevent the quiz from being closed
     *
     * @param quizPlayed
     */

    public void setQuizPlayed(boolean quizPlayed);

    /**
     * Sets the total score for this player and their current attempt, following a calculation of the score
     *
     * @param score
     */

    public void setTotalScore(int score);

    /**
     * Retrieves the player in this instance
     *
     * @return Player object
     */

    public Player getPlayer();

    /**
     * Retrieves the quiz in the instance
     *
     * @return Quiz object
     */

    public Quiz getQuiz();

    /**
     * Sets the Player in the instance
     *
     * @param player
     */

    public void setPlayer(Player player);

    /**
     * Sets the quiz in the instance
     *
     * @param quiz
     */

    public void setQuiz(Quiz quiz);

}
