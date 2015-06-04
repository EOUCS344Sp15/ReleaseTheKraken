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
import releasethekraken.entity.Entity;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author sbartlett
 */
public class EntityWaterBomb extends EntityProjectile
{

    public EntityWaterBomb(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner, int damage)
    {
        super(world, xLoc, yLoc, xVel, yVel, owner, damage);
        this.spawnInWorld(xLoc, yLoc, xVel, yVel);
        this.despawnTimer = 16*60;
    }
    
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(0.65F);
        
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
        fixtureDef.density = 200.0F; //This should plow through stuff
        fixtureDef.friction = 0.1F; //friction with other objects
        fixtureDef.restitution = 0.2F; //Bouncyness
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_SEA_PROJECTILE;
        //Set which collision types this object collides with
        fixtureDef.filter.maskBits = COL_ALL ^ COL_SEA_CREATURE; //Collide with everything except sea creatures
        
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
        
        float spriteUnitWidth = 2.0F;
        float spriteUnitHegiht = 2.0F;
        batch.draw(GameAssets.waterBombTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitHegiht/2,
                1F, //X point to rotate around
                1F, //Y point to rotate around
                spriteUnitWidth,
                spriteUnitHegiht,
                1.0F, //X scale
                1.0F, //Y scale
                (float) this.physBody.getAngle());
        
    }
    
    @Override
    public void onImpact()
    {
        //"Splash" with some water squirts!
        /*int splashes = 12;
        Vector2 splashVel = new Vector2(100, 0);
        
        for (int i=0; i<splashes; i++)
        {
            new EntityWaterSquirt(this.world, this.getPos().x, this.getPos().y, splashVel.x, splashVel.y, this.owner, 1);
            splashVel.rotate(((float)splashes/i)*360);
        }
        */ //TODO: This doesn't work, as you can't alter the body list during contacts.
        super.onImpact();
    }
}
