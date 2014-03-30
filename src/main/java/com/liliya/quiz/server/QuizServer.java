package com.liliya.quiz.server;


import com.liliya.quiz.model.*;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class QuizServer extends UnicastRemoteObject implements QuizService, Serializable {

    //list of all players with all quizzes they have taken(including multiple attempts at same one
    //stores quiz id and score
    private static final String SERVICE_NAME ="quiz";
    private List<PlayerQuizInstance> playerQuizInstances;
    private List<Quiz> allQuizzes;
    private Set<Player> allPlayers;
    private static final String FILENAME = "serverstate.xml";

   /* public QuizServer() throws RemoteException {
        this();
    }*/

    public QuizServer() throws RemoteException {

        allQuizzes = new ArrayList<Quiz>();
        allPlayers = new HashSet<Player>();
        playerQuizInstances = new ArrayList<PlayerQuizInstance>();
    }

    @Override
    public synchronized int generateQuiz(String name, Map<Integer, Question> questions) throws RemoteException {

        Quiz newQuiz = new QuizImpl(name, questions);
        int quizId = 0;
        //need to make a check for duplicate quizzes here
        allQuizzes.add(newQuiz);
        System.out.println("Size of all quizzes now is: " + allQuizzes.size());        //debugging-needs removal
        for (Quiz current : allQuizzes) {
            if (current.getQuizId() == newQuiz.getQuizId()) {
                quizId = current.getQuizId();
            } else quizId = 0;
        }

        return quizId;
    }

    // TODO need to refactor and take this to another method
    //TODO a quiz cannot be closed if there is a player client still playing it-needs to be figured out-does synchronized fix this?
    // TODO exception if there are no players of the quiz or it does not exist

    @Override
    public synchronized PlayerQuizInstance closeQuiz(int id) throws RemoteException {

        //find the quiz to be closed and set it to inactive so other players can't choose it
        for (Quiz curr : allQuizzes) {
            if (curr.getQuizId() == id) {
                curr.setQuizState(false);
            }
        }
        return HighScores.getTopQuizScore(id);
    }

    public synchronized PlayerQuizInstance loadQuiz(int id, String name) {
        PlayerQuizInstance newQuizPlayerInstance = new PlayerQuizInstance(setUpPlayer(name), findQuiz(id));
        playerQuizInstances.add(newQuizPlayerInstance);
        return newQuizPlayerInstance;
    }

    //takes guesses from player and calculates and returns score
    @Override
    public synchronized int calculateQuizScore(PlayerQuizInstance quizInstance, Map<Question, Integer> guesses) {
        int playerQuizInstanceScore = 0;
        for (Map.Entry<Question, Integer> entry : guesses.entrySet()) {
            if (entry.getKey().getCorrectAnswer() == ((entry.getValue()))) {
                playerQuizInstanceScore = playerQuizInstanceScore + entry.getKey().getCorrectAnswerPoints();
            }
        }
        for (PlayerQuizInstance current : playerQuizInstances) {
            if (current.equals(quizInstance)) {
                quizInstance.setTotalScore(playerQuizInstanceScore);
                HighScores.updateHighScores(current);
            }
        }

        return playerQuizInstanceScore;
    }

    @Override
    public synchronized List<Quiz> getListActiveQuizzes() throws RemoteException {
        List<Quiz> activeQuizzes = new ArrayList<Quiz>();
        for (Quiz current : allQuizzes) {
            if (current.getQuizState()) {
                activeQuizzes.add(current);
            }
        }
        return activeQuizzes;
    }

    @Override
    public synchronized Player addNewPlayer(String name) {

        Player newPlayer = new PlayerImpl(name);
        allPlayers.add(newPlayer);
        System.out.print("Size of all players now is: " + allPlayers.size());       //debugging only-remove
        return newPlayer;
    }

    @Override
    public Map<Quiz, List<Player>> getPlayersForQuiz() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Player, List<Quiz>> getQuizzesPerPlayer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reload() throws RemoteException {
        File f = new File("." + File.separator + FILENAME);
        if (f.exists() && f.length() > 0) {
            decodeData();
        } else if (f.exists() && f.length() == 0) {
            System.out.println("File is empty");
            //warn user if file exists but is empty
            //wait for file to be written to
        } else if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() throws RemoteException {
        encodeData();
        Registry registry=LocateRegistry.getRegistry(1699);
        try{
            registry.unbind(SERVICE_NAME);
            UnicastRemoteObject.unexportObject(this, true);
        } catch(NotBoundException ex){
            throw new RemoteException("Could not un-register, quitting anyway...", ex);
        }
    }

    private void encodeData() {

        XMLEncoder encode = null;
        try {
            encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("." + File.separator + FILENAME)));
            encode.writeObject(allQuizzes);
            encode.writeObject(allPlayers);
            encode.writeObject(playerQuizInstances);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            if (encode != null) {
                encode.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void decodeData() {

        XMLDecoder decode = null;
        try {
            decode = new XMLDecoder(new BufferedInputStream(new FileInputStream("." + File.separator + FILENAME)));
            allQuizzes = (List<Quiz>) decode.readObject();
            allPlayers = (Set<Player>) decode.readObject();
            playerQuizInstances = (List<PlayerQuizInstance>) decode.readObject();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (decode != null) {
                decode.close();
            }
        }
    }

    private Quiz findQuiz(int id) {
        Quiz existingQuiz = null;
        for (Quiz curr : allQuizzes) {
            if (curr.getQuizId() == id) {
                existingQuiz = curr;
                //return existingQuiz;
            }
        }
        if (existingQuiz != null) {
            System.out.println("Quiz exists");
        }  //debugging only remove
        return existingQuiz;
    }

    private Player setUpPlayer(String name) {
        for (Player curr : allPlayers) {
            if (curr.getName().equals(name)) {
                return curr;
            }
        }
        return addNewPlayer(name);
    }

    @Deprecated
    private PlayerQuizInstance determineQuizWinner(List<PlayerQuizInstance> instances) {
        int maxScore = 0;
        PlayerQuizInstance quizWinner = null;
        for (PlayerQuizInstance instance : instances) {
            if (instance.getTotalScore() > maxScore) {
                maxScore = instance.getTotalScore();

                quizWinner = instance;
            }

        }
        System.out.println("Highest score is:" + maxScore);
        return quizWinner;
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

    public static void main(String[] args) {
        //TODO security manager on the server side

        try {
            Registry registry = LocateRegistry.createRegistry(1699);
            QuizService server = new QuizServer();
            server.reload();
            String registryHost = "//localhost/";
            String serviceName = SERVICE_NAME;
            registry.rebind(serviceName, server);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (AlreadyBoundException ex) {
            ex.printStackTrace();
        }
    }

}
