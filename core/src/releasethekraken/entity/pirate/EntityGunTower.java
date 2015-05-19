/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameAssets;
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
        
        /*
            I suggest that you make use of the EntityProjectile class.  You can
            set the owner to "this" to specify the entity that shot the projectile,
            which will be used in determining what the projectile can collide with
            and damage.  Maybe subclass it to provide different types of projectiles
            such as: EntityBullet, EntityCannonBall, EntityWaterSquirt, etc, each
            with their own unique properties.
                - Dalton
        */
        
        /* This was getting REALLY annoying, so I commented it out :P
        //Code for testing the getClosestTarget method
        if (this.world.getWorldTime() % (5*60) == 0)
        {
            EntitySeaCreature target = this.world.getClosestTarget(this, EntitySeaCreature.class);
            
            Gdx.app.log(this.toString(), "Closest target: " + target);
        }*/
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        float spriteUnitWidth = 2F;
        batch.draw(GameAssets.entityGunTowerTexture,
                this.pos.x - spriteUnitWidth/2,
                this.pos.y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }
}
