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
    
    @Override
    public void update()
    {
        super.update();
    }
}
