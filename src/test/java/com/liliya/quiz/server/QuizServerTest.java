package com.liliya.quiz.server;

import com.liliya.quiz.model.QuizTestData;
import com.liliya.quiz.adminclient.QuizSetUpClient;
import com.liliya.quiz.model.*;
import com.liliya.quiz.playerclient.QuizPlayerClient;
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
    public void closeQuizNormal() throws RemoteException{

    }

    @Test
    public void getListCurrentQuizzes(){


    }


}
