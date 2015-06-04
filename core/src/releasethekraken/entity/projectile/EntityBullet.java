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
 *
 * @author sbartlett
 */
public class EntityBullet extends EntityProjectile
{

    public EntityBullet(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner, int damage)
    {
        super(world, xLoc, yLoc, xVel, yVel, owner, damage);
        this.spawnInWorld(xLoc, yLoc, xVel, yVel);
        this.despawnTimer = 8*60;
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
        bodyDef.fixedRotation = true;
        
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
        fixtureDef.filter.categoryBits = COL_PIRATE_PROJECTILE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_PIRATE; //Collide with everything except pirates
        
        this.physBody.createFixture(fixtureDef);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, x, y, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    @Override
    public void renderSprites(SpriteBatch batch, float delta, float runTime)
    {
        super.renderSprites(batch, delta, runTime);
        
        float spriteUnitWidth = 0.5F;
        float spriteUnitHeight = 0.5F;
        batch.draw(GameAssets.cannonBallTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                1/4F, //X point to rotate around
                1/4F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                1.0F, //Y scale
                (float) this.physBody.getLinearVelocity().angle());
        
    }
}
