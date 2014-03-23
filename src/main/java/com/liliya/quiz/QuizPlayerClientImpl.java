package com.liliya.quiz;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the player client which will be able to play available quizzes
 */

public class QuizPlayerClientImpl implements QuizPlayerClient{

    public static void main(String [] args){

       /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }
        */
            QuizPlayerClient newPlayerClient=new QuizPlayerClientImpl();
            newPlayerClient.launchMainMenuPlayer();

    }

    public void launchMainMenuPlayer(){
        System.out.println("What would you like to do: ");
        System.out.println("0-Play a quiz: ");
        System.out.println("1-Check your scores: ");
        System.out.println("2-Exit: ");

        int choice=Integer.parseInt(System.console().readLine());

        switch(choice){
            case 0:
                selectQuizFromMenu();
                break;
            case 1:
                //not implemented yet
                break;
            case 2:
                System.exit(1);
                break;
            default:
                System.out.print("Choose a valid option");
        }

    }


    @Override
    public int selectQuizFromMenu() {
        try{
            Remote service= Naming.lookup("//127.0.0.1:1699/quiz");
            QuizService quizPlayer=(QuizService) service;
            System.out.println("Select from the currently available quizzes: ");
            for(Quiz current:quizPlayer.getListActiveQuizzes()){
                System.out.println(current.getQuizId()+"-"+current.getQuizName());
            }
        }
        catch(MalformedURLException ex){
            ex.printStackTrace();
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(NotBoundException ex){
            ex.printStackTrace();
        }
      return Integer.parseInt(System.console().readLine());
    }

    @Override
    public List<String> playQuiz(int id) {
        System.out.print("Enter your name: ");
        String playerName=System.console().readLine();
        try{
        Remote service= Naming.lookup("//127.0.0.1:1699/quiz");
        QuizService quizPlayer=(QuizService) service;
        Quiz chosenQuiz=quizPlayer.loadQuiz(id, playerName);
        List<String> userGuesses=displayQuizToPlayer(chosenQuiz);
        //quizPlayer.calculateScore(userGuesses);

        }
        catch(MalformedURLException ex){
            ex.printStackTrace();
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(NotBoundException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<String> displayQuizToPlayer(Quiz playingQuiz){
           Set<Question> quizQuestions=playingQuiz.getQuizQuestions();
           List<String> playerGuesses=new ArrayList<String>();
           for(Question current:quizQuestions){
               System.out.println(current.getQuestion());
               for(Map.Entry<String, String> entry:current.getPossibleAnswers().entrySet()){
                   System.out.println(entry.getKey()+"-"+entry.getValue());
               }
               System.out.print("Your answer: ");
               //need a check for an invalid answer here
               playerGuesses.add(System.console().readLine());
           }
           return playerGuesses;
        }


}