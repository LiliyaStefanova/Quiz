package com.liliya.quiz;


import java.rmi.Remote;
import java.util.List;
import java.util.Map;

/**
 * The server functionality of the quiz system
 */

public interface QuizService extends Remote {

    /**
     * Creates a new quiz based on a list of questions provided by user
     * @param questions
     * @return id of the quiz
     */
    public int generateQuiz(List<Question> questions);

    /**
     * Stops the quiz instance upon request by set up client
     * @return list of all players for this quiz instance
     */
    public Map<Quiz, List<Player>> closeQuiz();

    /**
     * Plays the quiz chosen by the player by displaying questions and accepting answers
     * Uses id retrieved following the user choice from a list of quizzes
     * @return the score of the user at the end of the quiz
     */
    public int playQuiz(int id);

    /**
     * Returns a list of current quizzes for the user to choose from
     * @return list of current quizzes
     */
    public List<Quiz> getListCurrentQuizzes();

    /**
     * Returns the list of players who have played a quiz
     * @return list of players for quiz
     */
    public Map<Quiz, List<Player>> getPlayersForQuiz();

    /**
     * returns the list of quizzes that a player has played
     * @return list of quizzes per player
     */
    public Map<Player, List<Quiz>> getQuizzesPerPlayer();
    }
