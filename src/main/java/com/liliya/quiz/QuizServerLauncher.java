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
         /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }*/

        try{
            Registry registry= LocateRegistry.createRegistry(1099);
            QuizServer server=new QuizServer();
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