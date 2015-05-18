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
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.GameWorld;
import releasethekraken.ui.Renderable;
/**
 * The skeleton class for all entities.
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
        this.pos = new Vector2(
                mapObject.getX()/(this.world.getTiledMapUnitScale()) + (mapObject.getTextureRegion().getRegionWidth()/32F), 
                mapObject.getY()/(this.world.getTiledMapUnitScale()) + (mapObject.getTextureRegion().getRegionWidth()/32F) + (mapObject.getTextureRegion().getRegionWidth()/this.world.getTiledMapUnitScale()));
        this.vel = new Vector2(0, 0);
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
        this.pos.add(this.vel.x/60F, this.vel.y/60F); //Move the entity with their velocity
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
