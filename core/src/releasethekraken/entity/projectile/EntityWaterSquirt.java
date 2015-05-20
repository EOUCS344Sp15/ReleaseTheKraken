/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.Entity;

/**
 *
 * @author sbartlett
 */
public class EntityWaterSquirt extends EntityProjectile
{

    public EntityWaterSquirt(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner) 
    {
        super(world, xLoc, yLoc, xVel, yVel, owner);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        float spriteUnitWidth = 2F;
        batch.draw(GameAssets.entityFishTexture, //TODO: Need a sprite for WaterSquirt
                this.pos.x - spriteUnitWidth/2,
                this.pos.y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }
    
}
