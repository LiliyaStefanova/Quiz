package com.liliya.quiz;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class QuizSetUpClientImpl implements QuizSetUpClient {

    boolean backToMain = false;
    private QuizService quizPlayer = null;

    public static void main(String[] args) {

        QuizSetUpClient suc = new QuizSetUpClientImpl();
        suc.connectToServer();
        suc.menu();
    }

    public void connectToServer(){
        try {
            Remote service = Naming.lookup("//127.0.0.1:1699/quiz");
            quizPlayer = (QuizService) service;
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void menu() {
        //add option to set up quiz from file and one manually
        do {
            System.out.println("What would you like to do? ");
            System.out.println(
                    "Select an option: \n" +
                            "  1) Set up a quiz\n" +
                            "  2) Close a quiz\n" +
                            "  3) View players for a quiz \n" +
                            "  4) Return to main menu "
            );
            System.out.println(">> ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    setUpQuiz();
                    break;
                case 2:
                    System.out.print("Enter quiz id: ");
                    Scanner sc1 = new Scanner(System.in);
                    int quizID = sc1.nextInt();
                    requestQuizClose(quizID);
                    break;
                case 3:

                    break;
                case 4:
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
            try{
                int quizID = quizPlayer.generateQuiz(quizName, setUpQuestions());
                System.out.println("ID for quiz " + quizName + " is: " + quizID);
            }
            catch(RemoteException ex){
                ex.printStackTrace();
            }
    }

    @Override
    public void requestQuizClose(int quizID) {
            try{
            PlayerQuizInstance winner = quizPlayer.closeQuiz(quizID);
            displayQuizWinnerDetails(winner);
            }
            catch(RemoteException ex){
                ex.printStackTrace();
            }
    }

    @Override
    public void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore) {
        try {
            System.out.print("The winner of the game is: ");
            System.out.print(playerWithHighestScore.getPlayer().getName());
            System.out.print("With a score of: ");
            playerWithHighestScore.getTotalScore();
        } catch (NullPointerException ex) {
            System.out.println("No one has played that game");
        }
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
            System.out.print("Enter correct answer: ");
            try {
                Scanner sc4 = new Scanner(System.in);
                correctAnswer = sc4.nextInt();
            } catch (IllegalArgumentException ex) {
                System.out.println("You need to enter the number of the correct answer");
            }

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