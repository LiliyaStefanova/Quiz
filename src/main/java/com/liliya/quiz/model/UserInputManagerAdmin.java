package com.liliya.quiz.model;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * Methods which handle user input in the Set up client
 */
public class UserInputManagerAdmin {

    public String provideQuizName() {
        String quizName = "";
        do {
            try {
                System.out.print("Enter quiz name: ");
                Scanner sc1 = new Scanner(System.in);
                quizName = sc1.nextLine();
            } catch (IllegalArgumentException ex) {
                System.out.println("Please provide a quiz name");
            }
        } while (quizName.trim().equals(""));
        return quizName;
    }

    public int selectQuizToCloseFromList() {
        int choice = -1;
        do {
            try {
                System.out.print(">>");
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                throw new RuntimeException("You need to enter the quiz number", e);
            }
        } while (choice == -1);

        return choice;
    }

    public int setNumberOfQuestions() {
        try {
            System.out.print("Choose number of questions: ");
            Scanner sc1 = new Scanner(System.in);
            return sc1.nextInt();
        } catch (InputMismatchException ex) {
            throw new RuntimeException("Number of questions required", ex);
        }
    }

    public String provideQuestion() {
        String question = "";
        do {
            try {
                System.out.print("Enter question: ");
                Scanner sc2 = new Scanner(System.in);
                question = sc2.nextLine();
            } catch (IllegalArgumentException ex) {
                System.out.println("You must enter a question");
            }
        } while (question.trim().equals(""));
        return question;
    }

    public Map<Integer, String> providePossibleAnswers() {
        Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
        String answer = "";
        System.out.println("Enter possible answers: ");
        for (int i = 1; i <= 4; i++) {
            do {
                System.out.print("Possible answer " + i + ":");
                try {

                    Scanner sc3 = new Scanner(System.in);
                    answer = sc3.nextLine();
                } catch (IllegalArgumentException ex) {
                    System.out.println("Possible answer entry required");
                }
                possibleAnswers.put(i, answer);

            } while (answer.trim().equals(""));
        }
        return possibleAnswers;
    }

    public int provideCorrectAnswer() {
        int correctAnswer = 0;
        do {
            try {
                System.out.print("Enter correct answer(between 1 and 4): ");
                Scanner sc4 = new Scanner(System.in);
                correctAnswer = sc4.nextInt();
            } catch (IllegalArgumentException ex) {
                System.out.println("You need to enter the number of the correct answer");
            }
        } while (correctAnswer < 1 || correctAnswer > 4);
        return correctAnswer;
    }

    //a question can never have 0 points for a correct answer
    public int provideCorrectAnswerPoints() {
        int correctAnswerPoints = 0;
        do {
            try {
                System.out.print("Enter correct answer points: ");
                Scanner sc5 = new Scanner(System.in);
                correctAnswerPoints = sc5.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Answer points must be provided as number");
            }
        } while (correctAnswerPoints == 0);
        return correctAnswerPoints;
    }

    public String inputFileName() {
        String filename = "";
        System.out.print("Please provide a file path: ");
        do {
            try {
                Scanner sc1 = new Scanner(System.in);
                filename = sc1.nextLine();
            } catch (InputMismatchException ex) {
                throw new RuntimeException("Invalid file path");
            }
        } while (filename.trim().equals(""));
        return filename;
    }

    public int requestQuizId() {
        System.out.print("Enter quiz id: ");
        int quizId = -1;
        do {
            try {
                Scanner sc1 = new Scanner(System.in);
                quizId = sc1.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Quiz id must be number");
            }
        } while (quizId == -1);
        return quizId;
    }


}
