/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui.tooltip;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import releasethekraken.ui.UiObject;
import releasethekraken.ui.renderer.UiRenderer;

/**
 * Represents a tool tip that can be shown by an InteractiveUiObject.
 * @author Dalton
 */
public class ToolTip extends UiObject
{    
    protected boolean visible;
    
    //Constructor
    public ToolTip(UiRenderer renderer)
    {
        this(renderer, 0.1F, 0.1F);
    }
    
    //Constructor
    public ToolTip(UiRenderer renderer, float widthPercent, float heightPercent)
    {
        super(renderer, 0, 0, widthPercent, heightPercent);
        this.visible = false;
        this.depth = -10; //Tooltips should be above everything else
    }

    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
    }

    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    /**
     * @return True if this ToolTip should be visible
     */
    public boolean isVisible()
    {
        return this.visible;
    }
    
    /**
     * Sets if the ToolTip is visible or not
     * @param visible If it is visible or not
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
