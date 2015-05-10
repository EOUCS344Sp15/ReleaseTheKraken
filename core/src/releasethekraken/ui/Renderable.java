/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * A renderable interface.  This adds two methods for rendering with LibGDX.
 * renderShapes() will be called before renderSprites(), so sprites will appear
 * on top of any rendered shapes.
 * @author Dalton
 */
public interface Renderable
{    
    /**
     * Renders the shape layer.  Assumes that ShapeRenderer.begin()
     * has already been called.  Doesn't call ShapeRenderer.end()
     * 
     * @param shapeRenderer The ShapeRenderer to render with
     */
    public void renderShapes(ShapeRenderer shapeRenderer);
    
    /**
     * Renders the sprite layer.  Assumes that SpriteBatch.begin()
     * has already been called.  Doesn't call SpriteBatch.end()
     * 
     * @param batch The SpriteBatch to render with
     */
    public void renderSprites(SpriteBatch batch);
}
