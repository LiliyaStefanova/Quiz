package com.liliya.quiz;


import java.nio.channels.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.Scanner;

public class QuizLauncher {

    public static void main(String [] args){

     QuizLauncher.launch();

    }

    public static void launch(){

    System.out.println(
                "Select an option: \n" +
                        "  1) Quiz administration\n" +
                        "  2) Play\n" +
                        "  3) Exit game \n"

        );
        System.out.println(">> ");
        Scanner sc=new Scanner(System.in);
        int userType=sc.nextInt();

        switch(userType){
            case 1:
                QuizSetUpClient suc = new QuizSetUpClientImpl();
                suc.menu();
                break;
            case 2:
                QuizPlayerClient newPlayerClient = new QuizPlayerClientImpl();
                newPlayerClient.launchMainMenuPlayer();
                break;
            case 3:
                try{
               // server.flush();
                }
                catch(NullPointerException ex){
                    System.out.println("Server does not exist");
                }
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Please enter a valid option");
        }


    }


}
