package com.liliya.quiz.model;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class UserInputManager {

    //TODO input mismatch exceptions for all applicable methods
    /**
     * Methods which handle user input in the Set up client
     */
    public String provideQuizName() {
        String quizName = "";
        try {
            System.out.print("Enter quiz name: ");
            Scanner sc1 = new Scanner(System.in);
            quizName = sc1.nextLine();
        } catch (IllegalArgumentException ex) {
            System.out.println("Please provide a quiz name");
        }
        return quizName;
    }

    public int setNumberOfQuestions() {
        System.out.print("Choose number of questions: ");
        Scanner sc1 = new Scanner(System.in);
        return sc1.nextInt();
    }

    public String provideQuestion() {
        System.out.print("Enter question: ");
        Scanner sc2 = new Scanner(System.in);
        return sc2.nextLine();
    }

    public Map<Integer, String> providePossibleAnswers() {
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

    public int provideCorrectAnswer() {
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

    public int provideCorrectAnswerPoints() {
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


    public String inputFileName() {
        System.out.print("Please provide a file name: ");
        Scanner sc1 = new Scanner(System.in);
        return sc1.nextLine();
    }

    public int requestQuizId() {
        System.out.print("Enter quiz id: ");
        Scanner sc1 = new Scanner(System.in);
        return sc1.nextInt();

    }

    /**
     * Methods which handle user input for the Player client
     */

    public int provideSelectedAnswer(){
        int playerGuess=0;
        do{
            try{
                System.out.print("Your answer(type the answer number): ");
                Scanner sc = new Scanner(System.in);
                playerGuess = sc.nextInt();
                //TODO check if any of the answers provided are invalid

            } catch(InputMismatchException ex){
                System.out.println("Answer must be number of the question.Try again!");
            }
        } while(!answerRangeValidationCheck(playerGuess));

        return playerGuess;
    }

    private boolean answerRangeValidationCheck(int answer){
        boolean correct=true;
        if(answer<1 || answer>4){
            correct=false;
        }
        return correct;
    }

    public String providePlayerName(){
        System.out.print("Enter your name: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }


}
