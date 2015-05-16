/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameWorld;
import releasethekraken.entity.seacreature.EntitySeaCreature;

/**
 *
 * @author tyang
 */
public class EntityGunTower extends EntityPirate
{

    //Primary constructor
    public EntityGunTower(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //Override variables for this type of entity
        this.health = 10;
        this.maxHealth = 10;
    }
    
    //Secondary constructor
    public EntityGunTower(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
    }

    @Override
    public void update()
    {
        super.update();
        
        //Code for testing the getClosestTarget method
        if (this.world.getWorldTime() % (5*60) == 0)
        {
            EntitySeaCreature target = this.world.getClosestTarget(this, EntitySeaCreature.class);
            
            Gdx.app.log(this.toString(), "Closest target: " + target);
        }
    }
}
