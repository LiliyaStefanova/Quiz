package com.liliya.quiz;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class QuizServer extends UnicastRemoteObject implements QuizService, Serializable {

    //list of all players with all quizzes they have taken(including multiple attempts at same one
    //stores quiz id and score
    private List<PlayerQuizInstance> playerQuizInstances;
    private List<Quiz> allActiveQuizzes;
    private Set<Player> allPlayers;

    public QuizServer() throws RemoteException{

        allActiveQuizzes=new ArrayList<Quiz>();
    }
    @Override
    public int generateQuiz(String name, Question[] questions) throws RemoteException{

        Quiz newQuiz=new QuizImpl(name, questions);
        //need to make a check for duplicate quizzes here
        allActiveQuizzes.add(newQuiz);
        return newQuiz.getQuizId();

    }

    @Override
    public Map<Quiz, List<Player>> closeQuiz() throws RemoteException{
        //there should be some way to  mark quizzes as closed
        //need to set the state to false and remove from active quizzes list
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int playQuiz(int id, String name) {

        PlayerQuizInstance newInstance=new PlayerQuizInstance(playerExists(name), quizExists(id));
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Quiz> getListCurrentQuizzes() {
        return allActiveQuizzes;
    }

    @Override
    public Map<Quiz, List<Player>> getPlayersForQuiz() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Player, List<Quiz>> getQuizzesPerPlayer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Quiz quizExists(int id){
        Quiz existingQuiz=null;
        for(Quiz curr: allActiveQuizzes){
            if(curr.getQuizId()==id){
               existingQuiz=curr;
                return  existingQuiz;
            }
        }
        return existingQuiz;
    }

    private Player playerExists(String name){
        Player existingPlayer=null;
        for(Player curr: allPlayers){
            if(curr.getName().equals(name)){
                existingPlayer=curr;
                return curr;
            }
        }
         return existingPlayer;
    }
}
