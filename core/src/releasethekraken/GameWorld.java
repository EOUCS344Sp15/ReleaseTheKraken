/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.util.EntityDistanceComparator;

/**
 * This class represents the game world.  When a new level is loaded, a new
 * Game World will be created.
 * 
 * @author Dalton
 */
public class GameWorld implements Disposable
{    
    /** The world time, in ticks */
    private long worldTime = 0L;
    /** The world's TiledMap */
    private TiledMap tiledMap;
    /** The TiledMap's unit scale.  This is how many units = 1 meter */
    private float tiledMapUnitScale;
    /** The world's name */
    private String name;
    /** The array of entities in the world */
    private Array<Entity> entities;
    
    /** The amount of power ups the player has.  Use EntityPowerUp.Ability.ordinal() as the index */
    private int[] powerUps;
    
    //The width and height of the world
    public float width;
    public float height;
    
    /** The current amount of coins */
    private int coins;
    /** The current amount of points */
    private int points;
    /** The amount of points required to RELEASE THE KRAKEN! */
    private int pointsForKraken;
    
    /** A reference to the player */
    private EntityPlayer player;
     
    /**
     * Constructs a new GameWorld
     */
    public GameWorld()
    {
        this.entities = new Array<Entity>();
        this.name = "DefaultWorldName";
        this.width = 0;
        this.height = 0;
        this.points = 0;
        
        //Initialize the array of power up counts
        this.powerUps = new int[4];
        for (int i=0; i<this.powerUps.length; i++)
            this.powerUps[i] = 4;
        
        //Add powerups for testing purposes
        this.entities.add(new EntityPowerUp(this, 20, 20, EntityPowerUp.Ability.ATTACKUP, 10));
        this.entities.add(new EntityPowerUp(this, 25, 25, EntityPowerUp.Ability.HEALUP, 20));
        this.entities.add(new EntityPowerUp(this, 30, 30, EntityPowerUp.Ability.SPEEDUP, 30));
        this.entities.add(new EntityPowerUp(this, 35, 25, EntityPowerUp.Ability.DEFENSEUP, 40));
        
        //Add coins for testing purposes
        this.coins = 1337;
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
        
        //Update the points from the dev position for testing purposes.  TODO: Remove
        this.points = (int) InputHandler.DEV_POS.y * 10;
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

    /**
     * Sets the world's name
     * @param name The new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the world's width, in pixels
     * @return float: the width, in pixels
     */
    public float getWidth()
    {
        return width;
    }

    /**
     * Sets the world's width, in pixels
     * @param width The new width, in pixels
     */
    public void setWidth(float width)
    {
        this.width = width;
    }

    /**
     * Gets the world's height, in pixels
     * @return float: the height, in pixels
     */
    public float getHeight()
    {
        return height;
    }

    /**
     * Sets the world's height, in pixels
     * @param height The new height, in pixels
     */
    public void setHeight(float height)
    {
        this.height = height;
    }

    /**
     * Gets the world's TiledMap
     * @return the world's TiledMap
     */
    public TiledMap getTiledMap()
    {
        return tiledMap;
    }

    /**
     * Sets the world's TiledMap
     * @param tiledMap the new TiledMap
     */
    public void setTiledMap(TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
    }

    /**
     * Gets the TiledMap unit scale
     * @return the TiledMap unit scale
     */
    public float getTiledMapUnitScale()
    {
        return tiledMapUnitScale;
    }

    /**
     * Sets the TiledMap unit scale
     * @param tiledMapUnitScale the new TiledMap unit scale
     */
    public void setTiledMapUnitScale(float tiledMapUnitScale)
    {
        this.tiledMapUnitScale = tiledMapUnitScale;
    }

    @Override
    public void dispose()
    {
        //Dispose of all entities in the world
        for (Entity entity : this.entities)
            entity.dispose();
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
    }
    
    @Override
    public String toString()
    {
        return this.getName() + "[w: " + this.width + ", h: " + this.height + "]";
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
     * Gets the current amount of points earned
     * @return The current points
     */
    public int getPoints()
    {
        return points;
    }

    /**
     * Adds points to the current points
     * @param points The points to add
     */
    public void addPoints(int points)
    {
        this.points += points;
    }

    /**
     * Gets the amount of points required to RELEASE THE KRAKEN!
     * @return The amount of points to required to RELEASE THE KRAKEN!
     */
    public int getPointsForKraken()
    {
        return pointsForKraken;
    }

    /**
     * Sets the amount of points required to RELEASE THE KRAKEN!
     * @param pointsForKraken The new amount of points required to RELEASE THE KRAKEN!
     */
    public void setPointsForKraken(int pointsForKraken)
    {
        this.pointsForKraken = pointsForKraken;
    }

    /**
     * Gets the amount of coins the player has
     * @return The amount of coins the player has
     */
    public int getCoins()
    {
        return coins;
    }

    /**
     * Adds coins to the amount of coins that the player has
     * @param coins The amount of coins to add
     */
    public void addCoins(int coins)
    {
        this.coins += coins;
    }
    
    /**
     * Removes the specified amount of coins, if there are enough to remove
     * @param cost The amount of coins to remove
     * @return True if the amount of coins was removed, false otherwise (if there was not enough)
     */
    public boolean removeCoins(int cost)
    {
        if (cost < this.coins)
        {
            this.coins -= cost;
            return true;
        }
        else
            return false;
    }
    
    /**
     * Gets the EntityPlayer reference
     * @return The EntityPlayer reference
     */
    public EntityPlayer getPlayer()
    {
        return this.player;
    }
    
    /**
     * Sets the player reference
     * @param entityPlayer The EntityPlayer object that will be the player
     */
    public void setPlayer(EntityPlayer entityPlayer)
    {
        this.player = entityPlayer;
    }
    
    /**
     * Gets how many power ups the player has of a given type.
     * @param type The type of power up
     * @return How many of those power ups the player has
     */
    public int getPowerUps(EntityPowerUp.Ability type)
    {
        return this.powerUps[type.ordinal()];
    }
    
    /**
     * Adds (or subtracts) the specified amount of power ups for the given type
     * @param type The type of power up to add or subtract an amount to
     * @param amount The amount to add or subtract
     */
    public void addPowerUps(EntityPowerUp.Ability type, int amount)
    {
        this.powerUps[type.ordinal()] += amount;
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
