package com.liliya.quiz.playerclient;

import com.liliya.quiz.model.*;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerClientTest {

    @Test
    public void selectQuizToPlayTest() throws RemoteException {

        QuizTestData quizTestData = new QuizTestData();

        UserInputManagerPlayer userInputManagerMock = mock(UserInputManagerPlayer.class);
        QuizService quizServerMock = mock(QuizService.class);

        QuizPlayerClient playerClient = new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        //instance set up
        PlayerQuizInstance newInstance = new PlayerQuizInstanceImpl(quizTestData.newPlayer, quizTestData.newQuiz);
        newInstance.setTotalScore(5);

        List<Quiz> activeQuizzes = new ArrayList<Quiz>();
        activeQuizzes.add(quizTestData.newQuiz);

        when(quizServerMock.getListAvailableQuizzes()).thenReturn(activeQuizzes);
        when(userInputManagerMock.selectQuizToPlay()).thenReturn(3);

        playerClient.selectQuizToPlay();

        verify(quizServerMock).getListAvailableQuizzes();
    }

    @Test
    public void playQuizTest() throws RemoteException {

        QuizTestData quizTestData = new QuizTestData();

        UserInputManagerPlayer userInputManagerMock = mock(UserInputManagerPlayer.class);
        QuizService quizServerMock = mock(QuizService.class);

        QuizPlayerClient playerClient = new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        when(userInputManagerMock.inputPlayerName()).thenReturn("John");
        when(userInputManagerMock.inputSelectedAnswer()).thenReturn(3).thenReturn(1);
        when(quizServerMock.loadQuizForPlay(3, "John")).thenReturn(quizTestData.newInstance);
        when(quizServerMock.calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses)).thenReturn(13);

        playerClient.playQuiz(3);

        verify(quizServerMock).loadQuizForPlay(3, "John");
        verify(quizServerMock).calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses);

        assertEquals(13, quizServerMock.calculatePlayerScore(quizTestData.newInstance, quizTestData.playerGuesses));

    }

    @Test
    public void viewHighScoresTest() throws RemoteException {

        QuizTestData quizTestData = new QuizTestData();

        UserInputManagerPlayer userInputManagerMock = mock(UserInputManagerPlayer.class);
        QuizService quizServerMock = mock(QuizService.class);

        QuizPlayerClient playerClient = new QuizPlayerClientImpl(userInputManagerMock, quizServerMock);

        when(userInputManagerMock.inputPlayerName()).thenReturn("John");
        when(quizServerMock.getQuizzesPlayedByPlayer("John")).thenReturn(quizTestData.instancesPerPlayer);

        playerClient.viewHighScores();

        verify(quizServerMock).getQuizzesPlayedByPlayer("John");

    }


}
