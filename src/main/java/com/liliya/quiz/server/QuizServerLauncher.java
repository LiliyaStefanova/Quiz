package com.liliya.quiz.server;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.QuizService;
import com.liliya.quiz.model.Serializer;

import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class QuizServerLauncher {

    private static final String SERVICE_NAME = "quiz";

    private static Logger serverLogger = Logger.getLogger(QuizServer.class.getName());

    private static final TextMenuItem startServer = new TextMenuItem("LAUNCH SERVER", MenuActions.LAUNCH_SERVER);
    private static final TextMenuItem quit= new TextMenuItem("QUIT MENU", MenuActions.QUIT);

    private static List<TextMenuItem> serverMenu = new ArrayList<TextMenuItem>(Arrays.asList(startServer, quit));

    private QuizService service;

    public static void main(String[] args) {

        QuizServerLauncher qsl = new QuizServerLauncher();
        qsl.launchMainMenuServer();
    }

    public void launchMainMenuServer() {
        boolean menuActive=true;

        do {
            MenuActions action = TextMenu.display("QUIZ SERVER MENU", serverMenu);
            switch (action) {
                case LAUNCH_SERVER:
                    launchServer();
                    break;
                case QUIT:              //can be used to close menu when the server is shut down
                    menuActive=false;
                    break;
                default:
                    System.out.print("Choose a valid option");
            }
        } while (menuActive);
    }


    public void launchServer() {

        /* if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }*/

        try {
            serverLogger.info("Loading server...");
            Registry registry = LocateRegistry.createRegistry(1699);
            Serializer serializer = new Serializer();
            service = serializer.decodeData();
            String registryHost = "//localhost/";
            registry.rebind(SERVICE_NAME, service);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (AlreadyBoundException ex) {
            ex.printStackTrace();
        }
    }

}
