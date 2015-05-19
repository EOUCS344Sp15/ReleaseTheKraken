/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import releasethekraken.GameAssets;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.ui.GameRenderer;

/**
 * The tool tip that is shown when hovering over a power up button.
 * @author Dalton
 */
public class PowerUpToolTip extends ToolTip
{
    /** The type of power up shown */
    private EntityPowerUp.Ability powerUpType;
    /** The stats for the power up */
    private EntityPowerUp.PowerUpStats powerUpStats;
    /** The color of the tool tip */
    protected Color color;
    /** The height of the description, in pixels */
    private float descriptionHeight;
    
    /**
     * Constructs a new power up tool tip.
     * @param renderer The GameRenderer reference
     * @param powerUpType The type of power up being shown
     */
    public PowerUpToolTip(GameRenderer renderer, EntityPowerUp.Ability powerUpType)
    {
        super(renderer);
        
        this.powerUpType = powerUpType;
        this.powerUpStats = EntityPowerUp.getStats(powerUpType);
        this.color = Color.valueOf("234399");
        
	this.descriptionHeight = GameAssets.fontMain.getCache().addText(
                "Information:\n" + this.powerUpStats.description,
                0,
                0,
                0.25F*Gdx.graphics.getWidth(),
                -1,
                true).height;
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
        
        if (this.visible)
        {
            //The mouse coordinates
            float mouseX = Gdx.input.getX(0);
            float mouseY = Gdx.input.getY(0);
            
            float textHeight = GameAssets.fontMain.getCapHeight();
            
            float boxWidth = (0.25F + 0.005F)*Gdx.graphics.getWidth();
            float boxHeight = this.descriptionHeight + GameAssets.fontMain.getCapHeight();
            
            float boxX = mouseX + 0.05F*Gdx.graphics.getWidth();
            float boxY = Gdx.graphics.getHeight() - mouseY + (boxHeight - textHeight)/2 + textHeight/2;
            
            float triangleAlignX = boxX;
            
            /*
                Starting and stopping the shape renderer multiple times and copying 
                colors might have an impact on performance.  We might want to find
                a more efficient way of doing this.  Maybe tooltips get their own
                render pass?
            */
            
            shapeRenderer.end(); //End the shape batch, drawing everything it has so far
            
            //Enable OpenGL alpha blending
            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            
            //Start a new shape batch
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            //Render tooltip background
            shapeRenderer.setColor(this.color.cpy().sub(0, 0, 0, 0.35F)); //Make the color transparent
            shapeRenderer.rect(boxX, boxY - boxHeight, boxWidth, boxHeight); //Tooltip background
            shapeRenderer.triangle(mouseX,
                    Gdx.graphics.getHeight() - mouseY,
                    triangleAlignX, 
                    boxY,
                    triangleAlignX, 
                    boxY - boxHeight); //Triangle part
            
            shapeRenderer.end(); //End the shape batch, drawing the transparent tooltip
            
            //Disable OpenGL blending so everything else doesn't get messed up
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
            
            //Start a new shape batch so that it is left in the state it started in
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        }
    }

    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        if (this.visible)
        {
            //The mouse coordinates
            float mouseX = Gdx.input.getX(0);
            float mouseY = Gdx.input.getY(0);
            
            float boxX = mouseX + 0.05F*Gdx.graphics.getWidth();
            float boxY = Gdx.graphics.getHeight() - mouseY + (this.descriptionHeight/2);
            
            this.descriptionHeight = GameAssets.fontMain.draw(
                    batch,
                    "Information:\n" + this.powerUpStats.description,
                    boxX,
                    boxY,
                    0.25F*Gdx.graphics.getWidth(),
                    -1,
                    true).height;
        }
    }
}
