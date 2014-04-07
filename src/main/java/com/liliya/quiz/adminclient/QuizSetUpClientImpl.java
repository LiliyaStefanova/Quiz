package com.liliya.quiz.adminclient;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.*;
import com.liliya.quiz.model.UserInputManagerAdmin;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class QuizSetUpClientImpl implements QuizSetUpClient {


    boolean backToMain = false;
    private QuizService quizAdmin = null;

    private static final TextMenuItem setUpManually = new TextMenuItem("Set up quiz manually", MenuActions.SET_UP_QUIZ_MANUALLY);
    private static final TextMenuItem setUpFromFile = new TextMenuItem("Set up quiz from file", MenuActions.SET_UP_QUIZ_FROM_FILE);
    private static final TextMenuItem close = new TextMenuItem("Close quiz", MenuActions.CLOSE_QUIZ);
    private static final TextMenuItem back = new TextMenuItem("Go Back", MenuActions.BACK);
    private static final TextMenuItem quit = new TextMenuItem("Quit", MenuActions.QUIT);

    private static List<TextMenuItem> setUpClientMenu = new ArrayList<TextMenuItem>(Arrays.asList(setUpManually,
            setUpFromFile, close, back, quit));

    private UserInputManagerAdmin userInputManager;

    public QuizSetUpClientImpl(UserInputManagerAdmin userInputManager, QuizService service) {
        this.userInputManager = userInputManager;
        this.quizAdmin = service;
    }

    public static void main(String[] args) {

        QuizSetUpClient suc = new QuizSetUpClientImpl(new UserInputManagerAdmin(), null);
        suc.connectToServer();
        suc.menu();
    }

    public void connectToServer() {
        try {
            Remote service = Naming.lookup("//127.0.0.1:1699/quiz");
            quizAdmin = (QuizService) service;
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
            MenuActions action = TextMenu.display("Quiz Administrator", setUpClientMenu);
            switch (action) {
                case SET_UP_QUIZ_MANUALLY:
                    setUpQuizManually();
                    break;
                case SET_UP_QUIZ_FROM_FILE:
                    setUpQuizFromFile();
                    break;
                case CLOSE_QUIZ:
                    requestQuizClose();
                    break;
                case BACK:
                    backToMain = true;
                    QuizLauncher.launch();
                    break;
                case QUIT:
                    closeDownProgram();
                    break;
                default:
                    System.out.print("Choose a valid option");
            }

        } while (!backToMain);
    }

    @Override
    public void setUpQuizManually() {

        String quizName = userInputManager.provideQuizName();
        try {
            int quizID = quizAdmin.createNewQuiz(quizName, setUpQuestionsManually());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUpQuizFromFile() {
        String quizName = userInputManager.provideQuizName();
        try {
            int quizID = quizAdmin.createNewQuiz(quizName, setUpQuestionsFromFile());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void requestQuizClose() {
        try {
            PlayerQuizInstance winner = quizAdmin.closeQuiz(displayActiveQuizzes());
            displayQuizWinnerDetails(winner);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } /*catch (NoSuchElementException ex) {
            System.out.println("This quiz does not exist. Try again...");
        }*/
    }

    private int displayActiveQuizzes(){
        int choice = 0;
        System.out.println("Select from the currently available quizzes: ");
        try {
            List<Quiz> availableQuizzes = quizAdmin.getListAvailableQuizzes();
            if (availableQuizzes.isEmpty()) {
                System.out.println("There are no quizzes available at this time");
                menu();
            }
            for (Quiz current : availableQuizzes) {
                System.out.println(current.getQuizId() + ". " + current.getQuizName());
            }
            choice = userInputManager.selectQuizToCloseFromList();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch(InputMismatchException ex){
            throw new RuntimeException("Enter the quiz number", ex);
        }
        return choice;
    }

    //TODO prompt to ask user if sure they want to exit
    @Override
    public void closeDownProgram() {
        try{
        quizAdmin.flush();
        } catch(RemoteException ex){
            ex.printStackTrace();
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore) {
        //TODO sort out all exceptions
        System.out.print("The winner of the game is: ");
        try {
            System.out.println(playerWithHighestScore.getPlayer().getName());
            System.out.println("With a score of: " + playerWithHighestScore.getTotalScore());
        } catch (NullPointerException ex) {
            System.out.println("No one has played that quiz");
        }
    }

    private Map<Integer, Question> setUpQuestionsManually() {
        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        int correctAnswer = 0;
        int countQuestionEntries = userInputManager.setNumberOfQuestions();
        while (countQuestionEntries > 0) {
            Question newQuestion = new QuestionImpl(userInputManager.provideQuestion(),
                    userInputManager.providePossibleAnswers(),
                    userInputManager.provideCorrectAnswer(),
                    userInputManager.provideCorrectAnswerPoints());
            questions.put(countQuestionEntries, newQuestion);
            countQuestionEntries--;
        }
        return questions;
    }

    private Map<Integer, Question> setUpQuestionsFromFile() {

        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        String question;
        int correctAnswer = 0;
        int correctAnswerPoints;
        BufferedReader br = null;
        int lineCount = 0;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(userInputManager.inputFileName()));
            while ((sCurrentLine = br.readLine()) != null) {
                Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
                String[] questionParts = sCurrentLine.split(",");
                question = questionParts[0];
                for (int i = 1; i <= 4; i++) {
                    possibleAnswers.put(i, questionParts[i]);
                }
                correctAnswer = Integer.parseInt(questionParts[5]);
                correctAnswerPoints = Integer.parseInt(questionParts[6]);
                Question newQuestion = new QuestionImpl(question, possibleAnswers, correctAnswer, correctAnswerPoints);
                questions.put(lineCount, newQuestion);

                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return questions;

    }

}