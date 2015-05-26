/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.EntityPowerUp;

/**
 *
 * @author roelleh
 */
public class EntityPlayer extends EntitySeaCreature
{
    /** The maximum speed the player can move */
    public final float maxSpeed = 4.0F;
    /** The player's movement force, in newtons */
    public final float moveForce = 7500.0F;
    
    /** Which power up to preview the radius for, or null for none */
    public EntityPowerUp.Ability powerUpPreview = null;
    
    //Primary constructor
    public EntityPlayer(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        this.spawnInWorld(xLocation, yLocation, 0, 0);
        
        this.health = 15;
        this.maxHealth = 15;
    }
    
    //Secondary constructor
    public EntityPlayer(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        
        this.health = 15;
        this.maxHealth = 15;
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //Set up hitbox shape - Defines the hitbox
        CircleShape hitbox = new CircleShape();
        hitbox.setRadius(1);
        
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
        fixtureDef.restitution = 0.0F; //Bouncyness
        this.physBody.createFixture(fixtureDef);
        
        //Set the linear damping
        this.physBody.setLinearDamping(5F);
        
        //Apply impulse
        this.physBody.applyLinearImpulse(xVel, yVel, 0, 0, true);
        
        //Dispose of the hitbox shape, which is no longer needed
        hitbox.dispose();
    }
    
    @Override
    public void update()
    {
        super.update();
        
        /*
        //Temporary code to keep the player inside the world bounds, until a collision system is implemented
        if (this.pos.x < 0)
            this.vel.add(1, 0);
        if (this.pos.x > this.world.getWidth())
            this.vel.add(-1, 0);
        if (this.pos.y < 0)
            this.vel.add(0, 1);
        if (this.pos.y > this.world.getHeight())
            this.vel.add(0, -1);
        
        //Apply friction to the player's velocity
        if (this.vel.len() > 0)
        {
            this.vel.scl(0.9F); //Apply friction to the player
            
            //Make the velicity 0 if it is close enough
            if (this.vel.len() < 0.01F)
                this.vel.set(0, 0);
        }
        */
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
        
        //Render power up preview
        if (this.powerUpPreview != null)
        {
            shapeRenderer.end();
            
            //Enable OpenGL alpha blending
            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            EntityPowerUp.PowerUpStats powerUpStats = EntityPowerUp.getStats(this.powerUpPreview);
            
            shapeRenderer.setColor(powerUpStats.previewColor);
            shapeRenderer.circle(this.physBody.getPosition().x, this.physBody.getPosition().y, powerUpStats.radius, 32);
            
            shapeRenderer.end();
            
             //Disable OpenGL blending so everything else doesn't get messed up
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
            
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        }
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        float spriteUnitWidth = 2F;
        batch.draw(GameAssets.entityPlayerTexture,
                this.physBody.getPosition().x - spriteUnitWidth/2,
                this.physBody.getPosition().y - spriteUnitWidth/2,
                spriteUnitWidth,
                spriteUnitWidth);
    }
}
