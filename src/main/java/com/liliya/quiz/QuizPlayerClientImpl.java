package com.liliya.quiz;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 *  This is the player client which will be able to play available quizzes
 */
//Client side threading?

public class QuizPlayerClientImpl implements QuizPlayerClient {

    boolean backToMain=false;
    QuizService quizPlayer=null;

    public static void main(String[] args) {

       /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }
        */
        QuizPlayerClient newPlayerClient = new QuizPlayerClientImpl();
        newPlayerClient.connectToServer();
        newPlayerClient.launchMainMenuPlayer();

    }

    @Override
    public void connectToServer() {
        try {
            Remote service = Naming.lookup("//127.0.0.1:1699/quiz");
            quizPlayer = (QuizService) service;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }
    }

    public void launchMainMenuPlayer() {
        do{
        System.out.println("What would you like to do? ");
        System.out.println(
                "Select an option: \n" +
                    "  1) Play a quiz\n" +
                    "  2) Check scores\n" +
                    "  3) Exit system \n"
                );
        System.out.println(">> ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                playQuiz(selectQuizFromMenu());
                break;
            case 2:
                //not implemented yet
                break;
            case 3:
                backToMain=true;
                System.exit(0);
                break;
            default:
                System.out.print("Choose a valid option");
        }

     } while(!backToMain);
    }

    @Override
    public int selectQuizFromMenu() {
        int choice = 0;

            System.out.println("Select from the currently available quizzes: ");
            try{
            for (Quiz current : quizPlayer.getListActiveQuizzes()) {
                System.out.println(current.getQuizId()+") "+current.getQuizName());
            }
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            } catch(RemoteException ex){
                ex.printStackTrace();
            }
        return choice;
    }

    @Override
    public List<String> playQuiz(int id) {
        System.out.print("Enter your name: ");
        Scanner sc = new Scanner(System.in);
        String playerName = sc.nextLine();
        try {

            PlayerQuizInstance playerQuizInstance = quizPlayer.loadQuiz(id, playerName);
            Map<Question, Integer> userGuesses = displayQuizToPlayer(playerQuizInstance.getQuiz());
            System.out.print("Thank you for your responses. Your final score is: ");
            System.out.println(quizPlayer.calculateQuizScore(playerQuizInstance, userGuesses));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<Question, Integer> displayQuizToPlayer(Quiz playingQuiz) {
        Map<Integer, Question> quizQuestions = playingQuiz.getQuizQuestions();
        Map<Question, Integer> playerGuesses = new HashMap<Question, Integer>();
        for (Map.Entry<Integer, Question> entryQuestion : playingQuiz.getQuizQuestions().entrySet()) {
            System.out.println(entryQuestion.getKey() + ". " + entryQuestion.getValue().getQuestion());
            for (Map.Entry<Integer, String> entryAnswer : entryQuestion.getValue().getPossibleAnswers().entrySet()) {
                System.out.println(entryAnswer.getKey() + "-" + entryAnswer.getValue());
            }
            System.out.print("Your answer: ");
            Scanner sc = new Scanner(System.in);
            int playerGuess = sc.nextInt();
            //need a check for an invalid answer here
            playerGuesses.put(entryQuestion.getValue(), playerGuess);
        }
        return playerGuesses;
    }


}