package com.liliya.quiz.server;


import com.liliya.quiz.model.*;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;

public class QuizServer extends UnicastRemoteObject implements QuizService, Serializable {

    private List<PlayerQuizInstance> playerQuizInstances;   //list of all instances of a quiz played, player and score
    private List<Quiz> allQuizzes;
    private Set<Player> allPlayers;
    private static final String FILENAME = "server_state.xml";
    private int quizIDCounter;  //keeps track of the next quiz ID to be assigned

    private static final String SERVICE_NAME = "quiz";

    private static Logger serverLogger = Logger.getLogger(QuizServer.class.getName());

    public QuizServer() throws RemoteException {
        allQuizzes = new ArrayList<Quiz>();
        allPlayers = new HashSet<Player>();
        playerQuizInstances = new ArrayList<PlayerQuizInstance>();
        quizIDCounter = 0;
    }

    //TODO check for duplicate quizzes
    @Override
    public synchronized int createNewQuiz(String name, Map<Integer, Question> questions) throws RemoteException {

        if (questions == null) {
            throw new NullPointerException("Questions cannot be null");
        } else if (questions.isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be empty");
        } else {
            Quiz newQuiz = new QuizImpl(name, questions, quizIDCounter);
            quizIDCounter++;        //increment counter for next quiz
            int quizId = newQuiz.getQuizId();
            allQuizzes.add(newQuiz);
            serverLogger.info(allPlayers.size() + " quizzes created");
            return quizId;
        }
    }

    //TODO a quiz cannot be closed if there is a player client still playing it-needs to be figured out-does synchronized fix this?

    @Override
    public synchronized List<PlayerQuizInstance> closeQuiz(int id) throws RemoteException {

        //find the quiz to be closed and set it to inactive so other players can't choose it
        for (Quiz curr : allQuizzes) {
            if (curr.getQuizId() == id) {
                curr.setQuizState(false);
            }
        }
        return determineQuizWinner(getAllQuizInstances(id));
    }

    public synchronized PlayerQuizInstance loadQuizForPlay(int id, String name) throws RemoteException {
        PlayerQuizInstance newQuizPlayerInstance = new PlayerQuizInstance(setUpPlayer(name), findQuiz(id));
        playerQuizInstances.add(newQuizPlayerInstance);
        return newQuizPlayerInstance;
    }

    //takes player guesses and calculates and returns score
    @Override
    public synchronized int calculatePlayerScore(PlayerQuizInstance quizInstance, Map<Question, Integer> guesses) throws RemoteException {
        int playerQuizInstanceScore = 0;
        for (Map.Entry<Question, Integer> entry : guesses.entrySet()) {
            if (entry.getKey().getCorrectAnswer() == entry.getValue()) {
                playerQuizInstanceScore = playerQuizInstanceScore + entry.getKey().getCorrectAnswerPoints();
            }
        }
        for (PlayerQuizInstance current : playerQuizInstances) {
            if (current.equals(quizInstance)) {
                current.setTotalScore(playerQuizInstanceScore);
            }
        }

        return playerQuizInstanceScore;
    }

    @Override
    public synchronized List<Quiz> getListAvailableQuizzes() throws RemoteException {
        List<Quiz> activeQuizzes = new ArrayList<Quiz>();
        for (Quiz current : allQuizzes) {
            if (current.getQuizState()) {
                activeQuizzes.add(current);
            }
        }
        return activeQuizzes;
    }

    @Override
    public List<PlayerQuizInstance> getQuizzesPlayedByPlayer(String name) throws RemoteException {
        List<PlayerQuizInstance> quizInstancesForPlayer=new ArrayList<PlayerQuizInstance>();
        for(PlayerQuizInstance currentInstance:playerQuizInstances){
            if(currentInstance.getPlayer().getName().equals(name)){
                quizInstancesForPlayer.add(currentInstance);
            }
        }
        return quizInstancesForPlayer;
    }

    @Override
    public synchronized Player addNewPlayer(String name) throws RemoteException {

        Player newPlayer = new PlayerImpl(name);
        allPlayers.add(newPlayer);

        return newPlayer;
    }

    @Override
    public void flush() throws RemoteException {
        Serializer serializer = new Serializer();
        serializer.encodeData(this);
    }

    @Override
    public void shutDown() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(1699);
            registry.unbind(SERVICE_NAME);
            UnicastRemoteObject.unexportObject(this, false);
        } catch (NotBoundException ex) {
            throw new RemoteException("Could not un-register, quitting anyway...", ex);
        }

        new Thread() {
            @Override
            public void run() {

                serverLogger.info("Shutting down...");

                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    //nothing to do here
                }
                serverLogger.info("Done");
                System.exit(0);
            }
        }.start();
    }

    Quiz findQuiz(int id) {
        Quiz existingQuiz = null;
        for (Quiz curr : allQuizzes) {
            if (curr.getQuizId() == id) {
                existingQuiz = curr;
            }
        }
        return existingQuiz;
    }
    //find the existing player or create a new one
    Player setUpPlayer(String name) throws RemoteException {
        for (Player curr : allPlayers) {
            if (curr.getName().equals(name)) {
                return curr;
            }
        }
        return addNewPlayer(name);
    }

    //finds all instances of the quiz played
    List<PlayerQuizInstance> getAllQuizInstances(int id) {
        List<PlayerQuizInstance> quizPlayInstances = new ArrayList<PlayerQuizInstance>();

        for (PlayerQuizInstance instance : playerQuizInstances) {
            if (instance.getQuiz().getQuizId() == id) {
                quizPlayInstances.add(instance);
            }
        }

        return quizPlayInstances;
    }

    //finds the player with highest score out of all quiz instances
    //TODO functionality to deal with multiple players with the highest score
    List<PlayerQuizInstance> determineQuizWinner(List<PlayerQuizInstance> quizPlayInstances) {
        PlayerQuizInstance maxScore = null;
        List<PlayerQuizInstance> winners=new ArrayList<PlayerQuizInstance>();
        if (quizPlayInstances.isEmpty()) {
            return winners;
        }
        maxScore = Collections.max(quizPlayInstances, new Comparator<PlayerQuizInstance>() {
            @Override
            public int compare(PlayerQuizInstance o1, PlayerQuizInstance o2) {
                if (o1.getTotalScore() > o2.getTotalScore()) {
                    return 1;
                } else if ((o1.getTotalScore() < o2.getTotalScore())) {
                    return -1;
                } else return 0;
            }
        });
        for(PlayerQuizInstance current:quizPlayInstances){
            if(maxScore.getTotalScore()==current.getTotalScore()){
                winners.add(current);
            }
        }

        return winners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        QuizServer that = (QuizServer) o;

        if (allPlayers != null ? !allPlayers.equals(that.allPlayers) : that.allPlayers != null) return false;
        if (allQuizzes != null ? !allQuizzes.equals(that.allQuizzes) : that.allQuizzes != null) return false;
        if (playerQuizInstances != null ? !playerQuizInstances.equals(that.playerQuizInstances) : that.playerQuizInstances != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (playerQuizInstances != null ? playerQuizInstances.hashCode() : 0);
        result = 31 * result + (allQuizzes != null ? allQuizzes.hashCode() : 0);
        result = 31 * result + (allPlayers != null ? allPlayers.hashCode() : 0);
        return result;
    }

    public List<PlayerQuizInstance> getPlayerQuizInstances() {
        return playerQuizInstances;
    }

    public void setPlayerQuizInstances(List<PlayerQuizInstance> playerQuizInstances) {
        this.playerQuizInstances = playerQuizInstances;
    }

    public List<Quiz> getAllQuizzes() {
        return allQuizzes;
    }

    public void setAllQuizzes(List<Quiz> allQuizzes) {
        this.allQuizzes = allQuizzes;
    }

    public Set<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(Set<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public int getQuizIDCounter() {
        return quizIDCounter;
    }

    public void setQuizIDCounter(int quizIDCounter) {
        this.quizIDCounter = quizIDCounter;
    }

    public static void main(String[] args) {
        //TODO security manager on the server side

    }

}
