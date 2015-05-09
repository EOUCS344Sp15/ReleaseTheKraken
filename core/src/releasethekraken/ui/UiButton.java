/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;

/**
 *
 * @author Dalton
 */
public class UiButton extends UiObject
{
    protected String text; //The button's text
    protected long lastClickTime; //The time when the button was last clicked   
    protected ButtonState state;
    protected final Color[][] defaultColors; //Index 0 is background, 1 is triangular highlight, 2 is foreground.  First array is normal colors, second is pressed colors.
    private int currentColorIndex; //The index of the current button color
    
    //Constructor
    public UiButton(float x, float y, float width, float height, String text)
    {
        super(x, y, width, height);
        this.text = text;
        this.lastClickTime = 0L;
        this.state = ButtonState.UNPRESSED;
        this.currentColorIndex = 0;
        
        this.defaultColors = new Color[][]
        {
            {Color.valueOf("333333"), Color.valueOf("444444"), Color.valueOf("111111")},
            {Color.valueOf("999999"), Color.valueOf("AAAAAA"), Color.valueOf("777777")},
            {Color.valueOf("666666"), Color.valueOf("777777"), Color.valueOf("444444")},
            {Color.valueOf("AAAAAA"), Color.valueOf("BBBBBB"), Color.valueOf("888888")}
        };
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        //Code taken from Dalton's App        
        float borderWidth = 0.0025F*Gdx.graphics.getWidth();

        shapeRenderer.setColor(this.defaultColors[0][this.currentColorIndex]);
        shapeRenderer.rect(this.x, this.y, this.width, this.height); //Background
        shapeRenderer.setColor(this.defaultColors[1][this.currentColorIndex]);
        shapeRenderer.rect(this.x, this.y + this.height/2, this.width - this.height/2, this.height/2); //Render middle rectangle
        shapeRenderer.triangle(this.x + this.width, this.y + this.height, this.x + this.width - this.height/2, this.y + this.height/2, this.x + this.width - this.height/2, this.y + this.height); //render middle right triangle
        shapeRenderer.triangle(this.x, this.y, this.x, this.y + this.height/2, this.x + this.height/2, this.y + this.height/2); //render middle left triangle
        shapeRenderer.setColor(this.defaultColors[2][this.currentColorIndex]);
        shapeRenderer.rect(this.x + borderWidth, this.y + borderWidth, this.width - borderWidth * 2, this.height - borderWidth * 2); //Inner box
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        GameAssets.fontMain.draw(batch, this.text,
                this.x,
                this.y + this.height/2 + GameAssets.fontMain.getCapHeight()/2,
                this.width,
                1,
                false); //Render text
    }
    
    @Override
    public void update(GameWorld world)
    {
        if (this.state == ButtonState.PRESSED && world.getWorldTime() - this.lastClickTime > 5) //Unpress the button
        {
            this.state = ButtonState.UNPRESSED;
            this.onStoppedClicking(world);
        }
    }
    
    /**
     * Called when the button is clicked
     * 
     * @param mouseButton The mouse button that was clicked.  TODO: which button?
     * @param world The game world
     */
    public void onClick(int mouseButton, GameWorld world)
    {
        if (this.state != ButtonState.DISABLED)
        {
            this.lastClickTime = world.getWorldTime();
            this.state = ButtonState.PRESSED;
            this.currentColorIndex = 1; //Change colors to pressed colors
        }
    }
    
    /**
     * Called when the button is no longer being clicked.
     * @param world The game world
     */
    public void onStoppedClicking(GameWorld world)
    {
        if (this.state != ButtonState.DISABLED)
        {
            this.currentColorIndex = 0; //Reset colors back to normal
        }
    }
    
    /**
     * Called when the mouse is hovering over the button
     * 
     * @param x The x coordinate of the mouse
     * @param y The y coordinate of the mouse
     */
    public void onHover(float x, float y)
    {
        if (this.state != ButtonState.DISABLED && this.state != ButtonState.PRESSED)
        {
            this.currentColorIndex = 2;
        }
    }
    
    /**
     * Called when the mouse leaves the button
     */
    public void onLeaveHover()
    {
        if (this.state != ButtonState.DISABLED)
        {
            this.currentColorIndex = 0;
        }
    }
    
    /**
     * Returns whether the coordinates are within the button's bounds.
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
    
    @Override
    public String toString()
    {
        return super.toString() + " Text: " + this.text;
    }

    /**
     * Sets if the button is disabled.
     * @param disabled
     */
    public void setDisabled(boolean disabled)
    {
        if (disabled && this.state != ButtonState.DISABLED)
        {
            this.state = ButtonState.DISABLED;
            this.currentColorIndex = 3;
        }
        else if (!disabled && this.state == ButtonState.DISABLED)
        {
            this.state = ButtonState.UNPRESSED;
            this.currentColorIndex = 0;
        }
    }
}
