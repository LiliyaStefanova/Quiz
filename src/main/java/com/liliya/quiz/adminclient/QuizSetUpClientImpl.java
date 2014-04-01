package com.liliya.quiz.adminclient;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.*;

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
            setUpFromFile,close, back,quit));

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
                    try{
                    System.out.print("Enter quiz id: ");
                    Scanner sc1 = new Scanner(System.in);
                    int quizID = sc1.nextInt();
                    requestQuizClose(quizID);
                    } catch(IllegalArgumentException ex){
                        System.out.print("This quiz does not exist");
                    }
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
    public void setUpQuizFromFile() {
        String quizName=requestQuizName();
        try {
            int quizID = quizPlayer.generateQuiz(quizName, setUpQuestionsFromFile());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUpQuizManually() {

        String quizName=requestQuizName();
        try {
            int quizID = quizPlayer.generateQuiz(quizName, setUpQuestionsManually());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void requestQuizClose(int quizID) {
        try {
            PlayerQuizInstance winner = quizPlayer.closeQuiz(quizID);
            displayQuizWinnerDetails(winner);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    //make this private and remove from interface
    @Override
    public void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore) {
        //TODO sort out all exceptions
        System.out.print("The winner of the game is: ");
        try{
        System.out.println(playerWithHighestScore.getPlayer().getName());
        System.out.println("With a score of: " + playerWithHighestScore.getTotalScore());
        } catch (NullPointerException ex){
            System.out.println("No one has played that quiz");
        }


    }

    @Override
    public void closeDownProgram() {
        try{
        quizPlayer.shutdown();
        } catch( RemoteException ex){
            ex.printStackTrace();
        }
    }

    private String requestQuizName(){
        String quizName="";
        try{
        System.out.print("Enter quiz name: ");
        Scanner sc1 = new Scanner(System.in);
         quizName=sc1.nextLine();
        } catch(IllegalArgumentException ex){
            System.out.println("Please provide a quiz name");
        }
        return quizName;
    }

    private Map<Integer, Question> setUpQuestionsManually() {
        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        int correctAnswer = 0;
        System.out.print("Choose number of questions: ");
        Scanner sc1 = new Scanner(System.in);
        int numQuestions = sc1.nextInt();
        String[] answers = new String[numQuestions];
        int count = 0;
        while (count < numQuestions) {
            Question newQuestion = new QuestionImpl(createQuestion(),
                    createPossibleAnswers(),
                    setUpCorrectAnswer(),
                    setCorrectAnswerPoints());
            questions.put(count, newQuestion);
            count++;
        }
        return questions;
    }

    private Map<Integer, Question> setUpQuestionsFromFile() {
        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
        String question;
        int correctAnswer = 0;
        int correctAnswerPoints;
        BufferedReader br = null;
        int lineCount = 0;
        System.out.print("Please provide a file name: ");
        Scanner sc1 = new Scanner(System.in);
        String questionFile = sc1.nextLine();
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(questionFile));
            while ((sCurrentLine = br.readLine()) != null) {
                String[] questionParts = sCurrentLine.split(",");
                question = questionParts[0];
                for (int i = 1; i <= 4; i++) {
                    possibleAnswers.put(i - 1, questionParts[i]);
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


    private String createQuestion() {
        System.out.print("Enter question: ");
        Scanner sc2 = new Scanner(System.in);
        return sc2.nextLine();
    }

    private Map<Integer, String> createPossibleAnswers() {
        Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
        System.out.println("Enter possible answers: ");
        for (int i = 1; i <= 4; i++) {
            System.out.print("Possible answer " + i + ":");
            Scanner sc3 = new Scanner(System.in);
            String answer = sc3.nextLine();
            possibleAnswers.put(i, answer);
        }
        return possibleAnswers;
    }

    private int setUpCorrectAnswer() {
        int correctAnswer = 0;
        System.out.print("Enter correct answer: ");
        try {
            Scanner sc4 = new Scanner(System.in);
            correctAnswer = sc4.nextInt();
            if (correctAnswer < 1 || correctAnswer > 4) {
                //need to double check if exception can be thrown here
                throw new IllegalArgumentException("Correct answer must be between 1 and 4");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("You need to enter the number of the correct answer");
        }
        return correctAnswer;
    }

    private int setCorrectAnswerPoints() {
        int correctAnswerPoints = 0;
        try {
            System.out.print("Enter correct answer points: ");
            Scanner sc5 = new Scanner(System.in);
            correctAnswerPoints = sc5.nextInt();

        } catch (IllegalArgumentException ex) {
            System.out.println("Please provide points as a number");
        }
        return correctAnswerPoints;
    }


}