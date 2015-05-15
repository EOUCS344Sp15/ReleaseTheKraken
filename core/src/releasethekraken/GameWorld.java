/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.entity.Entity;
import releasethekraken.entity.PowerUp;
import releasethekraken.entity.pirate.BasicGunEntity;
import releasethekraken.entity.seacreature.BasicFishEntity;
import releasethekraken.util.EntityDistanceComparator;

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
        
        //Add entities, for testing purposes
        this.entities.add(new PowerUp(this, 0, 0, PowerUp.Ability.ATTACKUP, 20));
        this.entities.add(new BasicFishEntity(this, 10, 10));
        this.entities.add(new BasicGunEntity(this, 50, 50));
        this.entities.add(new BasicFishEntity(this, 100, 100));
        this.entities.add(new BasicFishEntity(this, 75, 75));
    }
    
    /**
     * Updates the state of the game world
     */
    public void update()
    {
        this.worldTime++;
        
        /*
        Update world entities
        Uses the lame way, since LibGDX Arrays reuse their Iterators, which
        causes problems when an entity iterates over all entites while being
        updated (see GameWorld.getClosestTarget())
        */
        for(int i=0; i<this.entities.size; i++)
            this.entities.get(i).update();
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
    
    /**
     * Finds the closest entity of type E, relative to the source entity.
     * 
     * To use: <br><br>
     * <code>SeaCreatureEntity target = this.world.getClosestTarget(this, SeaCreatureEntity.class);</code>
     * 
     * @param <E> Specify this with the target class. This is the entity type you are searching for
     * @param source The entity to find the closest target from
     * @param targetType The target entity class
     * @return The closest entity of type E, or null if none exists
     */
    public <E extends Entity> E getClosestTarget(Entity source, Class<E> targetType)
    {
        E target = null;
        
        Array<E> targets = new Array<E>(); //Set up an array to hold all potential targets
        
        //Find potential entities and add them to the array
        for (Entity ent : this.entities)           
            if (targetType.isAssignableFrom(ent.getClass()))
                targets.add((E)ent);
        
        targets.sort(new EntityDistanceComparator<E>(source)); //Sort the array of entities based on distance
        
        if (targets.size > 0 && targets.get(0) != null) //Return the closest target
            target = targets.get(0);
        
        return target;
    }
}
