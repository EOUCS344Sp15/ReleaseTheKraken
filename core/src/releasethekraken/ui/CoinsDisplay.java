/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import releasethekraken.GameAssets;

/**
 * Displays the amount of coins that the player has.
 * 
 * @author Dalton
 */
public class CoinsDisplay extends UiObject
{
    /** A local copy of the number of coins the player has */
    private int coins = 0;

    /**
     * Constructs a new CoinsDisplay.
     * @param renderer The GameRenderer reference
     * @param x The X coordinate, in pixels
     * @param y The Y coordinate, in pixels
     * @param width The width, in percentage of the screen width
     * @param height The height, in percentage of the screen height
     */
    public CoinsDisplay(GameRenderer renderer, float x, float y, float width, float height)
    {
        super(renderer, x, y, width, height);
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
        
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        batch.draw(GameAssets.coinTexture,
                this.x + 0.1F*this.height,
                this.y + 0.1F*this.height,
                this.height*0.8F,
                this.height*0.8F);
        
        GameAssets.fontMain.draw(batch,
                this.coins + " coins",
                this.x + 0.2F*this.width,
                this.y + this.height/2 + GameAssets.fontMain.getCapHeight()/2,
                this.width,
                -1,
                false);
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        this.coins = this.world.getCoins(); //Update the amount of coins to display
    }
}
