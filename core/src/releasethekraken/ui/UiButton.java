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
 * Represents a UI button.  Most of the code was taken from Dalton's CS 310 Android App
 * @author Dalton
 */
public class UiButton extends InteractiveUiObject
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
            /*Default Color           Clicked/Held Color       Hover Color              Disabled Color   */
            {Color.valueOf("333333"), Color.valueOf("4C4C4C"), Color.valueOf("404040"), Color.valueOf("111111")}, //Bottom
            {Color.valueOf("999999"), Color.valueOf("B1B1B1"), Color.valueOf("A0A0A0"), Color.valueOf("777777")}, //Front
            {Color.valueOf("666666"), Color.valueOf("7D7D7D"), Color.valueOf("707070"), Color.valueOf("444444")}  //Top
        };
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {     
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
        super.update(world);
        
        if (this.state == ButtonState.PRESSED && world.getWorldTime() - this.lastClickTime > 5) //Unpress the button
        {
            this.state = ButtonState.UNPRESSED;
            this.onStoppedClicking(world);
        }
    }
    
    @Override
    public void onClick(int mouseButton, GameWorld world)
    {
        super.onClick(mouseButton, world);
        
        if (this.state != ButtonState.DISABLED)
        {
            this.lastClickTime = world.getWorldTime();
            this.state = ButtonState.PRESSED;
            this.currentColorIndex = 1; //Change colors to pressed colors
        }
    }
    
    @Override
    public void onClickHeld(int mouseButton, GameWorld world)
    {
        super.onClickHeld(mouseButton, world);
        //Gdx.app.log("UiButton", "onClickHeld() called!");
        
        if (this.state != ButtonState.DISABLED)
        {
            this.lastClickTime = world.getWorldTime();
            this.state = ButtonState.PRESSED;
            this.currentColorIndex = 1; //Change colors to pressed colors
        }
    }
    
    @Override
    public void onStoppedClicking(GameWorld world)
    {
        super.onStoppedClicking(world);
        
        if (this.state != ButtonState.DISABLED)
        {
            this.currentColorIndex = 0; //Reset colors back to normal
        }
    }
    
    @Override
    public void onHover(float x, float y)
    {
        super.onHover(x, y);
        
        //Gdx.app.log("UiButton", "onHover() called!");
        
        if (this.state != ButtonState.DISABLED && this.state != ButtonState.PRESSED)
        {
            this.currentColorIndex = 2;
        }
    }
    
    @Override
    public void onLeaveHover()
    {
        super.onLeaveHover();
        
        if (this.state != ButtonState.DISABLED)
        {
            this.currentColorIndex = 0;
        }
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
