/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.Entity;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author sbartlett
 */
public class EntityCannonBomb extends EntityProjectile
{

    public EntityCannonBomb(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner, int damage)
    {
        super(world, xLoc, yLoc, xVel, yVel, owner, damage);
        this.spawnInWorld(xLoc, yLoc, xVel, yVel);
        this.despawnTimer = 6*ReleaseTheKraken.TICK_RATE;
        this.minVel = 4.0F;
    }
    
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(0.35F);
        
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
        fixtureDef.density = 500.0F; //About 1 g/cm^2 (2D), which is the density of water, which is roughly the density of humans.
        fixtureDef.friction = 0.1F; //friction with other objects
        fixtureDef.restitution = 0.1F; //Bouncyness
        
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
        
        float spriteUnitWidth = 0.7F;
        float spriteUnitHeight = 0.7F;
        batch.draw(GameAssets.cannonBallTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                0.35F, //X point to rotate around
                0.35F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                1.0F, //Y scale
                (float) this.physBody.getLinearVelocity().angle());
        
    }

    @Override
    public void onDespawn()
    {
        this.onImpact(); //Splash damage!
    }
    
    @Override
    public void onImpact()
    {
        final int bombDamage = this.damage;
        final GameWorld bombWorld = this.world;
        final Vector2 bombPos = this.physBody.getPosition().cpy();
        final Entity bombOwner = this.owner; //TODO: This could be effected by the same bug as Issue #91.
        
        //"Splash" with some water squirts!
        this.world.addSpawnTask(new GameWorld.BodySpawnTask()
        {
            @Override
            protected void doTask()
            {
                new EntityExplosion(bombWorld, bombPos.x, bombPos.y, bombOwner, damage, 2F);
            }
        });
        
        super.onImpact();
    }
}
