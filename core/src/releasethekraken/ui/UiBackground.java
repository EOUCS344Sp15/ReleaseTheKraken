/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import releasethekraken.ui.renderer.UiRenderer;

/**
 * The 
 * @author lschuetz
 */
public class UiBackground extends UiObject
{    
    //static Texture background;
    
    public UiBackground(UiRenderer renderer, Pixmap newBackground)
    {
        super(renderer, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        /*if (background != null)
        {
            background.dispose();
            background = null;
        }*/
        
        //background = new Texture(newBackground);
        newBackground.dispose();
        
        this.depth = 9999;
    } // end constructor
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        //batch.draw(background, x, y, width, height);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        /*if (background != null)
        {
            background.dispose();
            background = null;
        }*/
    } // end dispose
} // end class
