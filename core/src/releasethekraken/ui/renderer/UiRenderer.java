/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.ui.UiObject;
import releasethekraken.ui.tooltip.ToolTip;

/**
 * This class renders the UI.  It can be extended to render the game world.
 * @author Dalton
 */
public class UiRenderer implements Disposable
{
    /** SpriteBatch to render UI sprites */
    protected SpriteBatch uiSpriteBatch;
    /** ShapeRenderer to render UI shapes */
    protected ShapeRenderer uiShapeRenderer;
    /** The array of UiObjects */
    public Array<UiObject> uiObjects;
    /** The render time, in frames (ticks) */
    protected long renderTime; 

    /**
     * Constructor
     */
    public UiRenderer()
    {
        this.uiSpriteBatch = new SpriteBatch();
        this.uiShapeRenderer = new ShapeRenderer();
        
        this.uiObjects = new Array<UiObject>();
    }
    
    /**
     * Renders the game world.  This method can be completely overridden to
     * customize the order that things are rendered.
     */
    public void render()
    {
        this.renderTime++;
        
        //Clears screen buffer
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.renderUi();
    }
    
    /**
     * Renders the UI
     */
    protected void renderUi()
    {
        //Updates UI objects
        for (UiObject obj : this.uiObjects)
            obj.onUpdate();
        
        //Draws UI Shapes
        this.uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (UiObject obj : this.uiObjects)
            if (!(obj instanceof ToolTip))
                obj.renderShapes(this.uiShapeRenderer);
        
        this.uiShapeRenderer.end();
        
        //Draws UI Sprites
        this.uiSpriteBatch.begin();
        
        for (UiObject obj : this.uiObjects)
            if (!(obj instanceof ToolTip))
                obj.renderSprites(this.uiSpriteBatch);
        
        //this.uiSpriteBatch.draw(GameAssets.texBadlogic, Gdx.graphics.getWidth() - GameAssets.texBadlogic.getWidth(), 0); //Draws LibGDX logo
        
        this.uiSpriteBatch.end();
        
        //Draws UI ToolTip Shapes
        this.uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (UiObject obj : this.uiObjects)
            if (obj instanceof ToolTip)
                obj.renderShapes(this.uiShapeRenderer);
        
        this.uiShapeRenderer.end();
        
        //Draws UI ToolTip Sprites
        this.uiSpriteBatch.begin();
        
        for (UiObject obj : this.uiObjects)
            if (obj instanceof ToolTip)
                obj.renderSprites(this.uiSpriteBatch);

        this.uiSpriteBatch.end();
    }
    
    /**
     * Gets the render time, in frames (ticks)
     * @return The render time, in frames (ticks)
     */
    public long getRenderTime()
    {
        return this.renderTime;
    }
    
    @Override
    public void dispose()
    {
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.uiSpriteBatch.dispose();
        this.uiShapeRenderer.dispose();
    }
    
}
