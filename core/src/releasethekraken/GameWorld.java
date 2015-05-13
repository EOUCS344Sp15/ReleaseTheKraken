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
    private Array<Entity> entities; //The entities in the world
    
    //The width and height of the world
    public final float width;
    public final float height;
     
    //Constructor
    public GameWorld(String name, float width, float height)
    {
        this.entities = new Array<Entity>();
        this.name = name;
        this.width = width;
        this.height = height;
        
        //Add a new powerup, for testing purposes
        this.entities.add(new PowerUp(this, 0, 0, PowerUp.Ability.ATTACKUP, 20));
    }
    
    /**
     * Updates the state of the game world
     */
    public void update()
    {
        this.worldTime++;
        
        //Update world entities
        for(Entity entity : this.entities)
            entity.update();
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
        //Dispose of all entities in the world
        for (Entity entity : this.entities)
            entity.dispose();
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
    }
    
    /**
     * Adds an entity to the world
     * @param entity The entity to add
     */
    public void addEntity(Entity entity)
    {
        if (this.entities.contains(entity, true) == false)
        {
            this.entities.add(entity);
        }
    }
    
    /**
     * Removes an entity from the world
     * @param entity The entity to remove
     */
    public void removeEntity(Entity entity)
    {
        this.entities.removeValue(entity, true);
    }
    
    /**
     * Gets the array of entities, for rendering purposes.  
     * TODO: Is there a way to have this only visible to GameRenderer?
     * @return The array of entities
     */
    public Array<Entity> getEntitites()
    {
        return this.entities;
    }
}
