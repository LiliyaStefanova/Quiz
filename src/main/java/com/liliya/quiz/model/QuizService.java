package com.liliya.quiz.model;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The server functionality of the quiz system
 */

public interface QuizService extends Remote {
    /**
     * Creates a new quiz based on a list of questions provided by the set up client
     *
     * @param name      of the quiz specified by the set up client
     * @param questions of the quiz specified by the set up client
     * @return id of the quiz
     * @throws NullPointerException     if the questions are null
     * @throws IllegalArgumentException if the questions are empty
     */
    public int createNewQuiz(String name, Map<Integer, Question> questions) throws RemoteException;

    /**
     * Stops the quiz instance upon request by set up client
     *
     * @return a PlayerQuizInstance object with the highest score for this quiz
     *         The object contains the score and details of the player for the set up client to display
     */
    public List<PlayerQuizInstance> closeQuiz(int id) throws RemoteException;

    /**
     * Plays the quiz chosen by the player by displaying questions and accepting answers
     * Uses id retrieved following the user choice from a list of quizzes
     * @return the score of the user at the end of the quiz
     * @throws IllegalStateException if a user wants to close a quiz which has already been closed
     */

    /**
     * Server checks to see if the quiz is still being played by a client
     * Used to determine if a quiz can be closed by the set up client
     *
     * @param id
     * @return true or false depending on whether it is played or not
     * @throws RemoteException
     */
    public boolean checkQuizPlayed(int id) throws RemoteException;

    public PlayerQuizInstance loadQuizForPlay(int id, String name) throws RemoteException;

    /**
     * Calculates the total score of a user attempt based on guess provided by client
     *
     * @param quizInstance
     * @param guesses
     * @return total score for attempt
     */
    public int calculatePlayerScore(PlayerQuizInstance quizInstance, Map<Question, Integer> guesses) throws RemoteException;

    /**
     * Returns a list of currently available quizzes for the user to choose from
     *
     * @return list of current quizzes
     */
    public List<Quiz> getListAvailableQuizzes() throws RemoteException;

    /**
     * Returns a set of all instances of quizzes played by this player for the client to use
     *
     * @param name
     * @return set of PlayerQuizInstance
     */
    public List<PlayerQuizInstance> getQuizzesPlayedByPlayer(String name) throws RemoteException;

    /**
     * Adds new player to the existing list of players
     *
     * @param name of the player
     */
    public Player addNewPlayer(String name) throws RemoteException;

    /**
     * Persists all data on the server to file for reloading
     *
     * @throws RemoteException
     */
    public void flush() throws RemoteException;

    /**
     * Shuts down the server
     *
     * @throws RemoteException
     */
    public void shutDown() throws RemoteException;
}
