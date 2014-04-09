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
        //TODO sort out the security manager
       /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }
        */
        QuizPlayerClient newPlayerClient = new QuizPlayerClientImpl(new UserInputManagerPlayer(), null);
        newPlayerClient.connectToService();
        newPlayerClient.mainMenu();
    }

    @Override
    public void connectToService() {
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
            MenuActions action = TextMenu.display("QUIZ PLAYER MENU", playerMenu);
            switch (action) {
                case SELECT_QUIZ_FROM_LIST:
                    playQuiz(selectQuizToPlay());
                    break;
                case VIEW_HIGH_SCORES:
                    viewHighScores();
                    break;
                case QUIT:
                    closeDownProgram();
                default:
                    System.out.print("Choose a valid option");
            }
            System.out.println();

        } while (true);
    }

    @Override
    public int selectQuizToPlay() {

        int choice = 0;
        System.out.println("Select from currently available quizzes: ");
        try {
            List<Quiz> availableQuizzes = quizPlayer.getListAvailableQuizzes();
            if (availableQuizzes.isEmpty()) {
                System.out.println("No quizzes available at this time");
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
            Map<Question, Integer> userGuesses = submitAnswersForScoring(newInstanceQuizPlayer.getQuiz());
            System.out.println();
            System.out.print("Thank you for your responses. Your final score is: ");
            System.out.println(quizPlayer.calculatePlayerScore(newInstanceQuizPlayer, userGuesses));
            System.out.println();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    //TODO sort out the formatting of the scores
    @Override
    public void viewHighScores() {
        if (playerName.equals("")) {
            playerName = userInputManager.providePlayerName();
        }
        List<PlayerQuizInstance> quizInstancesForPlayer = new ArrayList<PlayerQuizInstance>();
        List<PlayerQuizInstance> topScores = new ArrayList<PlayerQuizInstance>();
        try {
            quizInstancesForPlayer = quizPlayer.getQuizzesPlayedByPlayer(playerName);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        Collections.sort(quizInstancesForPlayer, new Comparator<PlayerQuizInstance>() {
            @Override
            public int compare(PlayerQuizInstance o1, PlayerQuizInstance o2) {
                return o2.getTotalScore() - o1.getTotalScore();
            }
        });
        //will return only top 3 scores or all scores if less are available
        if (quizInstancesForPlayer.size() == 0) {
            System.out.println("You have not played any quizzes");
        } else if (quizInstancesForPlayer.size() < 3) {
            topScores = quizInstancesForPlayer;
        } else {
            topScores = quizInstancesForPlayer.subList(0, 3);
        }
        System.out.println("Top scores so far: ");
        System.out.println("Quiz"+ "\t\t\t\t"+ "Score");
        for (PlayerQuizInstance currentInstance : topScores) {
            System.out.println(currentInstance.getQuiz().getQuizName() +"\t\t\t\t"+ currentInstance.getTotalScore());
        }
    }

    @Override
    public void closeDownProgram() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    Map<Question, Integer> submitAnswersForScoring(Quiz quizPlayed) {
        Map<Integer, Question> quizQuestions = quizPlayed.getQuizQuestions();
        Map<Question, Integer> playerGuesses = new HashMap<Question, Integer>();
        for (Map.Entry<Integer, Question> currentQuestion : quizPlayed.getQuizQuestions().entrySet()) {
            System.out.println(currentQuestion.getKey() + ". " + currentQuestion.getValue().getQuestion());
            for (Map.Entry<Integer, String> currentPossAns : currentQuestion.getValue().getPossibleAnswers().entrySet()) {
                System.out.println(currentPossAns.getKey() + "." + currentPossAns.getValue());
            }
            playerGuesses.put(currentQuestion.getValue(), userInputManager.provideSelectedAnswer());

        }
        return playerGuesses;
    }

}