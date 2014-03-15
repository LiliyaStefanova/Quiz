package com.liliya.quiz;

/**
 * Represents a player who is launching and playing quizzes
 */
public interface Player {
    /**
     * Retrieve the name of the player
     * @return player name
     */
    public String getName();

    /**
     * Sets the name that the player would like to us
     * @param name
     */
    public void setPlayerId(String name);

    /**
     * Retrieve the playerId
     * @return id of player
     */
    public int getId();

    /**
     * Id will also be generated when a new player is created potentially
     * Need to see if it will be needed-may just make names unique
     */
}
