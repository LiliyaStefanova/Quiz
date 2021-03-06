package com.liliya.quiz.model;

import java.util.Map;

/**
 * Question as part of hte questions in a quiz
 */
public interface Question {

    /**
     * Returns the question
     *
     * @return question string
     */
    public String getQuestion();

    /**
     * Used when creating a new question
     *
     * @param question
     */
    public void setQuestion(String question);

    /**
     * returns the list of possible answers for this question(multiple choice)
     *
     * @return list of string answers
     */
    public Map<Integer, String> getPossibleAnswers();

    /**
     * Sets the list of possible answers to a question when the question is created
     *
     * @param possibleAnswers
     */
    public void setPossibleAnswers(Map<Integer, String> possibleAnswers);

    /**
     * Returns the correct answer for a question
     *
     * @return correct answer string
     */
    public int getCorrectAnswer();

    /**
     * Sets the correct answer to a question
     *
     * @param answer
     */
    public void setCorrectAnswer(int answer);

    /**
     * Sets the number of points to be rewarded for a correct answer when setting up a quiz
     *
     * @param points
     */
    public void setCorrectAnswerPoints(int points);

    /**
     * Returns the points for a correct answers; all other answers provide 0 points
     *
     * @return numerical value of points rewarded
     */

    public int getCorrectAnswerPoints();

}
