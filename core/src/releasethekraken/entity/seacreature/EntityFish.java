/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;

/**
 *
 * @author sbartlett
 */
public class EntityFish extends EntitySeaCreature
{
    //Primary constructor
    public EntityFish(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
    }
    
    //Secondary constructor
    public EntityFish(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
    }
}
