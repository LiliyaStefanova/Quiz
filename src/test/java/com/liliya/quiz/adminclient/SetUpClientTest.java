package com.liliya.quiz.adminclient;

import com.liliya.quiz.model.QuizTestData;
import com.liliya.quiz.model.*;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetUpClientTest {


    @Test
    public void setUpNewQuizManualNormalTest() throws Exception{

        UserInputManagerAdmin userInputManagerMock=mock(UserInputManagerAdmin.class);

        QuizService quizServerMock=mock(QuizService.class);

        QuizSetUpClient setUpClient=new QuizSetUpClientImpl(userInputManagerMock, quizServerMock);

        Map<Integer, String> possibleAnswers = new HashMap<Integer, String>();
        possibleAnswers.put(1, "1945");
        possibleAnswers.put(2, "1940");
        possibleAnswers.put(3, "1939");
        possibleAnswers.put(4, "1962");

        when(userInputManagerMock.provideQuizName()).thenReturn("My Quiz");
        when(userInputManagerMock.setNumberOfQuestions()).thenReturn(1);
        when(userInputManagerMock.provideQuestion()).thenReturn("When did WWII start?");
        when(userInputManagerMock.providePossibleAnswers()).thenReturn(possibleAnswers);
        when(userInputManagerMock.provideCorrectAnswer()).thenReturn(3);
        when(userInputManagerMock.provideCorrectAnswerPoints()).thenReturn(6);

        Map<Integer, Question> questions=new HashMap<Integer, Question>();
        Question question1=new QuestionImpl("When did WWII start?",possibleAnswers,3,6);
        questions.put(1,question1);

        when(quizServerMock.createNewQuiz("My Quiz", questions)).thenReturn(42);

        setUpClient.setUpQuizManually();

        verify(quizServerMock).createNewQuiz("My Quiz", questions);

    }

    @Test
    public void requestQuizCloseNormalTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerAdmin userInputManagerMock=mock(UserInputManagerAdmin.class);

        QuizService quizServerMock=mock(QuizService.class);

        QuizSetUpClient setUpClient=new QuizSetUpClientImpl(userInputManagerMock, quizServerMock);

        PlayerQuizInstance newInstance=new PlayerQuizInstance(quizTestData.newPlayer, quizTestData.newQuiz);
        newInstance.setTotalScore(5);

        when(userInputManagerMock.requestQuizId()).thenReturn(3);

        when(quizServerMock.closeQuiz(3)).thenReturn(newInstance);

        setUpClient.requestQuizClose();

        verify(quizServerMock).closeQuiz(3);
    }



}
