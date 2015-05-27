/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.GameWorld;

/**
 * The base class for UI objects. 
 * 
 * @author Dalton
 */
public class UiObject implements Disposable, Renderable, Comparable<UiObject>
{
    /** The GameRenderer that this belongs to */
    protected GameRenderer renderer;
    /** The GameWorld that this button is for.  Can be null */
    protected GameWorld world;
    
    //X and Y coordinates on the screen
    protected float x;
    protected float y;
    
    //Width and height, in percent of the screen
    protected float width;
    protected float height;
    
    protected int depth; //The depth that the UI Object will be rendered at.  Can be negative
    
    //Constructor
    public UiObject(GameRenderer renderer)
    {
        this(renderer, 0.0F, 0.0F, 0.0F, 0.0F);
    }
    
    //Constructor
    public UiObject(GameRenderer renderer, float x, float y)
    {
        this(renderer, x, y, 0.0F, 0.0F);
    }
    
    //Constructor
    public UiObject(GameRenderer renderer, float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width*Gdx.graphics.getWidth();
        this.height = height*Gdx.graphics.getHeight();
        this.depth = 0;
        this.renderer = renderer;
        this.world = renderer.getWorld();
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        
    }
    
    /**
     * Updates the UI object's logic
     */
    public void onUpdate()
    {
        
    }

    @Override
    public void dispose()
    {
        
    }

    @Override //UI Objects are comparable based on their render depth
    public int compareTo(UiObject other)
    {
        return other.depth - this.depth;
    }
}
