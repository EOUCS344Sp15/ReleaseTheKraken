/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.obstacle;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityLiving;

/** The EntityObstacle class represents the Obstacles that are present in the 
 *  game. The entity Obstacle extends EntityLiving.
 *
 * @author Mitch
 */
public abstract class EntityObstacle extends EntityLiving
{

    /**
     * Primary constructor
     * @param world
     * @param xLocation
     * @param yLocation 
     */
    public EntityObstacle(GameWorld world, float xLocation, float yLocation) 
    {
        super(world, xLocation, yLocation);
    }
    
    /**
     * Secondary constructor
     * @param world
     * @param mapObject 
     */
    public EntityObstacle(GameWorld world, TextureMapObject mapObject)
    {
       super(world, mapObject);
    }
    
}
