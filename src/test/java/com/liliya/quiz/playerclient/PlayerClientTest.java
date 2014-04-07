package com.liliya.quiz.playerclient;

import com.liliya.quiz.model.QuizTestData;
import com.liliya.quiz.model.*;
import org.junit.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PlayerClientTest {

    //selecting quiz from list of active quizzes
    //test a quiz passed will be displayed as required
    //how can I test submissions which rely on user input?
    @Test
    public void selectQuizToPlayTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerPlayer userInputManagerMock=mock(UserInputManagerPlayer.class);
        QuizService quizServerMock=mock(QuizService.class);

        QuizPlayerClient playerClient=new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        //instance set up
        PlayerQuizInstance newInstance=new PlayerQuizInstance(quizTestData.newPlayer,quizTestData.newQuiz);
        newInstance.setTotalScore(5);

        List<Quiz> activeQuizzes=new ArrayList<Quiz>();
        activeQuizzes.add(quizTestData.newQuiz);

        when(quizServerMock.getListAvailableQuizzes()).thenReturn(activeQuizzes);
        when(userInputManagerMock.selectQuizToPlayFromList()).thenReturn(3);

        playerClient.selectQuizToPlay();

        verify(quizServerMock).getListAvailableQuizzes();
    }

     //TODO need to figure out how to test this
    @Test
    public void selectQuizToPlayNoActiveQuizzes() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerPlayer userInputManagerMock=mock(UserInputManagerPlayer.class);
        QuizService quizServerMock=mock(QuizService.class);

        QuizPlayerClient playerClient=new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        List<Quiz> activeQuizzes=new ArrayList<Quiz>();

        when(quizServerMock.getListAvailableQuizzes()).thenReturn(activeQuizzes);

        playerClient.selectQuizToPlay();

        verify(quizServerMock).getListAvailableQuizzes();
    }
    //TODO user choice is not an int or is invalid
    @Test
    public void selectQuizToPlayIncorrectUserChoice(){

        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerPlayer userInputManagerMock=mock(UserInputManagerPlayer.class);

        when(userInputManagerMock.selectQuizToPlayFromList()).thenReturn(0);


        //doThrow(new InputMismatchException()).when(userInputManagerMock.selectQuizToPlayFromList());

    }

    @Test
    public void playQuizTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerPlayer userInputManagerMock=mock(UserInputManagerPlayer.class);
        QuizService quizServerMock=mock(QuizService.class);

        QuizPlayerClient playerClient=new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        when(userInputManagerMock.providePlayerName()).thenReturn("John");
        when(quizServerMock.loadQuizForPlay(3, "John")).thenReturn(quizTestData.newInstance);
        when(quizServerMock.calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses)).thenReturn(10);

        playerClient.playQuiz(3);

        verify(quizServerMock).loadQuizForPlay(3, "John");
        verify(quizServerMock).calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses);

        assertEquals(10, quizServerMock.calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses));
        // TODO why does this test not work with the correct answer value?

    }

    @Test
    public void playQuizTestNameNotProvided() throws RemoteException{
        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerPlayer userInputManagerMock=mock(UserInputManagerPlayer.class);
        QuizService quizServerMock=mock(QuizService.class);

        QuizPlayerClient playerClient=new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        when(userInputManagerMock.providePlayerName()).thenReturn(null);
        when(quizServerMock.loadQuizForPlay(3, "John")).thenReturn(quizTestData.newInstance);
        when(quizServerMock.calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses)).thenReturn(10);

        playerClient.playQuiz(3);

        verify(quizServerMock).loadQuizForPlay(3, "John");
        verify(quizServerMock).calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses);

        assertEquals(10, quizServerMock.calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses));

    }



}
