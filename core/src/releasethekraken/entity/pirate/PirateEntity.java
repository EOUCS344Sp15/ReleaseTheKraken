/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.LivingEntity;

/**
 *
 * @author tyang
 */
public class PirateEntity extends LivingEntity
{
    //Primary constructor
    public PirateEntity(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    
    //Secondary constructor
    public PirateEntity(GameWorld world, RectangleMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
    }
}
