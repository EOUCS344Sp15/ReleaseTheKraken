/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import releasethekraken.GameAssets;
import releasethekraken.ui.renderer.UiRenderer;

/**
 * Represents a borderless text box on the screen. Draws the text centered on
 * it's position.
 * @author Dalton
 */
public class UiText extends UiObject
{
    /** The text that will be displayed */
    private String text;    

    /**
     * Constructs a new UiText
     * @param renderer The UiRenderer instance
     * @param x The X coordinate, in pixels
     * @param y The Y coordinate, in pixels
     * @param width The width, in percentage of the screen
     * @param height The height, in percentage of the screen
     * @param text The text to display
     */
    public UiText(UiRenderer renderer, float x, float y, float width, float height, String text)
    {
        super(renderer, x, y, width, height);
        this.text = text;
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        float textWidth = GameAssets.fontMain.getSpaceWidth()*this.text.length();
        
        GameAssets.fontMain.draw(batch, this.text, this.x - textWidth/2, this.y, this.width, -1, false);
    }
}
