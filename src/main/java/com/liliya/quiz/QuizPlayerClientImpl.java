package com.liliya.quiz;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This is the player client which will be able to play available quizzes
 */

public class QuizPlayerClientImpl {

    public static void main(String [] args){


       /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }
*/
        try{
            Remote service= Naming.lookup("//127.0.0.1:1699/quiz");
            QuizService quizPlayer=(QuizService) service;
            List<Quiz> currentQuizList=quizPlayer.getListCurrentQuizzes();
            for(Quiz curr: currentQuizList){
                System.out.println(curr.getQuizName());
            }
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
}