/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.utils.Disposable;

/**
 * This class represents the game world.  When a new level is loaded, a new
 * Game World will be created.
 * 
 * @author Dalton
 */
public class GameWorld implements Disposable
{    
    private long worldTime = 0L; //The world time, in ticks
    private String name; //The world's name
    
    //Constructor
    public GameWorld()
    {
        this.name = "HardCodeLand";
    }
    
    /**
     * Updates the state of the game world
     */
    public void update()
    {
        this.worldTime++;
        //TODO
    }
    
    /**
     * Gets the world time in ticks
     * @return long: The world time, in ticks
     */
    public long getWorldTime()
    {
        return this.worldTime;
    }
    
    /**
     * Gets the world name
     * @return String: the world's name
     */
    public String getName()
    {
        return this.name;
    }

    @Override
    public void dispose()
    {
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
    }
}
