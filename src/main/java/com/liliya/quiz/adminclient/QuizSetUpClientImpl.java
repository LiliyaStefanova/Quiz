package com.liliya.quiz.adminclient;

import com.liliya.constants.ExceptionMsg;
import com.liliya.constants.UserDialog;
import com.liliya.exceptions.ChangedMyMindException;
import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.*;
import com.liliya.quiz.model.UserInputManagerAdmin;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.*;
import java.util.logging.Logger;

public class QuizSetUpClientImpl implements QuizSetUpClient {


    private QuizService quizService = null;

    private static final String SERVICE_NAME = "quiz";

    private static Logger clientLogger = Logger.getLogger(QuizSetUpClientImpl.class.getName());

    private static final TextMenuItem setUpManually = new TextMenuItem("MANUAL QUIZ SET UP", MenuActions.SET_UP_QUIZ_MANUALLY);
    private static final TextMenuItem setUpFromFile = new TextMenuItem("SET UP QUIZ FROM FILE", MenuActions.SET_UP_QUIZ_FROM_FILE);
    private static final TextMenuItem close = new TextMenuItem("CLOSE QUIZ", MenuActions.CLOSE_QUIZ);
    private static final TextMenuItem shutdownServer = new TextMenuItem("SAVE TO FILE AND SHUTDOWN SERVER", MenuActions.SHUTDOWN_SERVER);
    private static final TextMenuItem quit = new TextMenuItem("QUIT", MenuActions.QUIT);

    private static List<TextMenuItem> setUpClientMenu = new ArrayList<TextMenuItem>(Arrays.asList(setUpManually, setUpFromFile, close,
            shutdownServer, quit));

    private UserInputManagerAdmin userInputManager;

    public QuizSetUpClientImpl(UserInputManagerAdmin userInputManager, QuizService service) {
        this.userInputManager = userInputManager;
        this.quizService = service;
    }

    public static void main(String[] args) {

        QuizSetUpClient suc = new QuizSetUpClientImpl(new UserInputManagerAdmin(), null);
        suc.connectToService();
        suc.menu();
    }

    public void connectToService() {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            Remote service = Naming.lookup("//127.0.0.1:1699/quiz");
            quizService = (QuizService) service;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void menu() {
        do {
            System.out.println();
            MenuActions action = userInputManager.showMenu("QUIZ ADMINISTRATOR MENU", setUpClientMenu);
            try {
                switch (action) {
                    case SET_UP_QUIZ_MANUALLY:
                        setUpNewQuizManually();
                        break;
                    case SET_UP_QUIZ_FROM_FILE:
                        setUpNewQuizFromFile();
                        break;
                    case CLOSE_QUIZ:
                        requestQuizClose();
                        break;
                    case SHUTDOWN_SERVER:
                        closeDownServer();
                        break;
                    case QUIT:
                        closeDownClient();
                        break;
                    default:
                        System.out.print(ExceptionMsg.CHOOSE_VALID_OPTION);
                }
            } catch (ChangedMyMindException ex) {
                //do nothing-just return to menu
            }

        } while (true);
    }

    @Override
    public void setUpNewQuizManually() {

        String quizName = userInputManager.provideNewQuizName();
        try {
            int quizID = quizService.createNewQuiz(quizName, generateQuizQuestionsManually());
            System.out.println("ID for quiz \"" + quizName + "\" is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUpNewQuizFromFile() {
        String quizName = userInputManager.provideNewQuizName();
        try {
            int quizID = quizService.createNewQuiz(quizName, generateQuizQuestionsFromFile());
            System.out.println("ID for quiz \"" + quizName + "\" is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void requestQuizClose() {
        try {
            int quizIDSelected = displayActiveQuizzes();
            if (quizIDSelected == -1) {
                return;
            }
            if (quizService.checkQuizPlayed(quizIDSelected)) {
                System.out.println(UserDialog.QUIZ_STILL_PLAYED);
                return;
            } else {
                List<PlayerQuizInstance> winners = quizService.closeQuiz(quizIDSelected);
                displayQuizWinnersDetails(winners);
                System.out.println(UserDialog.QUIZ_NOW_CLOSED);
            }

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void closeDownServer() {
        try {
            if (userInputManager.confirmExit().equalsIgnoreCase("y")) {
                clientLogger.info("Writing to file...");
                quizService.flush();
                clientLogger.info("Shutting down server...");
                quizService.shutDown();
            } else {
                return;
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void closeDownClient() {
        if (userInputManager.confirmExit().equalsIgnoreCase("y")) {
            clientLogger.info("Shutting down client...");
            System.out.println(UserDialog.GOODBYE);
            System.exit(0);
        } else {
            return;
        }
    }

    private void displayQuizWinnersDetails(List<PlayerQuizInstance> playersWithHighestScore) {
        System.out.println(UserDialog.WINNERS_OF_THE_QUIZ);
        if (playersWithHighestScore.isEmpty()) {
            System.out.println(UserDialog.NO_ONE_HAS_PLAYED_QUIZ);
        } else {
            System.out.printf("%-12s%-12s%n", "Player", "Total Score");
            for (PlayerQuizInstance current : playersWithHighestScore) {
                System.out.printf("%-12s%-12d%n", current.getPlayer().getName(), current.getTotalScore());
            }
            System.out.println();
        }
    }

    private int displayActiveQuizzes() {
        int choice = 0;
        System.out.println("Select quiz: ");
        try {
            List<Quiz> availableQuizzes = quizService.getListAvailableQuizzes();
            if (availableQuizzes.isEmpty()) {
                System.out.println(UserDialog.NO_QUIZZES_AVAILABLE);
                return -1;
            }
            for (Quiz current : availableQuizzes) {
                System.out.println(current.getQuizId() + ". " + current.getQuizName());
            }
            choice = userInputManager.selectExistingQuizToClose();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (InputMismatchException ex) {
            throw new RuntimeException(ExceptionMsg.ENTER_QUIZ_NUMBER, ex);
        }
        return choice;
    }


    private Map<Integer, Question> generateQuizQuestionsManually() {

        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        int countQuestionEntries = userInputManager.setNewQuizNumQuestions();
        while (countQuestionEntries > 0) {
            Question newQuestion = new QuestionImpl(userInputManager.inputNewQuizQuestion(),
                    userInputManager.inputNewQuizPossAnswers(),
                    userInputManager.inputNewQuizCorrAnswer(),
                    userInputManager.inputNewQuizCorrAnsPts());
            questions.put(countQuestionEntries, newQuestion);
            countQuestionEntries--;
        }
        if (questions.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMsg.QUESTIONS_EMPTY);
        }
        return questions;
    }

    private Map<Integer, Question> generateQuizQuestionsFromFile() {

        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        String question;
        BufferedReader br = null;
        int lineCount = 0;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(userInputManager.inputNewQuizFilePath()));
            while ((sCurrentLine = br.readLine()) != null) {
                Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
                String[] questionParts = sCurrentLine.split(",");
                question = questionParts[0];
                for (int i = 1; i <= 4; i++) {
                    possibleAnswers.put(i, questionParts[i]);
                }
                int correctAnswer = Integer.parseInt(questionParts[5]);
                int correctAnswerPoints = Integer.parseInt(questionParts[6]);
                Question newQuestion = new QuestionImpl(question, possibleAnswers, correctAnswer, correctAnswerPoints);
                questions.put(lineCount, newQuestion);

                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return questions;

    }

}