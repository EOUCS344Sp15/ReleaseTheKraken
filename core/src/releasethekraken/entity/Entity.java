/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.GameWorld;
import releasethekraken.ui.Renderable;
/**
 * The skeleton class for all entities.
 * 
 * @author Lucas Schuetz
 */
public abstract class Entity implements Disposable, Renderable
{
    /** The game world that the entity is in */
    protected GameWorld world;
    /** The entity's physics body in the physics world */
    protected Body physBody = null;
    
    /**
     *  Default constructor
     * 
     * @param world The GameWorld the entity will be spawned in
     * @param xLocation The initial x coordinate
     * @param yLocation The initial y coordinate
     */
    public Entity(GameWorld world, float xLocation, float yLocation)
    {
        this.world = world;
    }
    
    /**
     * Constructs an Entity from a TextureMapObject that represents it in a
     * level file.  
     * 
     * @param world
     * @param mapObject
     */
    public Entity(GameWorld world, TextureMapObject mapObject)
    {
        this.world = world;
        
        /*
        TODO: I'm not entirely sure the positioning is accurate.  It might only work for 32x32 sprites in the level editor
        I'm also not sure why I have to add the texture height to the Y coordinate
        */
        this.spawnInWorld(
                mapObject.getX()/(this.world.getTiledMapUnitScale()) + (mapObject.getTextureRegion().getRegionWidth()/32F),
                mapObject.getY()/(this.world.getTiledMapUnitScale()) + (mapObject.getTextureRegion().getRegionWidth()/32F) + (mapObject.getTextureRegion().getRegionWidth()/this.world.getTiledMapUnitScale()),
                0,
                0);
    }
    
    /**
     * Spawns the entity in the physics world.  The entity should create its
     * physics body in the physics world, and attach itself to it.  This should
     * be called by the constructor.
     * This <strong>MUST</strong> be implemented, as an entity cannot exist in 
     * the world without doing this.
     * @param x The X position in the world
     * @param y The Y position in the world
     * @param xVel The X velocity
     * @param yVel The Y velocity
     */
    protected abstract void spawnInWorld(float x, float y, float xVel, float yVel);
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        //render shapes
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        //render sprites
    }

    /**
     * Updates the Entity object
     */
    public void update()
    {
        
    }
    
    @Override
    public void dispose()
    {
        //dispose of the entity
        this.world.removeEntity(this);
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "[" + this.physBody.getPosition().x + ", " + this.physBody.getPosition().y + "]";
    }
    
    /**
     * Gets the entity's physics body
     * @return The entity's physics body
     */
    public Body getPhysBody()
    {
        return this.physBody;
    }
    
    /**
     * Gets the entity's position from its physics body
     * @return The vector position of the entity
     */
    public Vector2 getPos()
    {
        return this.physBody.getPosition();
    }
    
    /**
     * Gets the linear velocity of the entity
     * @return The vector linear velocity of the entity
     */
    public Vector2 getVel()
    {
        return this.physBody.getLinearVelocity();
    }
    
    /**
     * Gets the GameWorld that the entity is in
     * @return The GameWorld that the entity is in
     */
    public GameWorld getWorld()
    {
        return this.world;
    }
}
