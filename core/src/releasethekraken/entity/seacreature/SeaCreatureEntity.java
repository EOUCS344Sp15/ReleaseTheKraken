/*
 * Base Class for all the creatures as per design.
 */
package releasethekraken.entity.seacreature;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.LivingEntity;

/**
 *
 * @author mwinburn
 */
public class SeaCreatureEntity extends LivingEntity
{
    //Primary constructor
    public SeaCreatureEntity(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    
    //Secondary constructor
    public SeaCreatureEntity(GameWorld world, RectangleMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
    }
}
