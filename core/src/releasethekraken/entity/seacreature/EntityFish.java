/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.projectile.EntityWaterSquirt;
import releasethekraken.entity.pirate.EntityPirate;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author sbartlett
 */
public class EntityFish extends EntitySeaCreature
{    
    /** The color of the fish */
    private Color color;
    
    private static final Color[] COLORS =
    {
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.ORANGE,
        Color.PURPLE,
        Color.OLIVE,
        Color.YELLOW
    };
    
    /**
     * Primary Constructor for EntityFish
     * @param world Variable containing the game world
     * @param xLocation X Location in the world
     * @param yLocation Y Location in the world
     */
    public EntityFish(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
        this.defaultMoveForce = 2500F;
        this.color = COLORS[world.random.nextInt(COLORS.length)]; //Assign a random color
        this.spawnInWorld(xLocation, yLocation, 0, 0);
    }
    
    /**
     * Secondary Constructor for EntityFish
     * @param world Variable containing the game world
     * @param mapObject Variable holding a texture map
     */
    public EntityFish(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        //This will be implemented when the level loader is written
        
        //TODO: Change these
        this.health = 10;
        this.maxHealth = 10;
        this.defaultMoveForce = 2500F;
        this.color = COLORS[world.random.nextInt(COLORS.length)]; //Assign a random color
    }
    
    @Override
    public void update()
    {
        super.update();
        
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
        //Set up hitbox shape - Defines the hitbox
        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(1.0F, 0.5F, new Vector2(0, 0), 0);
        
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
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer, float delta, float runTime)
    {
        super.renderShapes(shapeRenderer, delta, runTime);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch, float delta, float runTime)
    {
        super.renderSprites(batch, delta, runTime);
        
        float spriteUnitWidth = 2.0F;
        float spriteUnitHeight = 1.0F;
        
        //Calculate the 0-360 degree angle
        float angle = (this.physBody.getAngle()*MathUtils.radiansToDegrees) % 360;
        while (angle < 0)
            angle += 360;
        
        batch.setColor(this.color); //Enable color shading
        batch.draw(GameAssets.entityFishAnimation.getKeyFrame(runTime),
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                1F, //X point to rotate around
                1/2F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                (angle > 90 && angle < 270) ? -1.0F: 1.0F, //Y scale
                (float) Math.toDegrees(this.physBody.getAngle()));
        
        batch.setColor(Color.WHITE); //Disable color shading
        batch.draw(GameAssets.entityFishLayer2Animation.getKeyFrame(runTime),
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHeight/2,
                1F, //X point to rotate around
                1/2F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHeight,
                1.0F, //X scale
                (angle > 90 && angle < 270) ? -1.0F: 1.0F, //Y scale
                (float) Math.toDegrees(this.physBody.getAngle()));
    }
    
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
            new EntityWaterSquirt(this.world, this.getPos().x, this.getPos().y, difference.x, difference.y, this, damage); 
        } // target
    }
}
