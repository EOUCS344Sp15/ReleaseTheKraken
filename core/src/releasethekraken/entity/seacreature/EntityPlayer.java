/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;




import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;

/**
 *
 * @author roelleh
 */
public class EntityPlayer extends EntitySeaCreature
{
    //Primary constructor
    public EntityPlayer(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        this.health = 15;
        this.maxHealth = 15;      
    }
    
    //Secondary constructor
    public EntityPlayer(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        this.health = 15;
        this.maxHealth = 15; 
    }
    
 
}
