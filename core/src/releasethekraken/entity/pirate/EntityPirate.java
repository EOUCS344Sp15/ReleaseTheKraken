/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityLiving;

/**
 *
 * @author tyang
 */
public class EntityPirate extends EntityLiving
{
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
}
