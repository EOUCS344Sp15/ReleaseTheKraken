package releasethekraken.entity.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.Entity;
import static releasethekraken.physics.CollisionFilter.COL_ALL;
import static releasethekraken.physics.CollisionFilter.COL_PIRATE;
import static releasethekraken.physics.CollisionFilter.COL_PIRATE_PROJECTILE;

/**
 * An explosion entity that explodes!
 * 
 * @author Dalton
 */
public class EntityExplosion extends EntityProjectile
{
    /** The radius of the explosion */
    protected float radius;
    
    public EntityExplosion(GameWorld world, float xLoc, float yLoc, Entity owner, int damage, float radius)
    {
        super(world, xLoc, yLoc, 0, 0, owner, damage);
        this.despawnTimer = 1*ReleaseTheKraken.TICK_RATE;
        this.minVel = 0.0F;
        this.radius = radius;
        this.spawnInWorld(xLoc, yLoc, 0, 0);
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(this.radius);
        
        //Set up body definition - Defines the type of physics body that this is
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        
        //Set up physics body - Defines the actual physics body
        this.physBody = this.world.getPhysWorld().createBody(bodyDef);
        this.physBody.setUserData(this); //Store this object into the body so that it isn't lost
        
        //Set up physics fixture - Defines physical properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true; //Don't "teleport" other entities out of the way when it spawns on them
        fixtureDef.shape = hitbox;
        fixtureDef.density = 100.0F; //About 1 g/cm^2 (2D), which is the density of water, which is roughly the density of humans.
        fixtureDef.friction = 0.1F; //friction with other objects
        fixtureDef.restitution = 0.0F; //Bouncyness
        
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
        
        float spriteUnitWidth = this.radius*2;
        float spriteUnitHeight = this.radius*2;
        batch.draw(GameAssets.waterBombTexture, //TODO: Epic explosion effects!
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                this.radius, //X point to rotate around
                this.radius, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                0.65F*this.radius, //X scale
                0.65F*this.radius, //Y scale
                this.world.getWorldTime()*4); //Make it rapidly rotate for now
                //(float) this.physBody.getLinearVelocity().angle());
        
    }
}
