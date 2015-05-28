/*
 * Base Class for all the creatures as per design.
 */
package releasethekraken.entity.seacreature;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import java.util.HashMap;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityLiving;
import releasethekraken.entity.EntityPowerUp;

/**
 *
 * @author mwinburn
 */
public abstract class EntitySeaCreature extends EntityLiving
{
    /** A HashMap to map SeaCreatureStats to SeaCreature classes */
    private static final HashMap<Class<? extends EntitySeaCreature>, SeaCreatureStats> unitStats 
            = new HashMap<Class<? extends EntitySeaCreature>, SeaCreatureStats>();
    
    static //Add stats for each type of sea creature here
    {
        //TODO: adjust the values
        addStat(EntityFish.class, new SeaCreatureStats(10, 10, 5, 2*60, "Fish", "A description of a fish should probably go here..."));
        addStat(EntityTurtle.class, new SeaCreatureStats(60, 50, 3, 5*60, "Turtle", "The turtle is tanky but his damage is a little low. "));
        addStat(EntityOrca.class, new SeaCreatureStats(150, 100, 20, 10*60, "Orca", "A description of an orca should probably go here..."));
    }
    
    /** The current power up applied; can be null */
    protected EntityPowerUp.Ability appliedPowerUp = null;
    
    /** The amount of time left that the power up is applied for (ticks) */
    protected int powerUpTime = 0;
    
    //Primary constructor
    public EntitySeaCreature(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    
    //Secondary constructor
    public EntitySeaCreature(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
    }
    
    @Override
    public void update()
    {
        super.update();
        
        if (this.powerUpTime > 0)
        {
            this.powerUpTime--;
            
            switch (this.appliedPowerUp)
            {
                case HEALUP:
                    this.health = MathUtils.clamp(this.health+10, 0, this.maxHealth);
                case SPEEDUP:
                    //TODO: add functionality for speeding up entities
                case ATTACKUP:
                    //TODO: add functionalility for increasing enemy attack
                case DEFENSEUP:
                    //TODO: add functionality for incre
            }
        }
    }
    
    /**
     * Applies a power up to the entity
     * @param powerUp The power up being applied
     * @param time The amount of time the power up will last
     */
    public void applyPowerUp(EntityPowerUp.Ability powerUp, int time)
    {
        this.appliedPowerUp = powerUp;
        this.powerUpTime = time;
    }
    
    /**
     * Gets the stats for a particular type of sea creature
     * @param seaCreature The sea creature class to get stats for
     * @return The SeaCreatureStats object
     */
    public static SeaCreatureStats getStats(Class<? extends EntitySeaCreature> seaCreature)
    {
        return unitStats.get(seaCreature);
    }
    
    /**
     * Adds stats for a particular type of sea creature
     * @param seaCreature The sea creature class to add stats for
     * @param stats The stats to add
     */
    public static void addStat(Class<? extends EntitySeaCreature> seaCreature, SeaCreatureStats stats)
    {
        unitStats.put(seaCreature, stats);
    }
    
    /**
     * Represents the stats of a purchasable sea creature.  
     * @author Dalton
     */
    public static class SeaCreatureStats
    {
        /** How much the sea creature costs, in coins */
        public final int cost;
        /** How much health the sea creature starts with */
        public final int health;
        /** How strong the sea creature's attack is */
        public final int strength;
        /** How long it takes to spawn the sea creature, in ticks */
        public final int buildTime;
        /** The name of the sea creature */
        public final String name;
        /** A description of the sea creature */
        public final String description;
        
        /**
         * Constructs a new SeaCreatureStats object.  Register it with EntitySeaCreature
         * @param cost How much the sea creature costs, in coins
         * @param health How much health the sea creature starts with
         * @param strength How strong the sea creature's attack is
         * @param buildTime How long it takes to spawn the sea creature, in ticks
         * @param name The name of the sea creature
         * @param description A description of the sea creature
         */
        public SeaCreatureStats(int cost, int health, int strength, int buildTime, String name, String description)
        {
            this.cost = cost;
            this.health = health;
            this.strength = strength;
            this.buildTime = buildTime;
            this.name = name;
            this.description = description;
        }
    }
}
