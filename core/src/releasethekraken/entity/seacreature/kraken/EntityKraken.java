/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.seacreature.kraken;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.Entity;
import releasethekraken.entity.pirate.EntityPirate;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 * THE KRAKEN!
 * @author Dalton
 */
public class EntityKraken extends EntitySeaCreature
{
    /** The array of Kraken tenticles */
    private EntityKrakenTenticle[] tenticles;
    /** If the kraken has a target */
    private boolean hasTarget = false;

    public EntityKraken(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        this.health = 500;
        this.maxHealth = 500;
        this.defaultMoveForce = 50000F;
        this.spawnInWorld(xLocation, yLocation, 0, 0);
        
        //The spawn points of the tenticles
        Vector2[] tenticleSpawns =
        {
            new Vector2(2.0F, 2.0F),
            new Vector2(3.0F, 1.0F),
            new Vector2(3.0F, -1.0F),
            new Vector2(2.0F, -2.0F)
        };
        
        //Spawn the tenticles
        this.tenticles = new EntityKrakenTenticle[4];
        for (int i=0; i<this.tenticles.length; i++)
            this.tenticles[i] = new EntityKrakenTenticle(world, xLocation + tenticleSpawns[i].x, yLocation + tenticleSpawns[i].y, this, 3);
    }

    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        PolygonShape hitbox = new PolygonShape();
        //hitbox.setAsBox(3.0F, 2.0F, new Vector2(-0.5F, 0), 0);
        
        float xOffset = -0.5F;
        
        hitbox.set(new float[]
                {
                    -3.0F + xOffset, 0.0F, //Left middle
                    -2.0F + xOffset, 1.5F, //Top left
                    0.0F + xOffset, 2.0F, //Top middle
                    2.0F + xOffset, 1.5F, //Top right
                    3.0F + xOffset, 0.0F, //Right middle
                    2.0F + xOffset, -1.5F, //Bottom right
                    0.0F + xOffset, -2.0F, //Bottom middle
                    -2.0F + xOffset, -1.5F //Bottom left
                });
        
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
        fixtureDef.density = 200.0F;
        fixtureDef.friction = 0.05F; //friction with other objects
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
    
    @Override
    public void update()
    {
        super.update();
        
        this.hasTarget = false;
        
        //Find out if any of the tenticles are targeting stuff
        for (EntityKrakenTenticle tenticle : this.tenticles)
        {
            EntitySeaCreature tenticlePart = tenticle;
            
            while (tenticlePart != null)
            {
                if (tenticlePart instanceof EntityKrakenTenticle)
                {
                    //If any of the tenticles has a target, remember it and break out of the loop
                    if (((EntityKrakenTenticle)tenticlePart).getTarget() != null)
                    {
                        this.hasTarget = true;
                        break;
                    }
                    tenticlePart = ((EntityKrakenTenticle)tenticlePart).getChild(); //Go to next segment
                }
                else if (tenticlePart instanceof EntityKrakenGripper)
                {
                    //If any of the tenticles has a target, remember it and break out of the loop
                    if (((EntityKrakenGripper)tenticlePart).getTarget() != null)
                    {
                        this.hasTarget = true;
                        break;
                    }
                    tenticlePart = null; //The gripper should be the last segment
                }
            }
        }
        
        if (!this.hasTarget) //Only follow the path if there is no target
        {
            Vector2 targetPos = this.world.getPathTargetPos(this);
            Vector2 difference = targetPos.sub(this.getPos());
            difference.nor().scl(this.moveForce);
            this.physBody.applyForce(difference, this.getPos(), true);
        }
        else //Move towards the target
        {
            Entity target = this.world.getClosestTarget(this, EntityPirate.class, 40, true);
        
            if (target != null)
            {
                Vector2 targetPos = target.getPos();
                Vector2 difference = targetPos.sub(this.getPos());
                difference.nor().scl(this.moveForce);
                this.physBody.applyForce(difference, this.getPos(), true);
            }
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
    public void renderSprites(SpriteBatch batch, float delta, float runTime)
    {
        super.renderSprites(batch, delta, runTime);
        
        float spriteUnitWidth = 7.0F;
        float spriteUnitHeight = 4.0F;
        
        //if (this.hasTarget) //Debug target code
        //    batch.setColor(Color.RED);
        
        batch.draw(GameAssets.entityKrakenBodyTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                3.5F, //X point to rotate around
                2F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                1.0F, //Y scale
                (float) Math.toDegrees(this.physBody.getAngle()));
        
        //if (this.hasTarget) //Debug target code
        //    batch.setColor(Color.WHITE);

    }
    
    @Override
    public void attack(int damage)
    {
        EntityPirate target = this.world.getClosestTarget(this, EntityPirate.class);
    }
    
    @Override
    public void dispose()
    {
        for (EntityKrakenTenticle tenticle : this.tenticles)
            tenticle.setParent(null);
        super.dispose();
    }
}
