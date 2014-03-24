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

    public static void main(String [] args){

        QuizSetUpClient suc=new QuizSetUpClientImpl();
        suc.displayUserOptionsMenu();

    }

    @Override
    public void displayUserOptionsMenu() {
        //add option to set up quiz from file and one manually
        System.out.println("What would you like to do? ");
        System.out.println("Set up quiz-->0");
        System.out.println("Close quiz-->1");
        System.out.println("Exit-->2");
        System.out.print("-->");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();

        switch(choice){
            case 0:
                setUpQuiz();
                break;
            case 1:
                System.out.print("Enter quiz id: ");
                Scanner sc1=new Scanner(System.in);
                int quizID=sc1.nextInt();
                closeQuiz(quizID);
                break;
            case 2:
                System.exit(1);
                break;
            default:
                System.out.print("Choose a valid option");
        }

    }

    @Override
    public void setUpQuiz() {
        try{
            Remote service= Naming.lookup("//127.0.0.1:1699/quiz");
            QuizService quizPlayer=(QuizService) service;
            System.out.print("Enter quiz name: ");
            Scanner sc1=new Scanner(System.in);
            String quizName=sc1.nextLine();

           quizPlayer.generateQuiz(quizName, setUpQuestions());
        }
        catch(MalformedURLException ex){
            ex.printStackTrace();
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(NotBoundException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void closeQuiz(int quizID) {

        try{
            Remote service= Naming.lookup("//127.0.0.1:1699/quiz");
            QuizService quizPlayer=(QuizService) service;
            PlayerQuizInstance winner=quizPlayer.closeQuiz(quizID);
            displayQuizWinnerDetails(winner);
        }
        catch(MalformedURLException ex){
            ex.printStackTrace();
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(NotBoundException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void displayQuizWinnerDetails(PlayerQuizInstance playerWithHighestScore) {
        System.out.print("The winner of the game is: ");
        System.out.print(playerWithHighestScore.getPlayer().getName());
        System.out.print("With a score of: ");
        playerWithHighestScore.getTotalScore();
    }

    private Map<Integer, Question> setUpQuestions(){
        Map<Integer, Question> questions=new HashMap<Integer, Question>();
        System.out.print("Choose number of questions: ");
        Scanner sc1=new Scanner(System.in);
        int numQuestions=sc1.nextInt();
        String [] answers=new String[numQuestions];
        int count=1;
        while(count<=numQuestions){
            System.out.print("Enter question: ");
            Scanner sc2=new Scanner(System.in);
            String question=sc2.nextLine();
            System.out.print("Enter possible answers: ");
            for(int i=0; i>3; i++){
                Scanner sc3=new Scanner(System.in);
                String answer=sc1.nextLine();
                answers[i]=answer;
            }
            System.out.print("Enter correct answer: ");
            Scanner sc4=new Scanner(System.in);
            String correctAnswer=sc4.nextLine();
            System.out.print("Enter correct answer points: ");
            Scanner sc5=new Scanner(System.in);
            int correctAnswerPoints=sc5.nextInt();
            Question newQuestion=new QuestionImpl(question, answers, correctAnswer, correctAnswerPoints);
            questions.put(count, newQuestion);
        }

        return questions;
    }
}