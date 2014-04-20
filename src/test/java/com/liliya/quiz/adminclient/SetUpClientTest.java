package com.liliya.quiz.adminclient;

import com.liliya.menu.MenuActions;
import com.liliya.quiz.model.QuizTestData;
import com.liliya.quiz.model.*;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetUpClientTest {


    @Test
    public void setUpNewQuizManualNormalTest() throws Exception{

        QuizTestData quizTestData = new QuizTestData();

        UserInputManagerAdmin userInputManagerMock=mock(UserInputManagerAdmin.class);

        QuizService quizServerMock=mock(QuizService.class);

        QuizSetUpClient setUpClient=new QuizSetUpClientImpl(userInputManagerMock, quizServerMock);

        when(userInputManagerMock.provideNewQuizName()).thenReturn("My Quiz");
        when(userInputManagerMock.setNewQuizNumQuestions()).thenReturn(1);
        when(userInputManagerMock.inputNewQuizQuestion()).thenReturn("When did WWII start?");
        when(userInputManagerMock.inputNewQuizPossAnswers()).thenReturn(quizTestData.possibleAnswers1);
        when(userInputManagerMock.inputNewQuizCorrAnswer()).thenReturn(3);
        when(userInputManagerMock.inputNewQuizCorrAnsPts()).thenReturn(6);

        Map<Integer, Question> questions=new HashMap<Integer, Question>();
        Question question1=new QuestionImpl("When did WWII start?",quizTestData.possibleAnswers1,3,6);
        questions.put(1,question1);

        when(quizServerMock.createNewQuiz("My Quiz",questions)).thenReturn(42);

        setUpClient.setUpNewQuizManually();

        verify(quizServerMock).createNewQuiz("My Quiz", questions);

    }

    @Test
    public void requestQuizCloseNormalTest() throws RemoteException{

        QuizTestData quizTestData=new QuizTestData();

        UserInputManagerAdmin userInputManagerMock=mock(UserInputManagerAdmin.class);
        when(userInputManagerMock.showMenu(anyString(), anyList())).thenReturn(MenuActions.CLOSE_QUIZ);

        QuizService quizServerMock=mock(QuizService.class);

        QuizSetUpClient setUpClient=new QuizSetUpClientImpl(userInputManagerMock, quizServerMock);


        when(userInputManagerMock.selectExistingQuizToClose()).thenReturn(3);
        when(quizServerMock.loadQuizForPlay(3, "John")).thenReturn(quizTestData.newInstance);
        when(quizServerMock.getListAvailableQuizzes()).thenReturn(quizTestData.listAvailableQuizzes);

        when(quizServerMock.closeQuiz(3)).thenReturn(quizTestData.instancesPerPlayer);

        setUpClient.requestQuizClose();

        verify(quizServerMock).getListAvailableQuizzes();
        verify(quizServerMock).closeQuiz(3);

    }





}
