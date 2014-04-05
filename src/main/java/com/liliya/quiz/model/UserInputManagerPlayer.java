package com.liliya.quiz.model;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Handles user input for  player functionality
 */

public class UserInputManagerPlayer {

   public int chooseQuiz(){
       System.out.print(">>");
       Scanner sc = new Scanner(System.in);
       return sc.nextInt();
   }

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
