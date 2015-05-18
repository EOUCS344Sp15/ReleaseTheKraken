/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;

/**
 * Represents the points bar, showing how many points the player has, as well as
 * how much more until you can RELEASE THE KRAKEN!
 * 
 * @author Dalton
 */
public class UiPointsBar extends InteractiveUiObject
{
    /** The minimum value of the progress bar */
    private int min;
    /** The maximum value of the progress bar */
    private int max;
    /** The current value of the progress bar */
    private int value;
    
    /**
     * Constructs a new UiProgressBar
     * @param renderer The GameRenderer instance
     * @param x The X position, in pixels
     * @param y The Y position, in pixels
     * @param width The width of the progress bar, in percentage of the screen width
     * @param height The height of the progress bar, in percentage of the screen height
     */
    public UiPointsBar(GameRenderer renderer, float x, float y, float width, float height)
    {
        super(renderer, x, y, width, height);
        
        this.min = 0;
        this.max = 100;
        this.value = 0;
    }
    
    @Override
    public void update(GameWorld world)
    {
        super.update(world);
        
        //Update the points bar's variables
        this.max = world.getPointsForKraken();
        this.value = world.getPoints();
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
        
        //Draw background
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
        
        float barValue = this.value/(this.max*1F);
        float barWidth = this.width*barValue;
        barWidth = MathUtils.clamp(barWidth, 0, this.width);
        
        //Draw points bar
        final Color barColor = Color.valueOf("FF8800");
        shapeRenderer.setColor(barColor);
        shapeRenderer.rect(this.x, this.y, barWidth, this.height);
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        //Draw points display
        String display = this.value + "/" + this.max;
        GameAssets.fontMain.draw(batch,
                display,
                this.x + 0.05F*this.width,
                this.y + this.height/2 + GameAssets.fontMain.getCapHeight()/2,
                this.width,
                -1,
                false);
    }
}
