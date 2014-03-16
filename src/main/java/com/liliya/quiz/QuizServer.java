package com.liliya.quiz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 15/03/14
 * Time: 09:58
 * To change this template use File | Settings | File Templates.
 */
public class QuizServer extends UnicastRemoteObject implements QuizService {
    //stores all quizzes with players and scores
    //players and scores will be null for all newly generated quizzes
    private Map<Quiz,Map<Player,Integer>> quizzes;
    //list of all players with all quizzes they have taken(including multiple attempts at same one
    //stores quiz id and score
    private Map<Player, Map<Integer, Integer>> playerQuizScores;
    //private List<Quiz> allActiveQuizzes;
    private Set<Player> allPlayers;

    public QuizServer() throws RemoteException{

    }
    @Override
    public int generateQuiz(String name, Question[] questions) throws RemoteException{

        Quiz newQuiz=new QuizImpl(name, questions);
        //need to make a check for duplicate quizzes here
        quizzes.put(newQuiz, null);

        return newQuiz.getQuizId();

    }

    @Override
    public Map<Quiz, List<Player>> closeQuiz() throws RemoteException{
        //there should be some way to  mark quizzes as closed
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int playQuiz(int id) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Quiz> getListCurrentQuizzes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Quiz, List<Player>> getPlayersForQuiz() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Player, List<Quiz>> getQuizzesPerPlayer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
