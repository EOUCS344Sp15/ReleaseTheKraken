/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import releasethekraken.GameRenderer;
import releasethekraken.ui.tooltip.TextToolTip;

/**
 * Represents the side bar for the game
 * @author Dalton
 */
public class Sidebar extends UiObject
{
    private Array<UiButton> unitButtons;
    private Array<UiButton> powerupButtons;
    private UiButton krakenButton;
    
    private Color color;
    
    //Constructor
    public Sidebar(GameRenderer renderer)
    {
        super(renderer, 0, 0, 0.2F, 1.0F);
        
        this.color = Color.valueOf("3173DE");
        this.depth = 1; //Set the render depth so that it renders under its contents
        
        //Create arrays of children UI objects
        this.unitButtons = new Array<UiButton>();
        this.powerupButtons = new Array<UiButton>();
        
        //Variables to control button position and size.  Some will be changed in the for loops
        float buttonX;
        float buttonY;
        float xOffset;
        float yOffset;
        final float buttonWidth = 0.09F;
        final float buttonHeight = 0.12F;
        final int scrWidth = Gdx.graphics.getWidth();
        final int scrHeight = Gdx.graphics.getHeight();
        
        for (int i=0; i<4; i++) //Temporary, until we get actual units to represent
        {
            //Calculate X and Y offsets for the buttons based on the counter variable i
            xOffset = (i % 2 == 1 ? 0.098F*scrWidth : 0);
            yOffset = (i > 1 ? 0.13F*scrHeight : 0);
            
            //Set the button's position
            buttonX = 0.006F*scrWidth + xOffset;
            buttonY = 0.75F*scrHeight - yOffset;
            
            this.unitButtons.add(new UiButton(renderer, buttonX, buttonY, buttonWidth, 
                    buttonHeight, "Unit " + (i + 1)));
        }
        
        for (int i=0; i<4; i++) //Temporary, until we get actual powerups to represent
        {
            //Calculate X and Y offsets for the buttons based on the counter variable i
            xOffset = (i % 2 == 1 ? 0.098F*scrWidth : 0);
            yOffset = (i > 1 ? 0.13F*scrHeight : 0);
            
            //Set the button's position
            buttonX = 0.006F*scrWidth + xOffset;
            buttonY = 0.4F*scrHeight - yOffset;
            
            this.powerupButtons.add(new UiButton(renderer, buttonX, buttonY, buttonWidth, 
                    buttonHeight, "Power\nUp " + (i + 1)));
        }
        
        //Create the Release the Kraken button
        this.krakenButton = new UiButton(renderer, 0.0F, 0.0F, 0.2F, 0.15F, "RELEASE\nTHE KRAKEN");
        this.krakenButton.setToolTip(new TextToolTip(renderer, "Test ToolTip")); //Just for testing tooltips
        
        //Add the UI objects to the global list
        for (UiButton button : this.unitButtons)
            renderer.uiObjects.add(button);
        for (UiButton button : this.powerupButtons)
            renderer.uiObjects.add(button);
        renderer.uiObjects.add(this.krakenButton);
            
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(this.color);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
    }
}
