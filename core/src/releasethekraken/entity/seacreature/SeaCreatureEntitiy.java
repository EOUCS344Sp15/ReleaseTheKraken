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
public class SeaCreatureEntitiy extends LivingEntity{

    public SeaCreatureEntitiy(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
    }
    public SeaCreatureEntitiy(GameWorld world, RectangleMapObject mapObject)
    {
        super(world, mapObject);
    } 
}
