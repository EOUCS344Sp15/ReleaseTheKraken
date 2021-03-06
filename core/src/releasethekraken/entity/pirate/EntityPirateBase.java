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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author tyang
 */
public class EntityPirateBase extends EntityPirate
{

    //Primary constructor
    public EntityPirateBase(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //Override variables for this type of entity
        this.health = 2000;//OLD VALUE 1000000
        this.maxHealth = 2000;
        this.points = 500;
        
        this.spawnInWorld(xLocation, yLocation, 0, 0);
    }
    
    //Secondary constructor
    public EntityPirateBase(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 2000;
        this.maxHealth = 2000;
        this.points = 500;
    }
    
    /**
     * 
     * 
     * @author tyang
     */
    public void regenerateHealth()
    {
        this.health += 15;//New health regen, old was 1000
        if (this.health > this.maxHealth)
        {
            this.health = this.maxHealth;
        }
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
        //Set up hitbox shape - Defines the hitbox
         PolygonShape hitbox1 = new PolygonShape();
        hitbox1.setAsBox(4, 3-0.1F);
        
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
        fixtureDef.shape = hitbox1;
        fixtureDef.density = 80.0F;
        fixtureDef.friction = 0.1F; //friction with other objects
        fixtureDef.restitution = 0.1F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_PIRATE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_PIRATE_PROJECTILE; //Collide with everything except pirate projectiles
        
        this.physBody.createFixture(fixtureDef);
        
        //Other fixtures
        CircleShape hitbox2 = new CircleShape();
        hitbox2.setRadius(3);
        hitbox2.setPosition(new Vector2(-3, 0));
        
        fixtureDef.shape = hitbox2;
        this.physBody.createFixture(fixtureDef);

        hitbox2.setPosition(new Vector2(3, 0));
        
        fixtureDef.shape = hitbox2;
        this.physBody.createFixture(fixtureDef);
        
        //Set the linear damping
        this.physBody.setLinearDamping(7F);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, x, y, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox1.dispose();
    }
    
    @Override
    public void update()
    {
        super.update();
        
        if (this.world.getWorldTime() % (10*60) == 0) //Once every 10 seconds
        {
            regenerateHealth();
        }
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
        
        float spriteUnitWidth = 6*2F;
        float spriteUnitHeight = 4*2F;
        batch.draw(GameAssets.entityPirateBaseTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                spriteUnitWidth,
                spriteUnitHeight);
    }
    
    @Override
    public void onDeath()
    {
        this.world.setPirateBase(null);
        
        super.onDeath();
    }
}
