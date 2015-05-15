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
    protected Color baseColor;
    protected final Color[][] defaultColors; //Index 0 is background, 1 is triangular highlight, 2 is foreground.  First array is normal colors, second is pressed colors.
    private int currentColorIndex; //The index of the current button color
    
    /**
     * Constructs a new UI button.
     * @param renderer The GameRenderer instance
     * @param x The X coordinate, in pixels, on the screen
     * @param y The Y coordinate, in pixels, on the screen
     * @param width The width of the button, in percentage of screen width
     * @param height The height of the button, in percentage of screen height
     * @param text The text displayed on the button
     * @param color The color of the button.  <strong>IMPORTANT:</strong> The button won't display properly if the color is highly saturated!
     */
    public UiButton(GameRenderer renderer, float x, float y, float width, float height, String text, Color color)
    {
        super(renderer, x, y, width, height);
        this.text = text;
        this.lastClickTime = 0L;
        this.state = ButtonState.UNPRESSED;
        this.currentColorIndex = 0;
        this.baseColor = color;
        
        //Calculate colors from the base color
        this.defaultColors = new Color[3][4];
        
        //Normal Colors
        this.defaultColors[0][0] = this.baseColor.cpy().sub(0.2F, 0.2F, 0.2F, 0); //Bottom & Right
        this.defaultColors[1][0] = this.baseColor.cpy().add(0.2F, 0.2F, 0.2F, 0); //Top & Left
        this.defaultColors[2][0] = this.baseColor;                                //Front
        
        //Clicked Colors
        this.defaultColors[0][1] = this.defaultColors[1][0].cpy();
        this.defaultColors[1][1] = this.defaultColors[0][0].cpy();
        this.defaultColors[2][1] = this.defaultColors[2][0].cpy().sub(0.05F, 0.05F, 0.05F, 0);
        
        //Hover Colors
        this.defaultColors[0][2] = this.defaultColors[0][0].cpy().add(0.07F, 0.07F, 0.07F, 0);
        this.defaultColors[1][2] = this.defaultColors[1][0].cpy().add(0.07F, 0.07F, 0.07F, 0);
        this.defaultColors[2][2] = this.defaultColors[2][0].cpy().add(0.07F, 0.07F, 0.07F, 0);
        
        //Disabled Colors
        this.defaultColors[0][3] = this.defaultColors[0][0].cpy().sub(0.3F, 0.3F, 0.3F, 0);
        this.defaultColors[1][3] = this.defaultColors[1][0].cpy().sub(0.3F, 0.3F, 0.3F, 0);
        this.defaultColors[2][3] = this.defaultColors[2][0].cpy().sub(0.3F, 0.3F, 0.3F, 0);
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        super.renderShapes(shapeRenderer);
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
        super.renderSprites(batch);
        int lines = this.text.split("\n").length; //Count the number of lines
        
        GameAssets.fontMain.draw(batch, this.text,
                this.x,
                this.y + this.height/2 + GameAssets.fontMain.getCapHeight()*lines/2,
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
        
        //Override tooltip displaying so that it only displays if the button is active
        if (this.toolTip != null)
            if (this.state != ButtonState.DISABLED)
                this.toolTip.setVisible(true);
            else
                this.toolTip.setVisible(false);
        
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
