package com.liliya.quiz;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * This is the player client which will be able to play available quizzes
 */

public class QuizPlayerClientImpl implements QuizPlayerClient {

    private static final TextMenuItem selectFromList = new TextMenuItem("Select quiz", MenuActions.SELECT_QUIZ_FROM_LIST);
    private static final TextMenuItem viewHighScores = new TextMenuItem("View high scores", MenuActions.VIEW_HIGH_SCORES);
    private static final TextMenuItem back = new TextMenuItem("Go Back", MenuActions.BACK);

    private static List<TextMenuItem> playerMenu = new ArrayList<TextMenuItem>(Arrays.asList(selectFromList, viewHighScores, back));

    boolean backToMain = false;
    QuizService quizPlayer = null;

    public static void main(String[] args) {
        //TODO sort out the security manager
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
        do {

            MenuActions action = TextMenu.display("Quiz Play", playerMenu);

            switch (action) {
                case SELECT_QUIZ_FROM_LIST:
                    playQuiz(selectQuizFromMenu());
                    break;
                case VIEW_HIGH_SCORES:
                    //not implemented yet
                    break;
                case BACK:
                    backToMain = true;
                    System.exit(0);
                    break;
                default:
                    System.out.print("Choose a valid option");
            }

        } while (!backToMain);
    }

    @Override
    public int selectQuizFromMenu() {
        int choice = 0;

        System.out.println("Select from the currently available quizzes: ");
        try {
            for (Quiz current : quizPlayer.getListActiveQuizzes()) {
                System.out.println(current.getQuizId() + ") " + current.getQuizName());
            }
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
        } catch (RemoteException ex) {
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

    //TODO quiz to be displayed with numbers starting from 1 not 0
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
            //TODO check if any of the answers provided are invalid
            playerGuesses.put(entryQuestion.getValue(), playerGuess);
        }
        return playerGuesses;
    }


}