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
public class UiObject implements Disposable
{
    //X and Y percentage coordinates on the screen
    protected float x;
    protected float y;
    
    //Width and height, in percent of the screen
    protected float width;
    protected float height;
    
    //Constructor
    public UiObject()
    {
        this(0.0F, 0.0F);
    }
    
    //Constructor
    public UiObject(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    //Constructor
    public UiObject(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width*Gdx.graphics.getWidth();
        this.height = height*Gdx.graphics.getHeight();
    }
    
    /**
     * Renders the UI object's shape layer.  Assumes that ShapeRenderer.begin()
     * has already been called.  Doesn't call ShapeRenderer.end()
     * 
     * @param shapeRenderer The ShapeRenderer to render with
     */
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        
    }
    
    /**
     * Renders the UI object's sprite layer.  Assumes that SpriteBatch.begin()
     * has already been called.  Doesn't call SpriteBatch.end()
     * 
     * @param batch The SpriteBatch to render with
     */
    public void renderSprites(SpriteBatch batch)
    {
        
    }
    
    /**
     * Updates the UI object's logic
     */
    public void update(GameWorld world)
    {
        
    }

    @Override
    public void dispose()
    {
        
    }
}
