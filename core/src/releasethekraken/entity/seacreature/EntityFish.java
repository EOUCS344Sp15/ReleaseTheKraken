/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.projectile.EntityProjectile;
import releasethekraken.entity.projectile.EntityWaterSquirt;

/**
 *
 * @author sbartlett
 */
public class EntityFish extends EntitySeaCreature
{    
    /**
     * Primary Constructor for EntityFish
     * @param world Variable containing the game world
     * @param xLocation X Location in the world
     * @param yLocation Y Location in the world
     */
    public EntityFish(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
    }
    
    /**
     * Secondary Constructor for EntityFish
     * @param world Variable containing the game world
     * @param mapObject Variable holding a texture map
     */
    public EntityFish(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
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
        batch.draw(GameAssets.entityFishTexture,
                this.pos.x - spriteUnitWidth/2,
                this.pos.y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }
    
    /**
     * Builds and returns a new projectile
     * @param world The world that the projectile is in
     */
    public void attack()
    {
        this.world.addEntity(new EntityWaterSquirt(world, this.pos.x, this.pos.y, this.vel.x, this.vel.y, this));
    }
}
