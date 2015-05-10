/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import releasethekraken.GameAssets;
import releasethekraken.GameRenderer;

/**
 * Represents a ToolTip that displays text.
 * @author Dalton
 */
public class TextToolTip extends ToolTip
{
    protected String text; //The text to display
    protected Color color;
    
    public TextToolTip(GameRenderer renderer, String text)
    {
        super(renderer, 0.0F, 0.0F);
        this.text = text;
        this.color = Color.valueOf("234399");
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer) //TODO: Make tool tip display better
    {
        super.renderShapes(shapeRenderer);
        
        if (this.visible)
        {
            float textHeight = GameAssets.fontMain.getCapHeight();
            float boxWidth = 0.16F*Gdx.graphics.getWidth(); //TODO: Take into account the width of the text
            float boxHeight = textHeight*1.8F;
            
            //The mouse coordinates
            float mouseX = Gdx.input.getX(0);
            float mouseY = Gdx.input.getY(0);
            
            float boxX = mouseX + 0.05F*Gdx.graphics.getWidth();
            float boxY = Gdx.graphics.getHeight() - mouseY - textHeight - boxHeight/4;

            shapeRenderer.setColor(this.color);
            shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight); //Tooltip background
            shapeRenderer.triangle(mouseX,
                    Gdx.graphics.getHeight() - mouseY,
                    boxX, 
                    boxY,
                    boxX, 
                    boxY + boxHeight); //Triangle part
            
        }
    }

    @Override
    public void renderSprites(SpriteBatch batch) //TODO: Make tool tip display better
    {
        super.renderSprites(batch);
        
        if (this.visible)
        {
            //The mouse coordinates
            float mouseX = Gdx.input.getX(0);
            float mouseY = Gdx.input.getY(0);
            float boxX = mouseX + 0.05F*Gdx.graphics.getWidth();
            float boxY = Gdx.graphics.getHeight() - mouseY;

            GameAssets.fontMain.draw(batch, this.text, 
                    boxX, 
                    boxY, 
                    1.0F, -1, false);
        }
    }
}
