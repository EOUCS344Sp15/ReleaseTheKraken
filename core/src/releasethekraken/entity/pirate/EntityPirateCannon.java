/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.pirate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.projectile.EntityBullet;
import releasethekraken.entity.projectile.EntityCannonBomb;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author tyang
 */
public class EntityPirateCannon extends EntityPirate
{
    
    //Primary constructor
    public EntityPirateCannon(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //Override variables for this type of entity
        this.health = 70;
        this.maxHealth = 70;
        this.points = 30;
        this.coins = 20;
        this.attackRate = 4;
    }
    
    //Secondary constructor
    public EntityPirateCannon(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 70;
        this.maxHealth = 70;
        this.points = 30;
        this.coins = 20;
        this.attackRate = 4;
    }

    @Override
    public void update()
    {
        super.update();
        
        //Acquire a target every second
        if (this.world.getWorldTime() % (1*ReleaseTheKraken.TICK_RATE) == 0)
            this.target = this.world.getClosestTarget(this, EntitySeaCreature.class, 40, true);
        
        //Attack every second
        if (this.world.getWorldTime() % (this.attackRate*ReleaseTheKraken.TICK_RATE) == 0)
        {
           this.attack();
        }
    }
    
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(1);
        
        //Set up body definition - Defines the type of physics body that this is
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
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
        fixtureDef.restitution = 0.0F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_PIRATE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_PIRATE_PROJECTILE; //Collide with everything except pirate projectiles
        
        this.physBody.createFixture(fixtureDef);
        
        //Set the linear damping
        this.physBody.setLinearDamping(7F);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, x, y, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
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
        
        float angle = 0;
        
        //Look at target
        if (this.target != null)
            angle = this.target.getPos().cpy().sub(this.getPos()).angle();
        
        float spriteUnitWidth = 2.0F;
        batch.draw(GameAssets.entityPirateCannonTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitWidth/2,
                1F, //X point to rotate around
                1F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitWidth,
                1.0F, //X scale
                1.0F, //Y scale
                angle);
    }
    
    public void attack()
    {
        float newtonForce = 1000F; //The amount of force applied to the projectile
        
        if(target != null)
        {
            Vector2 difference = this.target.getPos().cpy().sub(this.getPos()); //Get the difference vector
            difference.nor().scl(newtonForce); //Normalize it to a unit vector, and scale it
            new EntityCannonBomb(this.world, this.getPos().x, this.getPos().y, difference.x, difference.y, this, 25);
        } // target
    }
}
