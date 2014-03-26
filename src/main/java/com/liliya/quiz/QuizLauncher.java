package com.liliya.quiz;


import java.util.Scanner;

public class QuizLauncher {

    public static void main(String [] args){


      // QuizLauncher quizLauncher=new QuizLauncher();

    }

    public static void launch(){
        //need to fix this to use string
        System.out.println("Are you  admin(1) or player(2), exit(3)?");
        Scanner sc=new Scanner(System.in);
        int userType=sc.nextInt();

        switch(userType){
            case 1:
                QuizServerLauncher qsl = new QuizServerLauncher();
                qsl.launch();
                QuizSetUpClient suc = new QuizSetUpClientImpl();
                suc.menu();
                break;
            case 2:
                QuizServerLauncher qsl1= new QuizServerLauncher();
                qsl1.launch();
                QuizPlayerClient newPlayerClient = new QuizPlayerClientImpl();
                newPlayerClient.launchMainMenuPlayer();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Please enter a valid option");
        }


    }


}
