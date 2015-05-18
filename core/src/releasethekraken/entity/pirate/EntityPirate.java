/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityLiving;

/**
 *
 * @author tyang
 */
public class EntityPirate extends EntityLiving
{
    /** The amount of points the pirate is worth if killed */
    private int points;
    
    //Primary constructor
    public EntityPirate(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    
    //Secondary constructor
    public EntityPirate(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
    }
    
    /**
     * Gets how many points the pirate is worth
     * @return The amount of points that the pirate is worth
     */
    public int getPoints()
    {
        return this.points;
    }
    
    @Override
    public void onDeath()
    {
        this.world.addPoints(this.points); //Add the points when this entity dies
        super.onDeath();
    }
}
