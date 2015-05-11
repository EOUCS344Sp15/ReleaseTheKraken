/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
    protected GameWorld world; //The game world that the entity is in
    
    //The X and Y coordinates of the entity
    protected float xLoc;
    protected float yLoc;
    
    //The X and Y velocity of the entity
    protected float xVel;
    protected float yVel;
    
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
        this.xLoc = xLocation;
        this.yLoc = yLocation;
        this.xVel = 0;
        this.yVel = 0;
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
    }
}
