package com.liliya.quiz;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 15/03/14
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class PlayerImpl implements Player, Serializable {

    private String name;

    public PlayerImpl(){
        //no args constructor for serialization
    }

    public PlayerImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerImpl player = (PlayerImpl) o;

        if (name != null ? !name.equals(player.name) : player.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public void setName(String name) {
        this.name = name;
    }
}