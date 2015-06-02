/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.Entity;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 * Represents a sea shell projectile that the player shoots
 * @author Dalton
 */
public class EntitySeaShell extends EntityProjectile
{
    /** The index of the array of sea shell textures */
    private int textureIndex;
    
    public EntitySeaShell(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner, int damage)
    {
        super(world, xLoc, yLoc, xVel, yVel, owner, damage);
        this.spawnInWorld(xLoc, yLoc, xVel, yVel);
        this.despawnTimer = 2*60;
        
        //Set the texture index to a random index
        this.textureIndex = world.random.nextInt(GameAssets.seaShellTextures.length);
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(0.25F);
        
        //Set up body definition - Defines the type of physics body that this is
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        
        //Set up physics body - Defines the actual physics body
        this.physBody = this.world.getPhysWorld().createBody(bodyDef);
        this.physBody.setUserData(this); //Store this object into the body so that it isn't lost
        
        //Set up physics fixture - Defines physical properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;
        fixtureDef.density = 100.0F; //About 1 g/cm^2 (2D), which is the density of water, which is roughly the density of humans.
        fixtureDef.friction = 0.1F; //friction with other objects
        fixtureDef.restitution = 0.2F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_SEA_PROJECTILE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_SEA_CREATURE; //Collide with everything except sea creatures
        
        //Gdx.app.log("EntitySeaShell", "Category Bits: " + Integer.toBinaryString(fixtureDef.filter.categoryBits) + " Hex: " + Integer.toHexString(fixtureDef.filter.categoryBits));
        //Gdx.app.log("EntitySeaShell", "Mask Bits: " + Integer.toBinaryString(fixtureDef.filter.maskBits) + " Hex: " + Integer.toHexString(fixtureDef.filter.maskBits));
        
        this.physBody.createFixture(fixtureDef);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, 0, 0, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        float spriteUnitWidth = 0.5F;
        batch.draw(GameAssets.seaShellTextures[this.textureIndex],
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitWidth/2,
                1/4F, //X point to rotate around
                1/4F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitWidth,
                1.0F, //X scale
                1.0F, //Y scale
                (float) Math.toDegrees(this.physBody.getAngle()));
    }
}
