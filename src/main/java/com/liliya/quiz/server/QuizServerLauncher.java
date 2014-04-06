package com.liliya.quiz.server;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.QuizService;
import com.liliya.quiz.model.Serializer;

import java.nio.channels.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class QuizServerLauncher {

    private static final  String SERVICE_NAME = "quiz";

    private static Logger serverLogger=Logger.getLogger(QuizServer.class.getName());

    private static final TextMenuItem startServer = new TextMenuItem("Launch quiz server", MenuActions.LAUNCH_SERVER);
    private static final TextMenuItem shutDownServer= new TextMenuItem("Shutdown server", MenuActions.SHUTDOWN_SERVER);
    private static final TextMenuItem writeToFIle = new TextMenuItem("Write to file", MenuActions.WRITE_TO_FILE);

    private static List<TextMenuItem> serverMenu = new ArrayList<TextMenuItem>(Arrays.asList(startServer, shutDownServer, writeToFIle));

    private QuizService server;

    public static void main(String[] args) {

        QuizServerLauncher qsl = new QuizServerLauncher();
        qsl.launchMainMenuServer();
    }
    public void launchMainMenuServer() {

        do {
            MenuActions action = TextMenu.display("Quiz Server Menu", serverMenu);
            switch (action) {
                case LAUNCH_SERVER:
                    launchServer();
                    break;
                case SHUTDOWN_SERVER:
                    try{
                    server.shutdown();
                    } catch(RemoteException ex){
                        ex.printStackTrace();
                    }
                    break;
                case WRITE_TO_FILE:
                    //
                    break;
                default:
                    System.out.print("Choose a valid option");
            }

        } while (true);
    }


    public void launchServer(){

        try {
            serverLogger.info("Loading server...");
            Registry registry = LocateRegistry.createRegistry(1699);
            Serializer serializer = new Serializer();
            server = serializer.decodeData();
            String registryHost = "//localhost/";
            registry.rebind(SERVICE_NAME, server);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (AlreadyBoundException ex) {
            ex.printStackTrace();
        }
    }
}
