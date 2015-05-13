/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import releasethekraken.GameWorld;

/**
 *
 * @author tyang
 */
public class BasicGunEntity extends PirateEntity
{

    //Primary constructor
    public BasicGunEntity(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //Override variables for this type of entity
        this.health = 10;
        this.maxHealth = 10;
    }
    
    //Secondary constructor
    public BasicGunEntity(GameWorld world, RectangleMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
    }

    @Override
    public void update()
    {
        super.update();
    }
}
