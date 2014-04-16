package com.liliya.quiz.model;


import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getPlayerNameTest(){

        Player newPlayer=new PlayerImpl("Jim");

        assertEquals("Jim", newPlayer.getName());
    }
}
