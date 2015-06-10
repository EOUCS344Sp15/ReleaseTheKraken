/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import java.util.Random;
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityPirateBase;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import releasethekraken.path.SeaCreaturePath;
import static releasethekraken.physics.CollisionFilter.*;
import releasethekraken.util.EntityDistanceComparator;
import releasethekraken.physics.PhysicsContactListener;
import releasethekraken.physics.RayCastCallbackCollider;

/**
 * This class represents the game world.  When a new level is loaded, a new
 * Game World will be created.
 * 
 * @author Dalton
 */
public class GameWorld implements Disposable
{
    /** The Box2D physics world */
    private World physWorld;
    /** The array of physics bodies in the physics world */
    private Array<Body> physBodies;
    /** The array of physics bodies that should be removed as soon as possible */
    private Array<Body> physBodiesToRemove;
    /** The array of spawn tasks that need to be executed as soon as it is safe to do so */
    private Array<BodySpawnTask> spawnTasks;
    /** The world time, in ticks */
    private long worldTime = 0L;
    /** The world's TiledMap */
    private TiledMap tiledMap;
    /** The TiledMap's unit scale.  This is how many units = 1 meter */
    private float tiledMapUnitScale;
    /** The world's name */
    private String name;
    
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
    
    /** A reference to the PirateBase */
    private EntityPirateBase pirateBase;
    
    /** The beginning of the chain of paths that sea creatures can take */
    private SeaCreaturePath firstPath;
    
    /** The world's random number generator */
    public final Random random = new Random();
     
    /**
     * Constructs a new GameWorld
     */
    public GameWorld()
    {
        this.name = "DefaultWorldName";
        this.width = 0;
        this.height = 0;
        this.points = 250; //TODO: Change this back to 0!
        
        Vector2 gravity = new Vector2(0, 0); //The world's gravity.  Since this is top down, it is 0
        this.physWorld = new World(gravity, true); //Create the physics world
        this.physWorld.setContactListener(new PhysicsContactListener()); //Set the physics world's contact listener
        this.physBodies = new Array<Body>();
        this.physBodiesToRemove = new Array<Body>();
        this.spawnTasks = new Array<BodySpawnTask>();
        
        //Initialize the array of power up counts
        this.powerUps = new int[4];
        for (int i=0; i<this.powerUps.length; i++)
            this.powerUps[i] = 0;
        
        //Add coins
        this.coins = 100;
    }
    
    /**
     * Updates the state of the game world
     */
    public void update()
    {     
        if(this.getPlayer() != null)
        {
                        /*
            Temporary power up generator. Every 5 seconds there is a 50% chance
            a power up will drop within a radius around the player.
            TODO: Figure out how to make sure they don't spawn on coral/obstacles
            & balancing once needed
            */
            int spawn = random.nextInt(2) + 1;
            int makeXNegative = random.nextInt(2) + 1;
            int makeYNegative = random.nextInt(2) + 1;
            int power = random.nextInt(20) + 1;
            int xrange = random.nextInt(7) + 10;
            int yrange = random.nextInt(7) + 10;
            float yPos, xPos;
            Vector2 playerPos = this.player.getPos();

            if(makeXNegative == 1)
                xrange *= -1;

            if(makeYNegative == 1)
                yrange *= -1;

            //clamp to world size
            if((playerPos.y + yrange) < 2)
            {
                yPos = 3;
            }
            else if((playerPos.y + yrange) > this.height)
            {
                yPos = this.height - 3;
            }
            else
            {
                yPos = playerPos.y + yrange;
            }

            if((playerPos.x + xrange) < 2)
            {
                xPos = 3;
            }
            else if((playerPos.x + xrange) > this.width)
            {
                xPos = this.width - 3;
            }
            else
            {
                xPos = playerPos.x + xrange;
            }

            //For testing purposes
            //Gdx.app.log("PlayerPos", "X: " + xrange + "Y: " + yrange);

            if((this.worldTime % (ReleaseTheKraken.TICK_RATE*1)) == 59 && spawn == 1)
            {
                if(power <= 4)//20% chance to spawn
                    new EntityPowerUp(this, xPos, yPos, EntityPowerUp.Ability.HEALUP, 15);
                else if(power <= 9)//25% chance to spawn
                    new EntityPowerUp(this, xPos, yPos, EntityPowerUp.Ability.ATTACKUP, 15);
                else if(power <= 15)//30% chance to spawn
                    new EntityPowerUp(this, xPos, yPos, EntityPowerUp.Ability.SPEEDUP, 15);
                else if(power <= 20)//25% chance to spawn    
                    new EntityPowerUp(this, xPos, yPos, EntityPowerUp.Ability.DEFENSEUP, 20);           
            }
        } // end if
        
        /* //This doesn't work, as the task is removed while iterating over the list of tasks.  See alternate below
        for (BodySpawnTask task : this.spawnTasks) //Does all of the spawn tasks, spawning new stuff
        {
            Gdx.app.log("GameWorld Tick: " + this.worldTime, "Doing BodySpawnTask: " + task);
            
            task.doTask();
            this.spawnTasks.removeValue(task, true);
        }*/
        
        //Pops spawn tasks off of the list "stack".  Also performs the tasks.
        while (this.spawnTasks.size > 0)
            this.spawnTasks.pop().doTask();
        
        this.physWorld.getBodies(this.physBodies); //Refreshes the physBodies array
        this.worldTime++;
        
        for (int i=0; i<this.physBodies.size; i++)
        {
            Object object = this.physBodies.get(i).getUserData();
            
            if (object instanceof Entity)
                ((Entity)object).update();
        }
        
        this.physWorld.step(1F/ReleaseTheKraken.TICK_RATE, 6, 2); //Calculate physics
                
        //Remove physics bodies that need to be destroyed
        for (Body body : this.physBodiesToRemove)
        {            
            this.physWorld.destroyBody(body);
            this.physBodiesToRemove.removeValue(body, true);
        }
        
        /*
        Update world entities
        Uses the lame way, since LibGDX Arrays reuse their Iterators, which
        causes problems when an entity iterates over all entites while being
        updated (see GameWorld.getClosestTarget())
        */
        //for(int i=0; i<this.entities.size; i++)
        //    this.entities.get(i).update();
        
        //Update the points from the dev position for testing purposes.  TODO: Remove
        //this.points = (int) InputHandler.DEV_POS.y * 10;
        
        //Print the paths for debugging purposes.
        if (this.worldTime == 10)
        {            
            Array<SeaCreaturePath> paths = new Array<SeaCreaturePath>();
            this.firstPath.getAllConnectedPaths(paths);
            Gdx.app.log("GameWorld", "Paths: " + paths);
        }
    }
    
    /**
     * Spawns the boundaries around the world.  Should only be called once, after
     * the world size variables have been set.
     * Code copy and pasted from Dalton's app
     */
    public void spawnWorldBoundaries()
    {
        //Set up boundaries
        EdgeShape edgeShape = new EdgeShape();

        //The fixture def
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10000.0F;
        fixtureDef.friction = 0.4F;
        fixtureDef.restitution = 0.0F;

        //The body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //The body
        Body body = this.physWorld.createBody(bodyDef);

        //Top part
        edgeShape.set(0, this.height, this.width, this.height);
        fixtureDef.shape = edgeShape;
        body.createFixture(fixtureDef);

        //Left part
        edgeShape.set(0, 0, 0, this.height);
        fixtureDef.shape = edgeShape;
        body.createFixture(fixtureDef);

        //Right part
        edgeShape.set(this.width, 0, this.width, this.height);
        fixtureDef.shape = edgeShape;
        body.createFixture(fixtureDef);

        //Bottom part
        edgeShape.set(0, 0, this.width, 0);
        fixtureDef.shape = edgeShape;
        fixtureDef.friction = 2.0F; //300.0F;
        body.createFixture(fixtureDef);

        edgeShape.dispose();
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
        for (Body body : this.physBodies)
            if (body.getUserData() instanceof Entity)
                ((Entity)body.getUserData()).dispose();
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.physWorld.dispose();
    }
    
    @Override
    public String toString()
    {
        return this.getName() + "[w: " + this.width + ", h: " + this.height + "]";
    }

    /**
     * Gets the Box2D physics world
     * @return The Box2D physics world
     */
    public World getPhysWorld()
    {
        return physWorld;
    }

    /**
     * Gets the list of physics bodies in the world
     * @return The list of physics bodies
     */
    public Array<Body> getPhysBodies()
    {
        return physBodies;
    }
    
    /**
     * Removes an entity from the world
     * @param entity The entity to remove
     */
    public void removeEntity(Entity entity)
    {
        if (entity.getPhysBody() != null)
        {
            if (!this.physBodiesToRemove.contains(entity.getPhysBody(), true))
                this.physBodiesToRemove.add(entity.getPhysBody());
            //else
            //    Gdx.app.log("GameWorld", "WARNING: Attempt to remove a physbody that is already scheduled to be removed! Owner: " + entity);
            //Gdx.app.log("GameWorld", "removeEntity(" + entity + ");");
        }
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
        if (cost <= this.coins)
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
     * Gets the EntityPirateBase reference
     * @return The EntityPirateBase reference
     */
    public EntityPirateBase getPirateBase()
    {
        return this.pirateBase;
    }
    
    /**
     * Sets the pirate base reference
     * @param entityPirateBase The EntityPirateBase object that will be the pirate base
     */
    public void setPirateBase(EntityPirateBase entityPirateBase)
    {
        this.pirateBase = entityPirateBase;
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
     * Gets the beginning of the chain of paths that sea creatures can take
     * @return The first path
     */
    public SeaCreaturePath getFirstPath()
    {
        return firstPath;
    }

    /**
     * Sets the beginning of the chain of paths that sea creature can take
     * @param firstPath The first path
     */
    public void setFirstPath(SeaCreaturePath firstPath)
    {
        this.firstPath = firstPath;
    }
    
    /**
     * Adds a new BodySpawnTask to the list of tasks to carry out as soon as possible.
     * The task will get removed from the list when performed.
     * @param task The BodySpawnTask to add
     */
    public void addSpawnTask(BodySpawnTask task)
    {
        this.spawnTasks.add(task);
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
        return getClosestTarget(source, targetType, Float.POSITIVE_INFINITY, false);
    }
    
    /**
     * Finds the closest entity of type E, relative to the source entity.  Also takes
     * range and line of sight into account.
     * 
     * To use: <br><br>
     * <code>SeaCreatureEntity target = this.world.getClosestTarget(this, SeaCreatureEntity.class);</code>
     * 
     * @param <E> Specify this with the target class. This is the entity type you are searching for
     * @param source The entity to find the closest target from
     * @param targetType The target entity class
     * @param range The range, in meters, to look for targets
     * @param useLineOfSight If line of sight to the target is required
     * @return The closest entity of type E, or null if none exists
     */
    public <E extends Entity> E getClosestTarget(Entity source, Class<E> targetType, float range, boolean useLineOfSight)
    {        
        E target = null;
        
        Array<E> targets = new Array<E>(); //Set up an array to hold all potential targets
        
        //Find potential entities and add them to the array
        for (Body body : this.physBodies)
        {
            Object object = body.getUserData();
            
            if (object instanceof Entity)
            {
                Entity ent = (Entity)object;
                
                float distance = source.getPos().dst(ent.getPos());
                
                //Th ray trace will collide with everything except sea creatures, pirates, projectiles, and powerups
                short collisionBits = COL_ALL ^ (COL_SEA_CREATURE | COL_PIRATE | COL_SEA_CREATURE | COL_SEA_PROJECTILE | COL_PIRATE_PROJECTILE | COL_PLAYER | COL_POWERUP);
                
                if (targetType.isAssignableFrom(ent.getClass())) //If the target is of the correct type
                    if (distance < range)
                    {
                        if (useLineOfSight) //If line of sight should be used
                        {
                            //Only add the target if there is line of sight
                            if (this.hasLineOfSight(source.getPos(), ent.getPos(), collisionBits))
                                targets.add((E)ent);
                        }
                        else
                            targets.add((E)ent);
                    }
            }
        }
        
        try
        {
            targets.sort(new EntityDistanceComparator<E>(source)); //Sort the array of entities based on distance
        }
        catch (Exception e)
        {
            Gdx.app.error("GameWorld", "Error sorting targets: ", e);
        }
        
        
        if (targets.size > 0 && targets.get(0) != null) //Return the closest target
            target = targets.get(0);
        
        return target;
    }
    
    /**
     * Returns true if there is line of sight from one point to the next, taking into
     * account the specified collision bits.  Use with CollisionFilter's bit flags.
     * @param pos1 The first position
     * @param pos2 The second position
     * @param collisionBits The collision bits to use
     * @return True if there is line of sight between the two points
     */
    public boolean hasLineOfSight(final Vector2 pos1, final Vector2 pos2, final short collisionBits)
    {
        //Create the RayCastCallback that will determine what fixtures stop the ray cast
        RayCastCallbackCollider callback = new RayCastCallbackCollider(collisionBits);
        
        this.physWorld.rayCast(callback, pos1, pos2); //Do the ray cast
        
        return !callback.wasBlocked();
    }
    
    /**
     * Gets the target position that a sea creature should move towards, based on
     * where the sea creature currently is.  At the end of the path, it will be
     * the last point on the path.
     * @param entity The sea creature
     * @return The position the sea creature should move towards
     */
    public Vector2 getPathTargetPos(EntitySeaCreature entity)
    {
        if (entity.getCurrentPath() == null) //Immediately return if there is no path to follow
            return entity.getPos();
        
        //TODO: The target position doesn't seem to be that great
        CatmullRomSpline smoothPath = entity.getCurrentPath().getSmoothPath();
        Vector2 targetPos = new Vector2();
        
        float pathProgress = smoothPath.locate(entity.getPos());
        
        if (pathProgress >= 0.9F) //Randomly choose a next path.  TODO: Allow for choosing
        {
            //Gdx.app.log(entity.toString(), "At the end of the path! ****************************************************************");
            SeaCreaturePath nextPath = entity.getCurrentPath().getNextPaths().random();
            
            if (nextPath != null)
            {
                entity.setCurrentPath(nextPath);
                return getPathTargetPos(entity);
            }
        }
        
        smoothPath.valueAt(targetPos, pathProgress);
        
        return targetPos;
    }
    
    /**
     * Static inner class that allows you to specify code to spawn physics bodies
     * as soon as possible.  You simply declare an anonymous instance of this class,
     * and give it to the GameWorld.  It will execute the task when it is not updating
     * the game world.  Once executed, the task is discarded.  You can still reuse
     * tasks though.
     * 
     * @author Dalton
     */
    public static abstract class BodySpawnTask
    {
        /**
         * Does whatever task needs done.  Override this to specify what needs
         * to be done for a certain task.
         */
        protected abstract void doTask();
    }
}
