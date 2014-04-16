package com.liliya.quiz.server;

import com.liliya.quiz.model.QuizTestData;
import com.liliya.quiz.model.*;
import org.junit.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing the methods the remote server implements
 */
public class QuizServerTest{


    @Test
    public void createNewQuizNormalTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        QuizServer quizServer=new QuizServer();

        UserInputManagerAdmin userInputManagerAdmin=mock(UserInputManagerAdmin.class);

        when(userInputManagerAdmin.provideQuizName()).thenReturn("My Quiz");

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        assertEquals(0, quizId);
        assertEquals(quizTestData.newQuiz.getQuizName(), quizServer.findQuizToPlay(0).getQuizName());
    }

    //Empty or null name not tested here as exception is handled as part of the UserInputManagerAdmin functionality

    @Test
    public void closeQuizNormalTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        QuizServer quizServer=new QuizServer();

        int quizId1 = quizServer.createNewQuiz("My Quiz 1", quizTestData.questions);
        PlayerQuizInstance newInstance1=quizServer.loadQuizForPlay(quizId1, "John");

        quizServer.closeQuiz(quizId1);

        assertEquals(null, quizServer.findQuizToPlay(quizId1));
        assertEquals(newInstance1.getQuiz().getQuizName(), quizServer.getPlayerQuizInstances().get(0).getQuiz().getQuizName());
        assertEquals(newInstance1.getPlayer().getName(), quizServer.getPlayerQuizInstances().get(0).getPlayer().getName());

    }

    @Test
    public void checkQuizPlayedTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        PlayerQuizInstance newInstance=quizServer.loadQuizForPlay(quizId, quizTestData.newPlayer.getName());
        newInstance.setQuizPlayed(true);

        assertTrue(quizServer.checkQuizPlayed(quizId));

    }

    @Test
    public void calculateIndividualScoreTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        PlayerQuizInstance newInstance=quizServer.loadQuizForPlay(quizId, "John");

        assertEquals(13, quizServer.calculatePlayerScore(newInstance, quizTestData.playerGuesses));
    }

    @Test
    public void checkQuizWinnerNormalTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        PlayerQuizInstance newInstance1=quizServer.loadQuizForPlay(quizId, "John");
        newInstance1.setTotalScore(6);
        PlayerQuizInstance newInstance2=quizServer.loadQuizForPlay(quizId, "Mary");
        newInstance2.setTotalScore(13);

        List<PlayerQuizInstance> listInstances=new ArrayList<PlayerQuizInstance>();
        listInstances.add(newInstance1);
        listInstances.add(newInstance2);

       assertTrue(quizServer.determineQuizWinner(listInstances).contains(newInstance2));
       assertEquals(1, quizServer.determineQuizWinner(listInstances).size());

    }

    @Test
    public void checkQuizWinnerSameScoreTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        PlayerQuizInstance newInstance1=quizServer.loadQuizForPlay(quizId, "John");
        newInstance1.setTotalScore(13);
        PlayerQuizInstance newInstance2=quizServer.loadQuizForPlay(quizId, "Mary");
        newInstance2.setTotalScore(13);

        List<PlayerQuizInstance> listInstances=new ArrayList<PlayerQuizInstance>();
        listInstances.add(newInstance1);
        listInstances.add(newInstance2);

        assertTrue(quizServer.determineQuizWinner(listInstances).contains(newInstance1));
        assertTrue(quizServer.determineQuizWinner(listInstances).contains(newInstance2));

    }

    @Test
    public void getListAvailableQuizzesTestNormal() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        quizServer.createNewQuiz("My test quiz", quizTestData.questions);

        assertEquals("My test quiz", quizServer.getListAvailableQuizzes().get(0).getQuizName());

    }

    //Checks that if there are no quizzes an empty list will be returned
    @Test
    public void getListAvailableQuizzesNoQuizTest() throws RemoteException{
        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        assertTrue(quizServer.getListAvailableQuizzes().isEmpty());
    }

    /**
     * Checks that if quizzes were all set to inactive nothing will be returned to the client
     * @throws RemoteException
     */
    @Test
    public void getListAvailableQuizzesInactiveOnly() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizID=quizServer.createNewQuiz("My test quiz", quizTestData.questions);
        quizServer.findQuizToPlay(quizID).setQuizState(false);

        assertTrue(quizServer.getListAvailableQuizzes().isEmpty());
    }

    @Test
    public void addNewPlayerTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        Player newPlayer=quizServer.addNewPlayer("John");

        assertTrue(quizServer.getAllPlayers().contains(newPlayer));

    }

    @Test
    public void getQuizzesPlayedByPlayer() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        quizServer.loadQuizForPlay(quizId, quizTestData.newPlayer.getName());
        quizServer.loadQuizForPlay(quizId, quizTestData.newPlayer.getName());

        assertEquals(2, quizServer.getQuizzesPlayedByPlayer("John").size());

    }

}
