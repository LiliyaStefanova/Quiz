package com.liliya.quiz;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class QuizServer extends UnicastRemoteObject implements QuizService, Serializable {

    //list of all players with all quizzes they have taken(including multiple attempts at same one
    //stores quiz id and score
    private List<PlayerQuizInstance> playerQuizInstances;
    private List<Quiz> allQuizzes;
    private Set<Player> allPlayers;

    public QuizServer() throws RemoteException{

        allQuizzes=new ArrayList<Quiz>();
        allPlayers=new HashSet<Player>();
        playerQuizInstances=new ArrayList<PlayerQuizInstance>();
    }
    @Override
    public int generateQuiz(String name, Map<Integer, Question> questions) throws RemoteException{

        Quiz newQuiz=new QuizImpl(name, questions);
        //need to make a check for duplicate quizzes here
        allQuizzes.add(newQuiz);
        return newQuiz.getQuizId();

    }

    @Override
    public PlayerQuizInstance closeQuiz(int id) throws RemoteException{
        List<PlayerQuizInstance> quizInstances=new ArrayList<PlayerQuizInstance>();
        PlayerQuizInstance winner=null;
        int maxScore=0;
        //need to refactor and take this to another method
        //there should be some way to  mark quizzes as closed
        for(Quiz curr:allQuizzes){
            if(curr.getQuizId()==id){
                curr.setQuizState(false);
                for(PlayerQuizInstance instance:playerQuizInstances){
                    if(instance.getQuiz().equals(curr)){
                        quizInstances.add(instance);
                    }
                }
            }
        }

        for(PlayerQuizInstance instances:quizInstances){
            if(instances.getTotalScore()>maxScore){
                maxScore=instances.getTotalScore();
                winner=instances;
            }
        }
        //returns the PlayerQuizInstance to the set up client where display will be formatted
        return winner;

    }

    @Override
    public PlayerQuizInstance loadQuiz(int id, String name) {
        /*Player instancePlayer=null;
        if(playerExists(name)==null){
            instancePlayer=addNewPlayer(name);
        }
        else{
            instancePlayer=playerExists(name);
            }*/
        //need to check if player exists and if player does not create a new one
        return new PlayerQuizInstance(playerExists(name), quizExists(id));
    }

    @Override
    public int calculateQuizScore(PlayerQuizInstance quizInstance, Map<Question, String> guesses) {
        int playerQuizInstanceScore=0;
        for(Map.Entry<Question, String> entry: guesses.entrySet()){
            if(entry.getKey().getCorrectAnswer().equals(entry.getValue())){
                playerQuizInstanceScore=playerQuizInstanceScore+entry.getKey().getCorrectAnswerPoints();
            }
        }
       for(PlayerQuizInstance current:playerQuizInstances){
           if(current.equals(quizInstance)){
               quizInstance.setTotalScore(playerQuizInstanceScore);
           }
       }
       return playerQuizInstanceScore;
    }

    @Override
    public List<Quiz> getListActiveQuizzes() {
        List<Quiz> activeQuizzes=new ArrayList<Quiz>();
        for(Quiz current:allQuizzes){
            if(current.getQuizState()){
                activeQuizzes.add(current);
            }
        }
        return activeQuizzes;
    }

    @Override
    public Player addNewPlayer(String name) {

        Player newPlayer=new PlayerImpl(name);
        allPlayers.add(newPlayer);

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

    private Quiz quizExists(int id){
        Quiz existingQuiz=null;
        for(Quiz curr: allQuizzes){
            if(curr.getQuizId()==id){
               existingQuiz=curr;
                return  existingQuiz;
            }
        }
        return existingQuiz;
    }

    private Player playerExists(String name){
        for(Player curr: allPlayers){
            if(curr.getName().equals(name)){
                return curr;
            }
        }
        return addNewPlayer(name);
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
}
