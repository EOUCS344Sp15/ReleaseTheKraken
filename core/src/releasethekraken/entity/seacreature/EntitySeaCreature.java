/*
 * Base Class for all the creatures as per design.
 */
package releasethekraken.entity.seacreature;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityLiving;

/**
 *
 * @author mwinburn
 */
public class EntitySeaCreature extends EntityLiving
{
    //Primary constructor
    public EntitySeaCreature(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    
    //Secondary constructor
    public EntitySeaCreature(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
    }
}
