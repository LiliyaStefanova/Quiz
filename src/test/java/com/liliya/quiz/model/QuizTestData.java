package com.liliya.quiz.model;

import com.liliya.quiz.model.*;
import com.liliya.quiz.model.PlayerQuizInstance;
import com.liliya.quiz.model.Question;

import java.util.HashMap;
import java.util.Map;

public class QuizTestData {

    public Map<Integer, String> possibleAnswers;
    public Map<Integer, Question> questions;
    public Question question1;
    public Player newPlayer;
    public Quiz newQuiz;
    public PlayerQuizInstance newInstance;
    public Map<Question, Integer> playerGuesses;

    public QuizTestData() {

        possibleAnswers = new HashMap<Integer, String>();
        possibleAnswers.put(1, "1945");
        possibleAnswers.put(2, "1940");
        possibleAnswers.put(3, "1939");
        possibleAnswers.put(4, "1962");

        questions = new HashMap<Integer, Question>();
        question1 = new QuestionImpl("When did WWII start?", possibleAnswers, 3, 6);
        questions.put(1, question1);

        newPlayer = new PlayerImpl("John");
        newQuiz = new QuizImpl("My Quiz", questions, 3);

        newInstance = new PlayerQuizInstance(newPlayer, newQuiz);
        //newInstance.setTotalScore(5);
        playerGuesses=new HashMap<Question, Integer>();
        playerGuesses.put(question1, 3);
    }
}

