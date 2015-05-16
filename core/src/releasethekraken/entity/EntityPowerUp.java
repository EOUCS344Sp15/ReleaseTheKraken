/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;

/**
 *
 * @author tarstarkes
 */
public class EntityPowerUp extends Entity
{
    protected String name; //The name of the powerup
    protected String description; //detailed description of the power up
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
    }
    
    public EntityPowerUp(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        this.type = Ability.valueOf(mapObject.getProperties().get("PowerupType", String.class));
        this.despawnTimer = mapObject.getProperties().get("Despawn", Integer.class);
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
        //render sprites
    }

    /**
     * @return The power up's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The power up's description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return The power up's type
     */
    public Ability getType()
    {
        return type;
    }
    
    
    
    /**
     * Static inner enumeration type to represent power up Ability types
     */
    public static enum Ability
    {
        SPEEDUP, ATTACKUP, HEALUP
    }
}
