package com.liliya.quiz.adminclient;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.*;
import com.liliya.quiz.model.UserInputManager;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class QuizSetUpClientImpl implements QuizSetUpClient {

    boolean backToMain = false;
    private QuizService quizPlayer = null;

    private static final TextMenuItem setUpManually = new TextMenuItem("Set up quiz manually", MenuActions.SET_UP_QUIZ_MANUALLY);
    private static final TextMenuItem setUpFromFile = new TextMenuItem("Set up quiz from file", MenuActions.SET_UP_QUIZ_FROM_FILE);
    private static final TextMenuItem close = new TextMenuItem("Close quiz", MenuActions.CLOSE_QUIZ);
    private static final TextMenuItem back = new TextMenuItem("Go Back", MenuActions.BACK);
    private static final TextMenuItem quit = new TextMenuItem("Quit", MenuActions.QUIT);

    private static List<TextMenuItem> setUpClientMenu = new ArrayList<TextMenuItem>(Arrays.asList(setUpManually,
            setUpFromFile, close, back, quit));

    private UserInputManager userInputManager = new UserInputManager();


    public static void main(String[] args) {

        QuizSetUpClient suc = new QuizSetUpClientImpl();
        suc.connectToServer();
        suc.menu();
    }

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
                    System.out.println("Goodbye!");
                    System.exit(0);
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
            int quizID = quizPlayer.generateQuiz(quizName, setUpQuestionsManually());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUpQuizFromFile() {
        String quizName = userInputManager.provideQuizName();
        try {
            int quizID = quizPlayer.generateQuiz(quizName, setUpQuestionsFromFile());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void requestQuizClose() {
        try {
            PlayerQuizInstance winner = quizPlayer.closeQuiz(userInputManager.requestQuizId());
            displayQuizWinnerDetails(winner);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.print("This quiz does not exist");
        }
    }

    @Override
    public void closeDownProgram() {
        try {
            quizPlayer.shutdown();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
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