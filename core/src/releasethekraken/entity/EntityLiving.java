/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.pirate.EntityPirateBase;
import releasethekraken.entity.seacreature.EntityOrca;
import releasethekraken.entity.seacreature.kraken.EntityKraken;

/**
 * Represents a living entity with health that can die.
 * @author Dalton
 */
public abstract class EntityLiving extends Entity
{
    /** The entity's health */
    protected int health;
    /** The entity's maximum health */
    protected int maxHealth;
    
    /**
     *  Default constructor
     * 
     * @param world The GameWorld the entity will be spawned in
     * @param xLocation The initial x coordinate
     * @param yLocation The initial y coordinate
     */
    public EntityLiving(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    
    /**
     * Constructs an Entity from a TextureMapObject that represents it in a
     * level file.  
     * 
     * @param world
     * @param mapObject
     */
    public EntityLiving(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        
        //Override this to parse data from the entity's node in the map format
    }

    @Override
    public void update()
    {
        super.update();
        if (this.health <= 0)
            this.onDeath();
    }
    
    /**
     * Handles damage passed to the entity.  Returns true if the damage was taken,
     * false otherwise.
     * @param damage
     * @return 
     */
    public boolean onDamage(int damage)
    {
        if (damage > 0) //Take damage if there is damage to take
        {
            this.health -= damage;
            if (this.health <= 0) //If the damage killed the entity, call onDeath()
                this.onDeath();
            return true;
        }
        return false;
    }
    
    /**
     * Called when the entity dies
     */
    public void onDeath()
    {
        this.dispose();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        //dispose of the entity
    }
    
    /**
     * @return The entity's health
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * @return The entity's max health
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer, float delta, float runTime)
    {
        super.renderShapes(shapeRenderer, delta, runTime);
        
        float width = 5F;
        float height = 0.5F;
        float healthRatio = this.health/((float)this.maxHealth);
        float yOffset = 1.1F;
        
        if (this instanceof EntityOrca)
            yOffset += 1.0F;
        else if (this instanceof EntityPirateBase)
            yOffset += 3.0F;
        else if (this instanceof EntityKraken)
            yOffset += 2.0F;
       
        //Draw background
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(
                this.getPos().x - (width/2),
                this.getPos().y + yOffset,
                width,
                height);
        
        //Calculate the healthbar color
        final Color fullColor = Color.valueOf("0FB300");
        Color healthColor = Color.RED.cpy().lerp(fullColor, healthRatio);

        //Draw health bar
        shapeRenderer.setColor(healthColor);
        shapeRenderer.rect(
                this.getPos().x - (width/2), 
                this.getPos().y + yOffset, 
                healthRatio * width, 
                height);

    }
}
