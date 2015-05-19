/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityPowerUp;

/**
 *
 * @author roelleh
 */
public class EntityPlayer extends EntitySeaCreature
{
    /** The maximum speed the player can move */
    public final float maxSpeed = 4.0F;
    
    /** Which power up to preview the radius for, or null for none */
    public EntityPowerUp.Ability powerUpPreview = null;
    
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
        
        this.health = 15;
        this.maxHealth = 15; 
    }
    
    @Override
    public void update()
    {
        super.update();
        
        //Temporary code to keep the player inside the world bounds, until a collision system is implemented
        if (this.pos.x < 0)
            this.vel.add(1, 0);
        if (this.pos.x > this.world.getWidth())
            this.vel.add(-1, 0);
        if (this.pos.y < 0)
            this.vel.add(0, 1);
        if (this.pos.y > this.world.getHeight())
            this.vel.add(0, -1);
        
        //Apply friction to the player's velocity
        if (this.vel.len() > 0)
        {
            this.vel.scl(0.9F); //Apply friction to the player
            
            //Make the velicity 0 if it is close enough
            if (this.vel.len() < 0.01F)
                this.vel.set(0, 0);
        }
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
        
        //Render power up preview
        if (this.powerUpPreview != null)
        {
            shapeRenderer.end();
            
            //Enable OpenGL alpha blending
            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            EntityPowerUp.PowerUpStats powerUpStats = EntityPowerUp.getStats(this.powerUpPreview);
            
            shapeRenderer.setColor(powerUpStats.previewColor);
            shapeRenderer.circle(this.pos.x, this.pos.y, powerUpStats.radius, 32);
            
            shapeRenderer.end();
            
             //Disable OpenGL blending so everything else doesn't get messed up
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        }
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        float spriteUnitWidth = 2F;
        batch.draw(GameAssets.entityPlayerTexture,
                this.pos.x - spriteUnitWidth/2,
                this.pos.y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }
}
