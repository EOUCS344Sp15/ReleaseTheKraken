/*
 * Base Class for all the creatures as per design.
 */
package releasethekraken.entity.seacreature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityLiving;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.seacreature.kraken.EntityKraken;
import releasethekraken.entity.seacreature.kraken.EntityKrakenGripper;
import releasethekraken.entity.seacreature.kraken.EntityKrakenTenticle;
import releasethekraken.path.SeaCreaturePath;

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
        addStat(EntityFish.class, new SeaCreatureStats(10, 10, 5, 2*60, "Fish", "Small, fast attacker. Doesn't have much health though."));
        addStat(EntityTurtle.class, new SeaCreatureStats(50, 50, 3, 5*60, "Turtle", "The turtle is tanky but his damage is a little low."));
        addStat(EntityOrca.class, new SeaCreatureStats(80, 100, 20, 10*60, "Orca", "Large, slow, but has a powerful water attack."));
    }
    
    /**
     * A map that maps PowerUp Enum types to integers representing how long (in ticks)
     * the powerup will last.  A value of 0 indicates that the powerup is not in effect.
     */
    protected EnumMap<EntityPowerUp.Ability, Integer> powerUps = new EnumMap<EntityPowerUp.Ability, Integer>(EntityPowerUp.Ability.class);
    
    /** The default move force */
    protected float defaultMoveForce = 0F;
    
    /** The force, in newtons, that is applied to move the sea creature */
    protected float moveForce = defaultMoveForce;
    
    /** The path that the sea creature is currently following */
    protected SeaCreaturePath currentPath;
    
    //Primary constructor
    public EntitySeaCreature(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        this.currentPath = world.getFirstPath();
    }
    
    //Secondary constructor
    public EntitySeaCreature(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        this.currentPath = world.getFirstPath();
    }
    
    @Override
    public void update()
    {
        super.update();
        
        //Make the sea creatures move
        if (!(this instanceof EntityPlayer) & !(this instanceof EntityKrakenTenticle) && !(this instanceof EntityKrakenGripper) && !(this instanceof EntityKraken))
        {        
            Vector2 targetPos = this.world.getPathTargetPos(this);
            Vector2 difference = targetPos.sub(this.getPos());
            difference.nor().scl(this.moveForce);
            this.physBody.applyForce(difference, this.getPos(), true);
        }
        
        //Gdx.app.log(this.getClass().getSimpleName(), "update(): this.powerUps.keySet(): " + Arrays.toString(this.powerUps.keySet().toArray()));
        
        for (EntityPowerUp.Ability ability : this.powerUps.keySet())
        {
            //Get the amount of time left for the powerup
            Integer ticksLeft = this.getPowerUpTime(ability);
            
            //Gdx.app.log("...", "\tPowerUp: " + ability + ", time left (ticks): " + ticksLeft);
            
            if (ticksLeft > 0)
            {
                ticksLeft--;
                
                switch (ability)
                {
                    case HEALUP:
                        if(this instanceof EntityPlayer || this instanceof EntityFish)
                            this.health = MathUtils.clamp(this.health+(this.maxHealth/2), 0, this.maxHealth);
                        else if(this instanceof EntityTurtle)
                            this.health = MathUtils.clamp(this.health+(this.maxHealth/4), 0, this.maxHealth);
                         else
                            this.health = MathUtils.clamp(this.health+(this.maxHealth/5), 0, this.maxHealth);
                        break;
                    case SPEEDUP:
                        this.moveForce = 2*this.defaultMoveForce;
                        break;

                }
            }
            else if (ability == EntityPowerUp.Ability.SPEEDUP && this.moveForce != this.defaultMoveForce)//after speed boost is done, set back to default speed
                    this.moveForce = this.defaultMoveForce;
            
            this.powerUps.put(ability, ticksLeft); //Make sure the powerup time is updated
        }
    }
    
    
    @Override
    public boolean onDamage(int damage)
    {
        if (this.getPowerUpTime(EntityPowerUp.Ability.DEFENSEUP) > 0)
            damage /= 2;
        
        return super.onDamage(damage);
    }
    
    /**
     * Applies a power up to the entity
     * @param powerUp The power up being applied
     * @param time The amount of time the power up will last
     */
    public void applyPowerUp(EntityPowerUp.Ability powerUp, int time)
    {
        Integer powerUpTime = this.getPowerUpTime(powerUp);
        powerUpTime += time;
        this.powerUps.put(powerUp, powerUpTime);
        
        //this.appliedPowerUp = powerUp;
        //this.powerUpTime = time;
    }
    
    /**
     * Gets the amount of time remaining for the specified powerup, or 0 if the powerup is no longer active.
     * @param powerUp The powerup to get the time for
     * @return the amount of time remaining for the specified powerup
     */
    public int getPowerUpTime(EntityPowerUp.Ability powerUp)
    {
        return this.powerUps.getOrDefault(powerUp, 0);
    }

    /**
     * Gets the current path that the sea creature is on
     * @return The current SeaCreaturePath
     */
    public SeaCreaturePath getCurrentPath()
    {
        return currentPath;
    }

    /**
     * Sets the current path that the sea creature is on
     * @param currentPath The new SeaCreaturePath
     */
    public void setCurrentPath(SeaCreaturePath currentPath)
    {
        this.currentPath = currentPath;
    }
    
    /**
     * 
     * @param shapeRenderer
     * @param delta
     * @param runTime
     * 
     * @author DJ Flores
     */
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer, float delta, float runTime)
    {
        super.renderShapes(shapeRenderer, delta, runTime);
        
        float offset = 0;
        
        if(this instanceof EntityKraken)
        {
            offset = 2f;
        }
        
        //TODO: Make this better
        int activePowerUps = 0; //Keep track of active powerups so we can offset the render position
        
        for (EntityPowerUp.Ability powerUp : this.powerUps.keySet())
        {            
            if (this.getPowerUpTime(powerUp) > 0)
            {
                float localOffset = activePowerUps * 0.7F; //Calculate render offset
                activePowerUps++; //Move the render offset for the next powerup
                
                shapeRenderer.setColor(EntityPowerUp.getStats(powerUp).mainColor);
                shapeRenderer.rect(this.getPos().x, this.getPos().y + 1.75f + offset + localOffset, 1f, .5f);
            }
        }
        
        /*if (true) //Render target position  TODO: Comment this out
        {
            Vector2 targetPos = this.world.getPathTargetPos(this);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.x(targetPos, 1);
        }*/
    }
    
    @Override
    public void renderSprites(SpriteBatch batch, float delta, float runTime)
    {
        super.renderSprites(batch, delta, runTime);
        
        /*if (true) //Render target position value  TODO: Comment this out
        {
            Vector2 targetPos = this.world.getPathTargetPos(this);
            float pathProgress = this.getCurrentPath().getSmoothPath().locate(this.getPos());
            GameAssets.fontMain.getData().setScale(0.1F);
            GameAssets.fontMain.draw(batch, "" + pathProgress, targetPos.x, targetPos.y);
        }*/
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
     * Builds and returns a new projectile
     * @param damage The damage multiplier for dealing damage to an entity
     */
    public void attack(int damage)
    {
        
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
