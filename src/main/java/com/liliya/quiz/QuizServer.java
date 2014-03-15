package com.liliya.quiz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 15/03/14
 * Time: 09:58
 * To change this template use File | Settings | File Templates.
 */
public class QuizServer extends UnicastRemoteObject implements QuizService {

    private Map<Quiz,List<Player>> playersPerQuiz;

    public QuizServer() throws RemoteException{

    }
    @Override
    public int generateQuiz(Question[] questions) throws RemoteException{
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Quiz, List<Player>> closeQuiz() throws RemoteException{
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
