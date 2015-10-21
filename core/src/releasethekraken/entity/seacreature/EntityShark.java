/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityPirate;
import releasethekraken.entity.projectile.EntityWaterSquirt;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author Dalton Baird
 */
public class EntityShark extends EntitySeaCreature 
{
    /** The shark's current animation */
    private Animation currentAnimation;
    /** The shark's current target */
    private Entity target = null;
    
    /**
     * Primary Constructor for EntityShark
     * @param world Variable containing the game world
     * @param xLocation X Location in the world
     * @param yLocation Y Location in the world
     */
    public EntityShark(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        this.health = 80;
        this.maxHealth = 80;
        this.defaultMoveForce = 7500F;
        this.moveForce = this.defaultMoveForce;
        this.currentAnimation = GameAssets.entitySharkMoveAnimation;
        this.spawnInWorld(xLocation, yLocation, 0, 0);
    }
    
    /**
     * Secondary Constructor for EntityShark
     * @param world Variable containing the game world
     * @param mapObject Variable holding a texture map
     */
    public EntityShark(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        this.health = 80;
        this.maxHealth = 80;
        this.defaultMoveForce = 7500F;
        this.moveForce = this.defaultMoveForce;
        this.currentAnimation = GameAssets.entitySharkMoveAnimation;
    }
    
    @Override
    public void update()
    {
        super.update();
        
        //Move towards enemies
        
        if (this.world.getWorldTime() % ReleaseTheKraken.TICK_RATE == 0) //Acquire a target every second
            this.target = this.world.getClosestTarget(this, EntityPirate.class, 20, true);
        
        Vector2 targetPos;
        
        if (this.target != null)
            targetPos = this.target.getPos(); //Move towards the target
        else
            targetPos = this.world.getPathTargetPos(this); //Follow the path
        
        //Move
        Vector2 difference = targetPos.sub(this.getPos());
        difference.nor().scl(this.moveForce);
        this.physBody.applyForce(difference, this.getPos(), true);
        
        float angleToTarget = targetPos.sub(this.getPos()).angle();
        
        //Convert to -180 to 180
        while (angleToTarget > 180)
            angleToTarget -= 360;
        while (angleToTarget < -180)
            angleToTarget += 360;
        
        //Get the angle difference
        float angle = angleToTarget - (this.physBody.getAngle()*MathUtils.radiansToDegrees);
        
        //Convert to -180 to 180
        while (angle > 180)
            angle -= 360;
        while (angle < -180)
            angle += 360;
        
        //Gdx.app.log(this.getClass().getSimpleName(), String.format("target: %20.20s, targetPos: %20.20s, angleToTarget: %10.10s, relativeAngle: %10.10s", this.target, targetPos, angleToTarget, angle));
        
        //Calculate torque to apply
        float torque = (angle > 0 ? 2000F : -2000F);
        
        //Rotate towards velocity
        this.physBody.applyTorque(torque, true);
        
        //The range at which the attack animation starts playing
        float attackRange = 6;
        
        //Change animation to attack animation if the distance is smaller than attackRange
        if (this.target != null && this.physBody.getPosition().sub(this.target.getPos()).len() < attackRange)
            this.currentAnimation = GameAssets.entitySharkAttackMoveAnimation;
        else
            this.currentAnimation = GameAssets.entitySharkMoveAnimation;
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(2.1F, 0.5F, new Vector2(0, 0), 0);
        
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
        fixtureDef.restitution = 0.0F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_SEA_CREATURE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_SEA_PROJECTILE; //Collide with everything except sea creature projectiles
        
        this.physBody.createFixture(fixtureDef);
        
        //Set the linear damping
        this.physBody.setLinearDamping(5F);
        
        //Set the angular damping
        this.physBody.setAngularDamping(2.5F);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, x, y, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    /**
     * Gets how often the shark causes damage, in ticks.
     * @return how often the shark causes damage, in ticks
     */
    public int getDamageInterval() //TODO: Should the kraken and other attack on collide entities have this too?
    {
        boolean attackPowerUp = this.getPowerUpTime(EntityPowerUp.Ability.ATTACKUP) > 0;
        
        return attackPowerUp ? 3 : 5;
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer, float delta, float runTime)
    {
        super.renderShapes(shapeRenderer, delta, runTime);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch, float delta, float runTime)
    {
        super.renderSprites(batch, delta, runTime);
        
        float spriteUnitWidth = 5.0F;
        float spriteUnitHeight = 2.0F;
        
        //Calculate the 0-360 degree angle
        float angle = (this.physBody.getAngle()*MathUtils.radiansToDegrees) % 360;
        while (angle < 0)
            angle += 360;
        
        batch.draw(this.currentAnimation.getKeyFrame(runTime),
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                spriteUnitWidth/2, //X point to rotate around
                spriteUnitHeight/2, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                (angle > 90 && angle < 270) ? -1.0F: 1.0F, //Y scale
                (float) Math.toDegrees(this.physBody.getAngle()));
        
        //DEBUG
        /*for (int i=0; i < 4; i++)
            batch.draw(GameAssets.entitySharkAttackMoveAnimation.getKeyFrames()[i],
                this.physBody.getPosition().x - spriteUnitWidth/2 + spriteUnitWidth*i,
                this.physBody.getPosition().y - spriteUnitHeight/2 + 4,
                spriteUnitWidth/2, //X point to rotate around
                spriteUnitHeight/2, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                1.0F, //Y scale
                0);
        */
    }
}
