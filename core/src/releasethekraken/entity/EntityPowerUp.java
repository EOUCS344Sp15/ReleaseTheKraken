/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import java.util.HashMap;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author tarstarkes
 */
public class EntityPowerUp extends Entity
{
    /** A HashMap to map PowerUpStats to PowerUp types */
    private static final HashMap<Ability, PowerUpStats> powerUpStats 
            = new HashMap<Ability, PowerUpStats>();
    
    static //Add stats for each type of power up here
    {
        //TODO: adjust the values
        addStat(Ability.ATTACKUP, new PowerUpStats(60*10, 10, "Damage\nBoost", "Increases the attack of the player and nearby allies.", Color.valueOf("FF6A00A6")));
        addStat(Ability.HEALUP, new PowerUpStats(1, 20, "Heal", "Heals the player and nearby allies.", Color.CYAN.cpy().sub(0, 0, 0, 0.35F)));
        addStat(Ability.SPEEDUP, new PowerUpStats(60*30, 30, "Speed\nBoost", "Increases the speed of the player and nearby allies.", Color.WHITE.cpy().sub(0, 0, 0, 0.35F)));
        addStat(Ability.DEFENSEUP, new PowerUpStats(60*20, 15, "Defense\nBoost", "Increases the defense of the player and nearby allies.", Color.TEAL.cpy().sub(0, 0, 0, 0.35F)));
    }
    
    protected Ability type; //tracks the type of power up
    protected long despawnTimer; //amount of time before powerUp despawns
    
            
    /**
     * Constructs a new PowerUp.
     * @param world The world the power up is in
     * @param xLocation The power up's X location
     * @param yLocation The power up's Y location
     * @param ptype The type of power up
     * @param seconds The amount of seconds that the power up will exist
     */
    public EntityPowerUp(GameWorld world, float xLocation, float yLocation, Ability ptype, int seconds)
    {
        super(world, xLocation, yLocation);
        this.type = ptype;
        this.despawnTimer = seconds*60;
        this.spawnInWorld(xLocation, yLocation, 0, 0);
    }
    
    public EntityPowerUp(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        this.type = Ability.valueOf(mapObject.getProperties().get("PowerupType", String.class));
        this.despawnTimer = mapObject.getProperties().get("Despawn", Integer.class);
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(1);
        
        //Set up body definition - Defines the type of physics body that this is
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        
        //Set up physics body - Defines the actual physics body
        this.physBody = this.world.getPhysWorld().createBody(bodyDef);
        this.physBody.setUserData(this); //Store this object into the body so that it isn't lost
        
        //Set up physics fixture - Defines physical properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;
        fixtureDef.density = 100.0F; //About 1 g/cm^2 (2D), which is the density of water, which is roughly the density of humans.
        fixtureDef.friction = 0.1F; //friction with other objects
        fixtureDef.restitution = 0.0F; //Bouncyness
        //fixtureDef.isSensor = true; //Make it not collide, but still make collision events
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_POWERUP;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ (COL_PIRATE_PROJECTILE | COL_SEA_PROJECTILE);
        
        this.physBody.createFixture(fixtureDef);
        
        //Set the linear damping
        this.physBody.setLinearDamping(5F);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    /**
     * Called when a power up is used.
     * @param world The world that the power up is used in
     * @param powerUpType The type of power up that was used
     */
    public static void onUse(GameWorld world, Ability powerUpType)
    {
        //TODO: use the power up.  Use the values in PowerUpStats.
        Gdx.app.log("EntityPowerUp", "onUse(world, " + powerUpType + ")");
        
        for (Body body : world.getPhysBodies())//search for entities on the map
        {
            Object o = body.getUserData();
            
            if (o instanceof EntitySeaCreature)//apply power up if seacreature TODO: Check range
            {
                ((EntitySeaCreature)o).applyPowerUp(powerUpType, getStats(powerUpType).duration);
            }
            //&& this.player.getPos().dst(this.source.getPos()
        }
    }
    
    @Override
    public void update()
    {
        super.update();
        despawnTimer--;
        if (despawnTimer <= 0)
            this.dispose();
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        //render shapes
    }
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        TextureRegion texture = null;
        
        switch (this.type) //Decide which texture to use
        {
        case ATTACKUP:
            texture = GameAssets.powerupTextures[0];
            break;
        case SPEEDUP:
            texture = GameAssets.powerupTextures[1];
            break;
        case HEALUP:
            texture = GameAssets.powerupTextures[2];
            break;
        case DEFENSEUP:
            texture = GameAssets.powerupTextures[3];
            break;
        }
        assert (texture != null); //The texture should not be null
        
        float spriteUnitWidth = 2F;
        batch.draw(texture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }

    /**
     * @return The power up's type
     */
    public Ability getType()
    {
        return type;
    }
    
    /**
     * Gets the stats for a particular type of power up
     * @param ability The type of power up to get stats for
     * @return The PowerUpStats object
     */
    public static PowerUpStats getStats(Ability ability)
    {
        return powerUpStats.get(ability);
    }
    
    /**
     * Adds stats for a particular type of power up
     * @param ability The type of power up to add the stats for
     * @param stats The stats to add
     */
    public static void addStat(Ability ability, PowerUpStats stats)
    {
        powerUpStats.put(ability, stats);
    }

    /**
     * Called when the power up is collided with the player
     */
    public void onPickUp()
    {
        Gdx.app.log("EntityPowerUp", "onPickUp()");
        this.world.addPowerUps(this.type, 1);
        this.dispose(); //TODO: Uncomment this after a way to safely delete entities is made
    }
    
    /**
     * Static inner enumeration type to represent power up Ability types
     */
    public static enum Ability
    {
        SPEEDUP, ATTACKUP, HEALUP, DEFENSEUP
    }

    /**
     * Represents the stats of a power up.
     * @author Dalton
     */
    public static class PowerUpStats
    {
        /** The duration, in ticks, of the power up */
        public final int duration;
        /** The radius of the effect, in meters */
        public final int radius;
        /** The name of the power up */
        public final String name;
        /** A description of the power up */
        public final String description;
        /** The color of the radius preview, with transparency */
        public final Color previewColor;
        
        /**
         * Constructs a new PowerUpStats object.  Register it with EntityPowerUp
         * @param duration The duration, in ticks, of the power up
         * @param radius The radius of the effect, in meters
         * @param name The name of the power up
         * @param description A description of the power up
         * @param previewColor The color of the radius preview, with transparency
         */
        public PowerUpStats(int duration, int radius, String name, String description, Color previewColor)
        {
            this.duration = duration;
            this.radius = radius;
            this.name = name;
            this.description = description;
            this.previewColor = previewColor;
        }
    }
}
