/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import releasethekraken.GameWorld;
import releasethekraken.InputHandler;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.ui.renderer.UiRenderer;
import releasethekraken.ui.tooltip.ToolTip;

/**
 * This is a normal UiObject that is designed for user interaction.  For example,
 * UiButton extends this.
 * @author Dalton
 */
public class InteractiveUiObject extends UiObject implements InputHandler.TouchListener
{
    protected boolean hoverActive = false; //True if a pointer is hovering over this
    protected ToolTip toolTip = null; //The UI object's tooltip.  Can be null.
    
    //Constructor
    public InteractiveUiObject(UiRenderer renderer)
    {
        this(renderer, 0.0F, 0.0F, 0.0F, 0.0F);
    }
    
    //Constructor
    public InteractiveUiObject(UiRenderer renderer, GameWorld world, float x, float y)
    {
        this(renderer, x, y, 0.0F, 0.0F);
    }
    
    //Constructor
    public InteractiveUiObject(UiRenderer renderer, float x, float y, float width, float height)
    {
        super(renderer, x, y, width, height);
        ReleaseTheKraken.inputHandler.registerTouchListener(this); //Register this object as a touch listener
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        Vector2 pointer0 = ReleaseTheKraken.inputHandler.getPointerLocations().get(0);
        
        //Call onHover() if the mouse is in the bounds of the object
        if (this.isInBounds(pointer0.x, pointer0.y))
        {
            this.onHover(pointer0.x, pointer0.y);
        }
        else if (this.isHoverActive()) //If the object was still being hovered over, call onLeaveHover()
        {
            this.onLeaveHover();
        } 
    }
    
    /**
     * Called when the object is clicked
     * 
     * @param mouseButton The mouse button that was clicked.  TODO: which button?
     */
    public void onClick(int mouseButton)
    {
        
    }
    
    /**
     * Called when the object is clicked and held
     * 
     * @param mouseButton The mouse button that was clicked.  TODO: which button?
     */
    public void onClickHeld(int mouseButton)
    {
        
    }
    
    /**
     * Called when the object is no longer being clicked.
     */
    public void onStoppedClicking()
    {
        
    }
    
    /**
     * Called when the mouse is hovering over the object
     * 
     * @param x The x coordinate of the mouse
     * @param y The y coordinate of the mouse
     */
    public void onHover(float x, float y)
    {
        this.hoverActive = true;
        if (this.toolTip != null)
            this.toolTip.setVisible(true);
    }
    
    /**
     * Called when the mouse leaves the object
     */
    public void onLeaveHover()
    {
        this.hoverActive = false;
        if (this.toolTip != null)
            this.toolTip.setVisible(false);
    }
    
    /**
     * Returns whether the coordinates are within the object's bounds.
     * @param clickX The X position clicked/touched
     * @param clickY The Y position clicked/touched
     * @return Whether the position is within the button
     */
    public boolean isInBounds(float clickX, float clickY)
    {
        clickY = Gdx.graphics.getHeight() - clickY;

        return clickX >= this.x && clickX <= this.x + this.width
            && clickY >= this.y && clickY <= this.y + this.height;
    }
    
    /**
     * @return True if this object is being hovered over
     */
    public boolean isHoverActive()
    {
        return this.hoverActive;
    }
    
    /**
     * Sets the Interactive UI Object's ToolTip
     * @param toolTip The new ToolTip
     */
    public void setToolTip(ToolTip toolTip)
    {
        if (this.toolTip != null)
        {
            this.toolTip.dispose();
            this.renderer.uiObjects.removeValue(this.toolTip, true); //Remove tooltip from the list
        }
        
        this.toolTip = toolTip;
        if (!this.renderer.uiObjects.contains(toolTip, true))
            this.renderer.uiObjects.add(toolTip); //Add tooltip to the list
    }

    //TouchListener interface methods
    
    @Override
    public void touchDown(int x, int y, int pointer, int button)
    {
        if (this.isInBounds(x, y)) //If the click was within the button's bounds, call onClick()
            this.onClick(button);
    }

    @Override
    public void touchUp(int x, int y, int pointer, int button) {}

    @Override
    public void touchHeld(int x, int y, int pointer, int button)
    {
        if (this.isInBounds(x, y)) //If the click was within the button's bounds, call onClick()
            this.onClickHeld(button);
    }
}
