package com.liliya.quiz.model;


import com.liliya.quiz.model.Player;
import com.liliya.quiz.model.PlayerImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getPlayerNameTest(){

        Player newPlayer=new PlayerImpl("Jim");

        assertEquals("Jim", newPlayer.getName());
    }
}
