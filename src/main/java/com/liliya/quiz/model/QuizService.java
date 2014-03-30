package com.liliya.quiz.model;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * The server functionality of the quiz system
 */

public interface QuizService extends Remote {
    /**
     * Creates a new quiz based on a list of questions provided by user
     *
     * @param questions of the quiz specified by the set up client
     * @return id of the quiz
     */
    public int generateQuiz(String name, Map<Integer, Question> questions) throws RemoteException;

    /**
     * Stops the quiz instance upon request by set up client
     *
     * @return list of all players for this quiz instance
     */
    public PlayerQuizInstance closeQuiz(int id) throws RemoteException;

    /**
     * Plays the quiz chosen by the player by displaying questions and accepting answers
     * Uses id retrieved following the user choice from a list of quizzes
     *
     * @return the score of the user at the end of the quiz
     */
    public PlayerQuizInstance loadQuiz(int id, String name) throws RemoteException;

    /**
     * Calculates the total score of a user attempt
     *
     * @param quizInstance
     * @param guesses
     * @return total score for attempt
     */
    public int calculateQuizScore(PlayerQuizInstance quizInstance, Map<Question, Integer> guesses) throws RemoteException;

    /**
     * Returns a list of current quizzes for the user to choose from
     *
     * @return list of current quizzes
     */
    public List<Quiz> getListActiveQuizzes() throws RemoteException;

    /**
     * Adds new player to the existing list
     *
     * @param name of the player
     */
    public Player addNewPlayer(String name) throws RemoteException;

    /**
     * Returns the list of players who have played a quiz
     *
     * @return list of players for quiz
     */
    public Map<Quiz, List<Player>> getPlayersForQuiz() throws RemoteException;

    /**
     * returns the list of quizzes that a player has played
     *
     * @return list of quizzes per player
     */
    public Map<Player, List<Quiz>> getQuizzesPerPlayer() throws RemoteException;

    /**
     * Persists the data on the server to a file
     */

    public void flush() throws RemoteException;
}
