/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.GameWorld;
import releasethekraken.ui.Renderable;
/**
 * The skeleton class for all entities.
 * 
 * changelog:
 * 
 * --5/11/15--
 * -fixed more documentation
 * -Added default implementation for the constructors
 * 
 * --5/9/15--
 * -added variables
 *  -xLoc - X Location
 *  -yLoc - Y Location
 *  -sprite - Sprite for the entity to display
 * 
 * -added methods
 *  -render() - used to draw the sprite
 *  -update() - used to update (move, add/remove health/stats)
 *  -dispose() - overriden from the Disposable class
 * 
 * -implemented Renderable
 * 
 * --5/10/15--
 * -added variables
 *  -float xVel - X Velocity
 *  -float yVel - Y Velocity
 *  -GameWorld world - where the entity exists
 * 
 * -changed variables
 *  -int xLoc --> float xLoc
 *  -int yLoc --> float yLoc
 * 
 * -removed
 *  -render() - removed and replaced by the following added methods
 *  -Sprite sprite - sprite will be stored in GameAssets file
 * 
 * -added methods
 *  -renderShapes(ShapeRenderer)
 *  -renderSprite(SpriteBatch)
 *  -default constructor
 *      -takes GameWorld and the x/y coordinates as arguments
 *  -constructor
 *      -takes GameWorld and RectangleMapObject as arguments
 * 
 * @author Lucas Schuetz
 */
public class Entity implements Disposable, Renderable
{
    /** The game world that the entity is in */
    protected GameWorld world;
    
    /** The position of the entity */
    protected Vector2 pos;
    
    /** The velocity of the entity */
    protected Vector2 vel;
    
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
        this.pos = new Vector2(xLocation, yLocation);
        this.vel = new Vector2(0, 0);
    }
    
    /**
     * Constructs an Entity from a RectangleMapObject that represents it in a
     * level file.  
     * 
     * @param world
     * @param mapObject
     */
    public Entity(GameWorld world, RectangleMapObject mapObject)
    {
        this.world = world;
        
        //Override this to parse data from the entity's node in the map format
    }
    
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
        //update the entity
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
        return this.getClass().getSimpleName() + "[" + this.pos.x + ", " + this.pos.y + "]";
    }

    /**
     * @return The entity's position
     */
    public Vector2 getPos()
    {
        return pos;
    }

    /**
     * @return The entity's velocity
     */
    public Vector2 getVel()
    {
        return vel;
    }
}
