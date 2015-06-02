/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityPirate;
import releasethekraken.entity.projectile.EntityWaterBomb;
import releasethekraken.entity.projectile.EntityWaterSquirt;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author sbartlett
 */
public class EntityOrca extends EntitySeaCreature
{    
    //Primary constructor
    public EntityOrca(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
        this.defaultMoveForce = 3000F;
        this.spawnInWorld(xLocation, yLocation, 0, 0);
    }
    
    //Secondary constructor
    public EntityOrca(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
        this.defaultMoveForce = 3000F;
    }
    
    
    @Override
    public void update()
    {
        super.update();
        
        //Attack every second
        if (this.world.getWorldTime() % 60 == 0)
        {
            int damage = 1;
            if (this.appliedPowerUp == EntityPowerUp.Ability.ATTACKUP)
            {
                damage*=1.5F;
            }
            attack(damage);
        }
    }
    
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(2);
        
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
        fixtureDef.restitution = 0.1F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_SEA_CREATURE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_SEA_PROJECTILE; //Collide with everything except sea creature projectiles
        
        this.physBody.createFixture(fixtureDef);
        
        //Set the linear damping
        this.physBody.setLinearDamping(5F);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, 0, 0, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        float spriteUnitWidth = 6F;
        float spriteUnitHeight = 4F;
        batch.draw(GameAssets.entityOrcaTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                spriteUnitWidth,
                spriteUnitHeight);
    }
    
    /**
     * Builds and adds a new projectile to the world
     */
    @Override
    public void attack(int damage)
    {
        EntityPirate target = this.world.getClosestTarget(this, EntityPirate.class);
        //System.out.println(target.toString());
        
        float newtonForce = 100F; //The amount of force applied to the projectile
        
        if(target != null)
        {
            Vector2 difference = target.getPos().cpy().sub(this.getPos()); //Get the difference vector
            difference.nor().scl(newtonForce); //Normalize it to a unit vector, and scale it
            new EntityWaterBomb(this.world, this.getPos().x, this.getPos().y, difference.x, difference.y, this); 
        } // target
    }
}
