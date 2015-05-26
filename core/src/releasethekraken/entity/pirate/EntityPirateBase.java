/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.pirate;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;

/**
 *
 * @author tyang
 */
public class EntityPirateBase extends EntityPirate
{

    //Primary constructor
    public EntityPirateBase(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //Override variables for this type of entity
        this.health = 1000000;
        this.maxHealth = 1000000;
        this.points = 500;
    }
    
    //Secondary constructor
    public EntityPirateBase(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 1000000;
        this.maxHealth = 1000000;
    }
    
    /**
     * 
     * 
     * @author tyang
     */
    public void regenerateHealth()
    {
        this.health += 1000;
        if (this.health > this.maxHealth)
        {
            this.health = this.maxHealth;
        }
    }
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
    }
    
    @Override
    public void update()
    {
        super.update();
        
        if (this.world.getWorldTime() % (10*60) == 0)
        {
            regenerateHealth();
        }
    }
}
