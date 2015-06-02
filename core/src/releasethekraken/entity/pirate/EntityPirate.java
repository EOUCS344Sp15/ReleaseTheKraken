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
 * Last Edit: 
 * @author tyang
 */
public abstract class EntityPirate extends EntityLiving
{
    /** The amount of points the pirate is worth if killed */
    protected int points;
    /** How many coins you get for killing the pirate */
    protected int coins;
    /** The amount of time between attack function calls (measured in seconds).*/
    protected int attackRate;
    
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
        //Gdx.app.log(this.toString(), "onDeath() Adding " + this.points + " points!");
        this.world.addPoints(this.points); //Add the points when this entity dies
        this.world.addCoins(this.coins); //Add the coins when this entity dies
        super.onDeath();
    }
}
