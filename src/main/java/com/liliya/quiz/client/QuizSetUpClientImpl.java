package com.liliya.quiz.client;

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

public class QuizSetUpClientImpl implements QuizSetUpClient {

    boolean backToMain = false;
    private QuizService quizPlayer = null;

    private static final TextMenuItem setUp = new TextMenuItem("Set up quiz", MenuActions.SET_UP_QUIZ);
    private static final TextMenuItem close = new TextMenuItem("Close quiz", MenuActions.CLOSE_QUIZ);
    private static final TextMenuItem back = new TextMenuItem("Go Back", MenuActions.BACK);

    private static List<TextMenuItem> setUpClientMenu = new ArrayList<TextMenuItem>(Arrays.asList(setUp, close, back));

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
        //TODO add option to set up quiz from file and one manually
        do {
            MenuActions action = TextMenu.display("Quiz Administrator", setUpClientMenu);
            switch (action) {
                case SET_UP_QUIZ:
                    setUpQuiz();
                    break;
                case CLOSE_QUIZ:
                    //TODO exception if quiz id does not exist
                    System.out.print("Enter quiz id: ");
                    Scanner sc1 = new Scanner(System.in);
                    int quizID = sc1.nextInt();
                    requestQuizClose(quizID);
                    break;
                case BACK:
                    backToMain = true;
                    QuizLauncher.launch();
                    break;
                default:
                    System.out.print("Choose a valid option");
            }

        } while (!backToMain);
    }

    @Override
    public void setUpQuiz() {

        System.out.print("Enter quiz name: ");
        Scanner sc1 = new Scanner(System.in);
        String quizName = sc1.nextLine();
        try {
            int quizID = quizPlayer.generateQuiz(quizName, setUpQuestions());
            System.out.println("ID for quiz " + quizName + " is: " + quizID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void requestQuizClose(int quizID) {
        try{
            PlayerQuizInstance winner = quizPlayer.closeQuiz(quizID);
            displayQuizWinnerDetails(winner);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore) {
        //TODO sort out all exceptions
            System.out.print("The winner of the game is: ");
            System.out.println(playerWithHighestScore.getPlayer().getName());
            System.out.println("With a score of: " + playerWithHighestScore.getTotalScore());
        }


    private Map<Integer, Question> setUpQuestions() {
        Map<Integer, Question> questions = new HashMap<Integer, Question>();
        int correctAnswer = 0;
        System.out.print("Choose number of questions: ");
        Scanner sc1 = new Scanner(System.in);
        int numQuestions = sc1.nextInt();
        String[] answers = new String[numQuestions];
        int count = 0;
        while (count < numQuestions) {
            Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
            System.out.print("Enter question: ");
            Scanner sc2 = new Scanner(System.in);
            String question = sc2.nextLine();
            System.out.println("Enter possible answers: ");
            for (int i = 1; i <= 4; i++) {
                System.out.print("Possible answer " + i + ":");
                Scanner sc3 = new Scanner(System.in);
                String answer = sc3.nextLine();
                possibleAnswers.put(i, answer);
            }
            //TODO validation on the range of the correct answer-must be between 1 and 4
            System.out.print("Enter correct answer: ");
            try {
                Scanner sc4 = new Scanner(System.in);
                correctAnswer = sc4.nextInt();
            } catch (IllegalArgumentException ex) {
                System.out.println("You need to enter the number of the correct answer");
            }
            //TODO validation on the range of correct answer points if any; must be number
            System.out.print("Enter correct answer points: ");
            Scanner sc5 = new Scanner(System.in);
            int correctAnswerPoints = sc5.nextInt();
            Question newQuestion = new QuestionImpl(question, possibleAnswers, correctAnswer, correctAnswerPoints);
            questions.put(count, newQuestion);
            count++;
        }

        return questions;
    }
}