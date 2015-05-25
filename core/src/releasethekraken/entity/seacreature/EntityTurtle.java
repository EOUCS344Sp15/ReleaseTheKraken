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
 * @author mwinburn
 */
public class EntityTurtle extends EntitySeaCreature 
{
    
    /**
     * Primary Constructor for EntityTurtle
     * @param world Variable containing the game world
     * @param xLocation X Location in the world
     * @param yLocation Y Location in the world
     */
    public EntityTurtle(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        this.health = 20;
        this.maxHealth = 20;
    }
    
    /**
     * Secondary Constructor for EntityTurtle
     * @param world Variable containing the game world
     * @param mapObject Variable holding a texture map
     */
    public EntityTurtle(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        this.health = 20;
        this.maxHealth = 20;
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
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
        batch.draw(GameAssets.entityTurtleTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }
    
    /**
     * Builds and returns a new projectile
     */
    public void attack()
    {
        //this.world.addEntity(new EntityWaterSquirt(world, this.pos.x, this.pos.y, this.vel.x, this.vel.y, this));
    }
}
