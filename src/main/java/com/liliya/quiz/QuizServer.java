package com.liliya.quiz;


import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class QuizServer extends UnicastRemoteObject implements QuizService, Serializable {

    //list of all players with all quizzes they have taken(including multiple attempts at same one
    //stores quiz id and score
    private List<PlayerQuizInstance> playerQuizInstances;
    private List<Quiz> allQuizzes;
    private Set<Player> allPlayers;
    private static final String FILENAME = "serverstate.xml";
    private final ScheduledExecutorService persistenceScheduler = Executors.newScheduledThreadPool(1);


    public QuizServer() throws RemoteException {
        this(true);
    }

    public QuizServer(boolean deserialize) throws RemoteException {

        allQuizzes = new ArrayList<Quiz>();
        allPlayers = new HashSet<Player>();
        playerQuizInstances = new ArrayList<PlayerQuizInstance>();
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
    public synchronized int generateQuiz(String name, Map<Integer, Question> questions) throws RemoteException {

        Quiz newQuiz = new QuizImpl(name, questions);
        //need to make a check for duplicate quizzes here
        allQuizzes.add(newQuiz);
        //encodeData();
        System.out.println("Size of all quizzes now is: " + allQuizzes.size());
        return newQuiz.getQuizId();

    }

    //need to refactor and take this to another method
    //there should be some way to  mark quizzes as closed
    //a quiz cannot be closed if there is a player client still playing it

    @Override
    public synchronized PlayerQuizInstance closeQuiz(int id) throws RemoteException {
        List<PlayerQuizInstance> quizInstances = new ArrayList<PlayerQuizInstance>();
        PlayerQuizInstance winner = new PlayerQuizInstance();
        int maxScore = 0;
        //find the quiz to be closed and set it to inactive so other players can't choose it
        for (Quiz curr : allQuizzes) {
            if (curr.getQuizId() == id) {
                curr.setQuizState(false);
                // find all instances of the quiz played
            }
        }
        for (PlayerQuizInstance instance : playerQuizInstances) {
            if (instance.getQuiz().getQuizId() == id) {
                quizInstances.add(instance);
            }
        }

        for (PlayerQuizInstance instance : quizInstances) {
            if (instance.getTotalScore() > maxScore) {
                maxScore = instance.getTotalScore();
                winner = instance;
            }

        }
        for (PlayerQuizInstance instance : quizInstances) {
            if (instance.getTotalScore()==maxScore) {
                winner = instance;
            }

        }
        //returns the PlayerQuizInstance to the set up client where display will be formatted
        System.out.println("The winner is:" + winner.getPlayer().getName());
        return winner;

    }

    @Override
    public synchronized PlayerQuizInstance loadQuiz(int id, String name) {
        System.out.println("Size of all instances now is: " + playerQuizInstances.size());
        PlayerQuizInstance newQuizPlayerInstance = new PlayerQuizInstance(setUpPlayer(name), findQuiz(id));
        playerQuizInstances.add(newQuizPlayerInstance);
        System.out.println("Size of all instances now is: " + playerQuizInstances.size());
        return newQuizPlayerInstance;
    }

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
            }
        }
        return playerQuizInstanceScore;
    }

    @Override
    public synchronized List<Quiz> getListActiveQuizzes() {
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
        //encodeData();
        System.out.print("Size of all players now is: " + allPlayers.size());
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
    public void encodeData() {

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

    @Override
    @SuppressWarnings("unchecked")
    public void decodeData() {

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
                return existingQuiz;
            }
        }
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

    private void persist() {
        final Runnable saver = new Runnable() {
            public void run() {
                encodeData();
            }
        };
        final ScheduledFuture<?> saverHandle =
                persistenceScheduler.scheduleAtFixedRate(saver, 10, 10, TimeUnit.SECONDS);

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
}
