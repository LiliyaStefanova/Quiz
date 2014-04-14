package com.liliya.quiz.playerclient;

import com.liliya.constants.UserMessages;
import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.*;

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.*;

/**
 * Implementation of the Player Client interface
 * Can look up quizzes available to play on the server
 * Can view up to top 3 highest scores
 */

public class QuizPlayerClientImpl implements QuizPlayerClient {

    private static final TextMenuItem selectFromList = new TextMenuItem("SELECT QUIZ TO PLAY", MenuActions.SELECT_QUIZ_FROM_LIST);
    private static final TextMenuItem viewHighScores = new TextMenuItem("VIEW HIGH SCORES", MenuActions.VIEW_HIGH_SCORES);
    private static final TextMenuItem quit = new TextMenuItem("QUIT", MenuActions.QUIT);

    private static List<TextMenuItem> playerMenu = new ArrayList<TextMenuItem>(Arrays.asList(selectFromList, viewHighScores, quit));

    QuizService quizPlayer = null;
    String playerName = "";

    UserInputManagerPlayer userInputManager;

    public QuizPlayerClientImpl(UserInputManagerPlayer userInputManager, QuizService server) {
        this.userInputManager = userInputManager;
        this.quizPlayer = server;

    }

    public static void main(String[] args) {

        QuizPlayerClient newPlayerClient = new QuizPlayerClientImpl(new UserInputManagerPlayer(), null);
        newPlayerClient.connectToService();
        newPlayerClient.mainMenu();
    }

    @Override
    public void connectToService() {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
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

    public void mainMenu() {
        do {
            System.out.println();
            MenuActions action = userInputManager.showMenu("QUIZ PLAYER MENU", playerMenu);
            switch (action) {
                case SELECT_QUIZ_FROM_LIST:
                    playQuiz(selectQuizToPlay());
                    break;
                case VIEW_HIGH_SCORES:
                    viewHighScores();
                    break;
                case QUIT:
                    closeDownProgram();
                    break;
                default:
                    System.out.print("Choose a valid option");
            }

        } while (true);
    }

    //TODO needs to fix this to ensure that the player client is not calling menu directly
    @Override
    public int selectQuizToPlay() {

        int choice = 0;
        System.out.println(UserMessages.SELECT_QUIZ);
        try {
            List<Quiz> availableQuizzes = quizPlayer.getListAvailableQuizzes();
            if (availableQuizzes.isEmpty()) {
                System.out.println(UserMessages.NO_QUIZZES_AVAILABLE);
                mainMenu();
            }
            for (Quiz current : availableQuizzes) {
                System.out.println(current.getQuizId() + ". " + current.getQuizName());
            }
            choice = userInputManager.selectQuizToPlayFromList();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (InputMismatchException ex) {
            throw new RuntimeException("Enter the quiz number", ex);
        }
        System.out.println();
        return choice;
    }

    @Override
    public void playQuiz(int id) {
        while (playerName.equals("")) {
            playerName = userInputManager.providePlayerName();
        }
        try {
            PlayerQuizInstance newInstanceQuizPlayer = quizPlayer.loadQuizForPlay(id, playerName);
            if (newInstanceQuizPlayer == null) {
                System.out.println(UserMessages.NOT_AVAILABLE);
                System.out.println();
                return;
            }
            Map<Question, Integer> userGuesses = submitAnswersForScoring(newInstanceQuizPlayer.getQuiz());
            System.out.print(UserMessages.FINAL_SCORE + quizPlayer.calculatePlayerScore(newInstanceQuizPlayer, userGuesses));
            System.out.println();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Returns only top 3 scores or all scores if less are available for a player
    @Override
    public void viewHighScores() {
        List<PlayerQuizInstance> quizInstancesForPlayer = new ArrayList<PlayerQuizInstance>();
        if (playerName.equals("")) {
            playerName = userInputManager.providePlayerName();
        }
        try {
            quizInstancesForPlayer = quizPlayer.getQuizzesPlayedByPlayer(playerName);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        System.out.println(UserMessages.SCORES);
        System.out.printf("%-20s%-15s%n", "Quiz", "Score");
        System.out.println("----------------------------");
        for (PlayerQuizInstance currentInstance : findTopScores(quizInstancesForPlayer)) {
            System.out.printf("%-20s%-15d%n", currentInstance.getQuiz().getQuizName(), currentInstance.getTotalScore());
        }
        System.out.println();
    }

    @Override
    public void closeDownProgram() {
        if (userInputManager.confirmExit().equalsIgnoreCase("y")) {
            System.out.println(UserMessages.GOODBYE);
            System.exit(0);
        } else {
            return;
        }
    }

    Map<Question, Integer> submitAnswersForScoring(Quiz quizPlayed) {
        Map<Question, Integer> playerGuesses = new HashMap<Question, Integer>();
        for (Map.Entry<Integer, Question> currentQuestion : quizPlayed.getQuizQuestions().entrySet()) {
            System.out.println(currentQuestion.getKey() + "." + currentQuestion.getValue().getQuestion());
            for (Map.Entry<Integer, String> currentPossAns : currentQuestion.getValue().getPossibleAnswers().entrySet()) {
                System.out.println("\t" + currentPossAns.getKey() + "." + currentPossAns.getValue());
            }
            playerGuesses.put(currentQuestion.getValue(), userInputManager.provideSelectedAnswer());

        }
        return playerGuesses;
    }

    List<PlayerQuizInstance> findTopScores(List<PlayerQuizInstance> quizInstancesForPlayer) {

        List<PlayerQuizInstance> topScores = new ArrayList<PlayerQuizInstance>();

        Collections.sort(quizInstancesForPlayer, new Comparator<PlayerQuizInstance>() {
            @Override
            public int compare(PlayerQuizInstance o1, PlayerQuizInstance o2) {
                return o2.getTotalScore() - o1.getTotalScore();
            }
        });
        if (quizInstancesForPlayer.size() == 0) {
            System.out.println(UserMessages.NO_QUIZZES_PLAYED);
        } else if (quizInstancesForPlayer.size() < 3) {
            topScores = quizInstancesForPlayer;
        } else {
            topScores = quizInstancesForPlayer.subList(0, 3);
        }
        return topScores;
    }

}