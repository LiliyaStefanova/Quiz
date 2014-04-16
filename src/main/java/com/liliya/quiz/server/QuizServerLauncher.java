package com.liliya.quiz.server;

import com.liliya.menu.MenuActions;
import com.liliya.menu.TextMenu;
import com.liliya.menu.TextMenuItem;
import com.liliya.quiz.model.QuizService;
import com.liliya.quiz.model.Serializer;

import java.nio.channels.AlreadyBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class QuizServerLauncher {

    private static final String SERVICE_NAME = "quiz";

    private static Logger serverLogger = Logger.getLogger(QuizServer.class.getName());

    private QuizService service;

    public static void main(String[] args) {

        QuizServerLauncher qsl = new QuizServerLauncher();
        qsl.launchServer();
    }

    public void launchServer() {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

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
