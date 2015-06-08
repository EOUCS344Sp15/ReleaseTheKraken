/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.seacreature.kraken;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.Entity;
import releasethekraken.entity.pirate.EntityPirate;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import static releasethekraken.physics.CollisionFilter.COL_ALL;
import static releasethekraken.physics.CollisionFilter.COL_SEA_CREATURE;
import static releasethekraken.physics.CollisionFilter.COL_SEA_PROJECTILE;

/**
 * Represents a Kraken tenticle segment.  
 * @author Dalton
 */
public class EntityKrakenTenticle extends EntitySeaCreature
{
    /** The body that this tenticle are attached to.  Can be null. */
    private EntitySeaCreature parent = null;
    /** The next thing attached to this tenticle.  Can be null. */
    private EntitySeaCreature child = null;
    /** What the entity is targeting */
    private Entity target = null;

    public EntityKrakenTenticle(GameWorld world, float xLocation, float yLocation, EntitySeaCreature parent, int segmentsLeft)
    {
        super(world, xLocation, yLocation);
        this.parent = parent;
        this.health = 100;
        this.maxHealth = 100;
        this.defaultMoveForce = 4000F;
        this.spawnInWorld(xLocation, yLocation, 0, 0);
        
        //Spawn the other segments
        if (segmentsLeft > 0)
           this.child = new EntityKrakenTenticle(world, xLocation + 3, yLocation, this, segmentsLeft - 1);
        else
            this.child = new EntityKrakenGripper(world, xLocation + 3, yLocation, this);
    }

    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //******************** Main Segment ********************
        
        //Set up hitbox shape - Defines the hitbox
        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(2.0F, 0.5F, new Vector2(0, 0), 0);
        
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
        fixtureDef.friction = 0.05F; //friction with other objects
        fixtureDef.restitution = 0.0F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_SEA_CREATURE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_SEA_PROJECTILE; //Collide with everything except sea creature projectiles
        
        this.physBody.createFixture(fixtureDef);
        
        //Set up the physics joint
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(this.parent.getPhysBody(), this.physBody, new Vector2(x - 1.5F, y));
        jointDef.collideConnected = false;
        
        jointDef.enableLimit = true;
        float jointRange = 220*MathUtils.degreesToRadians;
        
        jointDef.lowerAngle = 0 - (jointRange/2);
        jointDef.upperAngle = (jointRange/2);
        
        this.physBody.getWorld().createJoint(jointDef); //Create the physics joint
        
        //Set the linear damping
        this.physBody.setLinearDamping(5F);
        
        //Set the angular damping
        this.physBody.setAngularDamping(2.5F);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, x, y, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    @Override
    public void update()
    {
        super.update();
        
        //Move towards enemies
        
        if (this.world.getWorldTime() % ReleaseTheKraken.TICK_RATE == 0) //Acquire a target every second
            this.target = this.world.getClosestTarget(this, EntityPirate.class, 15, true);
        
        if (this.target != null)
        {
            Vector2 targetPos = this.target.getPos();
            Vector2 difference = targetPos.sub(this.getPos());
            difference.nor().scl(this.moveForce);
            this.physBody.applyForce(difference, this.getPos(), true);
        }
        
        //Get the angle difference
        float angle = this.getVel().angle() - (this.physBody.getAngle()*MathUtils.radiansToDegrees);
        
        //Convert to -180 to 180
        while (angle > 180)
            angle -= 180;
        while (angle < -180)
            angle += 180;
        
        //Calculate torque to apply
        float torque = (angle > 0 ? 500F : -500F);
        
        //Rotate towards velocity
        this.physBody.applyTorque(torque, true);
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer, float delta, float runTime)
    {
        if (this.parent == null) //Only render the health bar if there is no body
            super.renderShapes(shapeRenderer, delta, runTime);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch, float delta, float runTime)
    {
        super.renderSprites(batch, delta, runTime);
        
        float spriteUnitWidth = 4.0F;
        float spriteUnitHeight = 1.0F;
        
        batch.draw(GameAssets.entityKrakenTenticle2Texture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                spriteUnitWidth/2, //X point to rotate around
                spriteUnitHeight/2, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                1.0F, //Y scale
                (float) Math.toDegrees(this.physBody.getAngle()));

    }

    /**
     * Gets the parent this is attached to
     * @return the parent this is attached to
     */
    public EntitySeaCreature getParent()
    {
        return parent;
    }

    /**
     * Sets the parent this is attached to
     * @param parent the new parent
     */
    public void setParent(EntitySeaCreature parent)
    {
        this.parent = parent;
    }
    
    /**
     * Gets the child this is attached to
     * @return the child this is attached to
     */
    public EntitySeaCreature getChild()
    {
        return child;
    }

    /**
     * Sets the parent this is attached to
     * @param child the new parent
     */
    public void setChild(EntitySeaCreature child)
    {
        this.child = child;
    }
    
    @Override
    public void dispose()
    {
        //Tell the connnected parts that this no longer exists
        if (this.parent instanceof EntityKrakenTenticle)
            ((EntityKrakenTenticle)this.parent).setChild(null);
        if (this.child instanceof EntityKrakenTenticle)
            ((EntityKrakenTenticle)this.child).setParent(null);
        else if (this.child instanceof EntityKrakenGripper)
            ((EntityKrakenGripper)this.child).setParent(null);
            
        super.dispose();
    }
    
    /**
     * Gets the Entity that this entity is targeting
     * @return The Entity being targeted
     */
    public Entity getTarget()
    {
        return this.target;
    }
}
