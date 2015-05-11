/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import releasethekraken.GameAssets;
import releasethekraken.ui.GameRenderer;

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
            //The mouse coordinates
            float mouseX = Gdx.input.getX(0);
            float mouseY = Gdx.input.getY(0);
            
            float textHeight = GameAssets.fontMain.getCapHeight();
            float textWidth = GameAssets.fontMain.getSpaceWidth()*this.text.length();
            
            float boxWidth = textWidth;
            float boxHeight = textHeight*1.8F;
            
            float boxX = mouseX + 0.05F*Gdx.graphics.getWidth();
            float boxY = Gdx.graphics.getHeight() - mouseY + (boxHeight - textHeight)/2 + textHeight/2;
            
            float triangleAlignX = boxX;
            
            //Swap the tooltip to the other side if it's on the other side of the screen
            if (mouseX > Gdx.graphics.getWidth()/2)
            {
                boxX = mouseX - 0.05F*Gdx.graphics.getWidth() - textWidth;
                triangleAlignX = boxX + boxWidth;
            }
            
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
            shapeRenderer.setColor(this.color.cpy().sub(0, 0, 0, 0.5F)); //Make the color transparent
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
    public void renderSprites(SpriteBatch batch) //TODO: Make tool tip display better
    {
        super.renderSprites(batch);
        
        if (this.visible)
        {
            //The mouse coordinates
            float mouseX = Gdx.input.getX(0);
            float mouseY = Gdx.input.getY(0);
            
            float textHeight = GameAssets.fontMain.getCapHeight();
            float textWidth = GameAssets.fontMain.getSpaceWidth()*this.text.length();
            
            float boxX = mouseX + 0.05F*Gdx.graphics.getWidth();
            float boxY = Gdx.graphics.getHeight() - mouseY + (textHeight/2);
            
            //Swap the tooltip to the other side if it's on the other side of the screen
            if (mouseX > Gdx.graphics.getWidth()/2)
                boxX = mouseX - 0.05F*Gdx.graphics.getWidth() - textWidth;
            
            GameAssets.fontMain.draw(batch, this.text, boxX, boxY, 1.0F, -1, false);
        }
    }
}
