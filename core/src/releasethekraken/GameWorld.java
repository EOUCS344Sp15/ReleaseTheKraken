/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.entity.Entity;
import releasethekraken.entity.PowerUp;

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
    private Array<Entity> entities;
     
    //Constructor
    public GameWorld()
    {
        this.entities = new Array<Entity>();
        this.name = "HardCodeLand";
        
        this.entities.add(new PowerUp(this, 0, 0, PowerUp.Ability.ATTACKUP, 20));
    }
    
    /**
     * Updates the state of the game world
     */
    public void update()
    {
        this.worldTime++;
        //TODO
        for(Entity entity : this.entities)
        {
            entity.update();
        }
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
    
    public void addEntity(Entity entity)
    {
        if (this.entities.contains(entity, true) == false)
        {
            this.entities.add(entity);
        }
    }
    public void removeEntity(Entity entity)
    {
        this.entities.removeValue(entity, true);
    }
    public Array<Entity> getEntitites()
    {
        return this.entities;
    }
}
