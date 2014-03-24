package com.liliya.quiz;

import java.nio.channels.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class QuizServerLauncher {


    public static void main(String [] args){

        QuizServerLauncher qsl=new QuizServerLauncher();
        qsl.launch();
    }

    public void launch(){

     /*   Question questionCapital;
        Question questionRiver;
        String question1="What is the capital of Australia?";
        String question2="What is the longest rive in the world?";
        String [] possibleAnswers1={"Canberra", "Melbourne", "Darwin", "Singapore" };
        String [] possibleAnswers2={"The Nile", "The Amazon", "Orinoko", "Mississippi" };
        String correctAnswer1="a";
        String correctAnswer2="a";
        int pointsAwarded1=5;
        int pointsAwarded2=7;
        questionCapital=new QuestionImpl(question1, possibleAnswers1, correctAnswer1, pointsAwarded1);
        questionRiver=new QuestionImpl(question2, possibleAnswers2, correctAnswer2, pointsAwarded2);
        Question [] questions={questionCapital, questionRiver};*/


         /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }*/

        try{
            Registry registry= LocateRegistry.createRegistry(1699);
            QuizServer server=new QuizServer();
            //server.generateQuiz("Geography", questions);
            //server.addNewPlayer("Ivan");
            String registryHost="//localhost/";
            String serviceName="quiz";
            registry.rebind(serviceName, server);
        }
        catch(RemoteException ex){
            ex.printStackTrace();
        }
        catch(AlreadyBoundException ex){
            ex.printStackTrace();
        }
    }


}