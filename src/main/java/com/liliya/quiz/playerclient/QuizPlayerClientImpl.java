package com.liliya.quiz.playerclient;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.*;

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
    String playerName = "";

    UserInputManager userInputManager = new UserInputManager();

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
            MenuActions action = TextMenu.display("Quiz Player Menu", playerMenu);
            switch (action) {
                case SELECT_QUIZ_FROM_LIST:
                    playQuiz(selectQuizFromMenu());
                    break;
                case VIEW_HIGH_SCORES:
                    userInputManager.providePlayerName();
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
        System.out.print("Select from the currently available quizzes: ");
        try {
            if (quizPlayer.getListActiveQuizzes().isEmpty()) {
                System.out.println("There are no quizzes available at this time");

            }
            for (Quiz current : quizPlayer.getListActiveQuizzes()) {
                System.out.println(current.getQuizId() + ") " + current.getQuizName());
            }
            System.out.print(">>");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        return choice;
    }

    @Override
    public void playQuiz(int id) {

        try {

            PlayerQuizInstance playerQuizInstance = quizPlayer.loadQuiz(id, playerName);
            Map<Question, Integer> userGuesses = submitAnswersForScoring(playerQuizInstance.getQuiz());
            System.out.print("Thank you for your responses. Your final score is: ");
            System.out.println(quizPlayer.calculateQuizScore(playerQuizInstance, userGuesses));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //TODO quiz to be displayed with numbers starting from 1 not 0
    private Map<Question, Integer> submitAnswersForScoring(Quiz playingQuiz) {
        Map<Integer, Question> quizQuestions = playingQuiz.getQuizQuestions();
        Map<Question, Integer> playerGuesses = new HashMap<Question, Integer>();
        for (Map.Entry<Integer, Question> entryQuestion : playingQuiz.getQuizQuestions().entrySet()) {
            System.out.println(entryQuestion.getKey() + ". " + entryQuestion.getValue().getQuestion());
            for (Map.Entry<Integer, String> entryAnswer : entryQuestion.getValue().getPossibleAnswers().entrySet()) {
                System.out.println(entryAnswer.getKey() + "." + entryAnswer.getValue());
            }
            playerGuesses.put(entryQuestion.getValue(), userInputManager.provideSelectedAnswer());
        }
        return playerGuesses;
    }


    //TODO check if the player has already entered their name and do not ask repeatedly

}