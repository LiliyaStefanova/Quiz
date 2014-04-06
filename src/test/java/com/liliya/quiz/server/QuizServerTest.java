package com.liliya.quiz.server;

import com.liliya.quiz.model.QuizTestData;
import com.liliya.quiz.adminclient.QuizSetUpClient;
import com.liliya.quiz.model.*;
import com.liliya.quiz.playerclient.QuizPlayerClient;
import com.liliya.quiz.playerclient.QuizPlayerClientImpl;
import org.junit.*;

import java.rmi.RemoteException;

import static org.junit.Assert.*;
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

        QuizSetUpClient quizSetUpClientMock=mock(QuizSetUpClient.class);
        QuizPlayerClient quizPlayerClientMock=mock(QuizPlayerClient.class);
        UserInputManagerAdmin userInputManagerAdmin=mock(UserInputManagerAdmin.class);
        UserInputManagerPlayer userInputManagerPlayer=mock(UserInputManagerPlayer.class);


        when(userInputManagerAdmin.provideQuizName()).thenReturn("My Quiz");

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        assertEquals(0, quizId);
        assertEquals(quizTestData.newQuiz.getQuizName(), quizServer.findQuiz(0).getQuizName());
    }

    @Test
    public void closeQuizNormalTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);
        PlayerQuizInstance newInstance=quizServer.loadQuizForPlay(quizId, "John");

        quizServer.closeQuiz(quizId);

        assertFalse(quizServer.findQuiz(quizId).getQuizState());
        assertEquals(newInstance.getQuiz().getQuizName(), quizServer.getPlayerQuizInstances().get(0).getQuiz().getQuizName());
        assertEquals(newInstance.getPlayer().getName(), quizServer.getPlayerQuizInstances().get(0).getPlayer().getName());

    }

    @Test
    public void calculateIndividualScoreTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizId = quizServer.createNewQuiz("My Quiz", quizTestData.questions);

        PlayerQuizInstance newInstance=quizServer.loadQuizForPlay(quizId, "John");

        assertEquals(6, quizServer.calculateIndividualScore(newInstance, quizTestData.playerGuesses));
    }

    @Test
    public void getListAvailableQuizzesTestNormal() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        quizServer.createNewQuiz("My test quiz", quizTestData.questions);

        assertEquals("My test quiz", quizServer.getListAvailableQuizzes().get(0).getQuizName());

    }

    /**
     * Checks that if quizzes were all set to inactive nothing will be returned to the client
     * @throws RemoteException
     */
    @Test
    public void getListAvailableQuizzesEmpty() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        int quizID=quizServer.createNewQuiz("My test quiz", quizTestData.questions);
        quizServer.findQuiz(quizID).setQuizState(false);

        assertTrue(quizServer.getListAvailableQuizzes().isEmpty());

    }

    @Test
    public void addNewPlayerTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();
        QuizServer quizServer=new QuizServer();

        Player newPlayer=quizServer.addNewPlayer("John");

        assertTrue(quizServer.getAllPlayers().contains(newPlayer));

    }

}
