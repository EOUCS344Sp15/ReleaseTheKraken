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
import releasethekraken.entity.seacreature.EntitySeaCreature;
import releasethekraken.ui.renderer.GameRenderer;

/**
 * 
 * @author Dalton
 */
public class PurchaseUnitToolTip extends ToolTip
{
    /** The type of sea creature being shown */
    private Class<? extends EntitySeaCreature> seaCreature;
    /** The stats for the sea creature */
    private EntitySeaCreature.SeaCreatureStats seaCreatureStats;
    /** The color of the tool tip */
    protected Color color;
    /** The height of the description, in pixels */
    private float descriptionHeight;
    
    /**
     * Constructs a new purchase unit tool tip.
     * @param renderer The GameRenderer reference
     * @param seaCreature The type of sea creature being shown
     */
    public PurchaseUnitToolTip(GameRenderer renderer, Class<? extends EntitySeaCreature> seaCreature)
    {
        super(renderer);
        
        this.seaCreature = seaCreature;
        this.seaCreatureStats = EntitySeaCreature.getStats(seaCreature);
        this.color = Color.valueOf("234399");
        
        //Get the height of the description, in pixels
	this.descriptionHeight = GameAssets.fontMain.getCache().addText(
                "Information:\n" + this.seaCreatureStats.description,
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
            float boxHeight = this.descriptionHeight + GameAssets.fontMain.getCapHeight() + 0.16F*Gdx.graphics.getHeight();
            
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
            
            //Draw the description, saving the height for the next render
            this.descriptionHeight = GameAssets.fontMain.draw(
                    batch,
                    "[CYAN]Information:[WHITE]\n" + this.seaCreatureStats.description,
                    boxX,
                    boxY + 0.16F*Gdx.graphics.getHeight()/2,
                    0.25F*Gdx.graphics.getWidth(),
                    -1,
                    true).height;
            
            //Draw the health text
            GameAssets.fontMain.draw(batch,
                    String.format("%-3d[CYAN]health[WHITE]", this.seaCreatureStats.health),
                    boxX + 0.03F*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight() - mouseY + 0.005F*Gdx.graphics.getHeight(),
                    0.25F*Gdx.graphics.getWidth(),
                    -1,
                    false);
            
            //Draw the strength text
            GameAssets.fontMain.draw(batch,
                    String.format("%-3d[CYAN]strength[WHITE]", this.seaCreatureStats.strength),
                    boxX + 0.03F*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight() - mouseY - 0.03F*Gdx.graphics.getHeight(),
                    0.25F*Gdx.graphics.getWidth(),
                    -1,
                    false);
            
            //Draw the price text
            GameAssets.fontMain.draw(batch,
                    String.format("%-3d[CYAN]coins[WHITE]", this.seaCreatureStats.cost),
                    boxX + 0.03F*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight() - mouseY - 0.065F*Gdx.graphics.getHeight(),
                    0.25F*Gdx.graphics.getWidth(),
                    -1,
                    false);
            
            //Draw the build time text
            GameAssets.fontMain.draw(batch,
                    String.format("%-3.0f[CYAN]seconds[WHITE]", this.seaCreatureStats.buildTime/60F),
                    boxX + 0.03F*Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight() - mouseY - 0.1F*Gdx.graphics.getHeight(),
                    0.25F*Gdx.graphics.getWidth(),
                    -1,
                    false);
            
            //Draw the clock symbol
            batch.draw(GameAssets.heartTexture,
                    boxX,
                    Gdx.graphics.getHeight() - mouseY - 0.02F*Gdx.graphics.getHeight(),
                    0.018F*Gdx.graphics.getWidth(),
                    0.018F*Gdx.graphics.getWidth());
            
            //Draw the clock symbol
            batch.draw(GameAssets.strengthTexture,
                    boxX,
                    Gdx.graphics.getHeight() - mouseY - 0.055F*Gdx.graphics.getHeight(),
                    0.018F*Gdx.graphics.getWidth(),
                    0.018F*Gdx.graphics.getWidth());
            
            //Draw the clock symbol
            batch.draw(GameAssets.coinTexture,
                    boxX,
                    Gdx.graphics.getHeight() - mouseY - 0.090F*Gdx.graphics.getHeight(),
                    0.018F*Gdx.graphics.getWidth(),
                    0.018F*Gdx.graphics.getWidth());
            
            //Draw the clock symbol
            batch.draw(GameAssets.clockTexture,
                    boxX,
                    Gdx.graphics.getHeight() - mouseY - 0.125F*Gdx.graphics.getHeight(),
                    0.018F*Gdx.graphics.getWidth(),
                    0.018F*Gdx.graphics.getWidth());
        }
    }
}
