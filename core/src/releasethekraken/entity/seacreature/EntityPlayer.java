/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.entity.seacreature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.InputHandler;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.projectile.EntitySeaShell;
import static releasethekraken.physics.CollisionFilter.*; //Import the collision bit constants

/**
 *
 * @author roelleh
 */
public class EntityPlayer extends EntitySeaCreature implements InputHandler.KeyListener
{
    /** The maximum speed the player can move */
    public final float MAX_SPEED = 4.0F;
    /** The player's movement force, in newtons */
    public final float MOVE_FORCE = 7500.0F;
    
    /** Which power up to preview the radius for, or null for none */
    public EntityPowerUp.Ability powerUpPreview = null;
    
    /** The position in the world that the player is aiming at */
    private Vector2 aimPos = new Vector2();
    
    /**A move left button has been pressed by the user */ 
    private boolean leftPressedThisTick = false;
    
    /**A move right button has been pressed by the user */ 
    private boolean rightPressedThisTick = false;
    
    /**A move down button has been pressed by the user */ 
    private boolean downPressedThisTick = false;
    
    /**A move up button has been pressed by the user */ 
    private boolean upPressedThisTick = false;
    
    //Primary constructor
    public EntityPlayer(GameWorld world, float xLocation, float yLocation)
    {
        super(world, xLocation, yLocation);
        this.spawnInWorld(xLocation, yLocation, 0, 0);
        
        this.health = 15;
        this.maxHealth = 15;
        ReleaseTheKraken.inputHandler.registerKeyListener(this); //Register as a KeyListener
    }
    
    //Secondary constructor
    public EntityPlayer(GameWorld world, TextureMapObject mapObject)
    {
        super(world, mapObject);
        
        this.health = 15;
        this.maxHealth = 15;
        ReleaseTheKraken.inputHandler.registerKeyListener(this); //Register as a KeyListener
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
        
        //Set which collision type this object is
        fixtureDef.filter.categoryBits = COL_PLAYER | COL_SEA_CREATURE;
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
    public void update()
    {
        super.update();
        
        this.downPressedThisTick = false;
        this.upPressedThisTick = false;
        this.leftPressedThisTick = false;
        this.rightPressedThisTick = false;
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
       /* shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(this.getPos().x - (this.maxHealth * .25f), this.getPos().y + 1.1f, this.maxHealth * .5f, 0.5f);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(this.getPos().x - (this.maxHealth * .25f), this.getPos().y + 1.1f, this.health * .5f, 0.5f);
*/
        //Draw crosshair
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.x(this.aimPos, 1);
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
    
    @Override
    public void dispose()
    {
        ReleaseTheKraken.inputHandler.unregisterKeyListener(this);
        super.dispose();
    }

    /**
     * Gets the player's world aim position
     * @return The player's world aim position
     */
    public Vector2 getAimPos()
    {
        return aimPos;
    }

    @Override
    public void keyDown(int keycode)
    {
        switch (keycode)
        {
        case Input.Keys.SPACE: //Projectile firing
            Vector2 velocity = this.aimPos.cpy().sub(this.getPos()).nor().scl(500); //Calculate direction and velocity to fire at
            float spread = 10F; //The amount of possible spread, in degrees
            velocity.rotate(this.world.random.nextFloat()*spread - spread/2); //Add +- spread/2 degrees of spread
            new EntitySeaShell(this.world, this.getPos().x, this.getPos().y, velocity.x, velocity.y, this);
            break;
        }
    }

    @Override
    public void keyUp(int keycode) {}

    @Override
    public void keyHeld(int keycode)
    {
        switch (keycode)
        {
            case Input.Keys.UP:
            case Input.Keys.W:
                if (!this.upPressedThisTick)
                {
                //if (this.physBody.getLinearVelocity().y < this.MAX_SPEED)
                    this.physBody.applyForceToCenter(0, this.MOVE_FORCE, true);
                    this.upPressedThisTick = true;
                }
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                if (!this.downPressedThisTick)
                {
                //if (this.physBody.getLinearVelocity().y > 0 - this.MAX_SPEED)
                    this.physBody.applyForceToCenter(0, -this.MOVE_FORCE, true);
                    this.downPressedThisTick = true;
                }
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                if(!this.leftPressedThisTick)
                {
                //if (this.physBody.getLinearVelocity().x > 0 - this.MAX_SPEED)
                    this.physBody.applyForceToCenter(-this.MOVE_FORCE, 0, true);
                    this.leftPressedThisTick = true;
                }
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                if(!this.rightPressedThisTick)
                {
                    //if (this.physBody.getLinearVelocity().x < this.MAX_SPEED)
                    this.physBody.applyForceToCenter(this.MOVE_FORCE, 0, true);
                    this.rightPressedThisTick = true;
                }
                break;
            case Input.Keys.Z:
                if(this.health > 1)
                    this.health = this.health - 1;
                break;
            case Input.Keys.X:
                if(this.health == this.maxHealth)
                    this.health = this.maxHealth;
                else
                    this.health = this.health + 1;
                break;
        }
    }
}
