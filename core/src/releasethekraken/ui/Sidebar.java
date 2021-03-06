/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import releasethekraken.ui.renderer.GameRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.seacreature.EntityFish;
import releasethekraken.entity.seacreature.EntityOrca;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import releasethekraken.entity.seacreature.EntityShark;
import releasethekraken.entity.seacreature.EntityTurtle;
import releasethekraken.entity.seacreature.kraken.EntityKraken;
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
    private Color borderColor;
    
    //Constructor
    public Sidebar(GameRenderer renderer)
    {
        super(renderer, 0, 0, 0.2F, 1.0F);
        
        this.color = Color.valueOf("3173DE");
        this.borderColor = this.color.cpy().sub(0.1F, 0.1F, 0.1F, 0);
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
        
        //Array of purchasable units.  TODO: Generate this instead
        Class[] purchaseableUnits =
        {
            EntityFish.class,
            EntityTurtle.class,
            EntityOrca.class,
            EntityShark.class
        };
        
        
        for (int i=0; i<4; i++) //Temporary, until we get actual units to represent
        {
            //Calculate X and Y offsets for the buttons based on the counter variable i
            xOffset = (i % 2 == 1 ? 0.098F*scrWidth : 0);
            yOffset = (i > 1 ? 0.13F*scrHeight : 0);
            
            //Set the button's position
            buttonX = 0.006F*scrWidth + xOffset;
            buttonY = 0.75F*scrHeight - yOffset;
            
            this.unitButtons.add(new PurchaseUnitUiButton(renderer, buttonX, buttonY, buttonWidth, 
                    buttonHeight, purchaseableUnits[i]));
        }
        
        for (int i=0; i<4; i++)
        {
            //Calculate X and Y offsets for the buttons based on the counter variable i
            xOffset = (i % 2 == 1 ? 0.098F*scrWidth : 0);
            yOffset = (i > 1 ? 0.13F*scrHeight : 0);
            
            //Set the button's position
            buttonX = 0.006F*scrWidth + xOffset;
            buttonY = 0.4F*scrHeight - yOffset;
            
            this.powerupButtons.add(new PowerUpUiButton(renderer, buttonX, buttonY, buttonWidth, 
                    buttonHeight, EntityPowerUp.Ability.values()[i]));
        }
        
        //Create the Release the Kraken button
        this.krakenButton = new UiButton(renderer, 0.0F, 0.0F, 0.2F, 0.15F, "RELEASE\nTHE KRAKEN", Color.GREEN.cpy().sub(0.5F, 0.5F, 0.5F, 0))
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);

                        if (this.world.getPoints() >= this.world.getPointsForKraken())
                        {
                            Vector2 spawnPos = new Vector2();
                            this.world.getFirstPath().getSmoothPath().valueAt(spawnPos, 0.02F);
                            
                            Gdx.app.log(this.getClass().getName(), "Attempting to RELEASE THE KRAKEN!");
                            
                            new EntityKraken(this.world, spawnPos.x, spawnPos.y);
                            this.world.addPoints(0 - this.world.getPointsForKraken()); //Subtract points
                        }
                    }
                };
        this.krakenButton.setToolTip(new TextToolTip(renderer, "Click to RELEASE THE KRAKEN!")); //Just for testing tooltips
        //this.krakenButton.setDisabled(true);
        
        //Add the UI objects to the global list
        for (UiButton button : this.unitButtons)
            renderer.uiObjects.add(button);
        for (UiButton button : this.powerupButtons)
            renderer.uiObjects.add(button);
        renderer.uiObjects.add(this.krakenButton);
        
        renderer.uiObjects.add(new UiText(
                renderer,
                0.1F*scrWidth,
                0.9F*scrHeight,
                0.2F*scrWidth,
                0.1F*scrHeight,
                "Purchase Units"));
        
        renderer.uiObjects.add(new UiText(
                renderer,
                0.1F*scrWidth,
                0.55F*scrHeight,
                0.2F*scrWidth,
                0.1F*scrHeight,
                "Use Power Ups"));
        
        renderer.uiObjects.add(new UiText(
                renderer,
                0.06F*scrWidth,
                0.23F*scrHeight,
                0.2F*scrWidth,
                0.1F*scrHeight,
                "Points"));
        
        renderer.uiObjects.add(new UiPointsBar(
                renderer,
                0.006F*scrWidth,
                0.16F*scrHeight,
                0.2F - 0.006F*2,
                0.04F));
        
        renderer.uiObjects.add(new CoinsDisplay(renderer,
                0.006F*scrWidth,
                0.92F*scrHeight,
                0.2F - 0.006F*2,
                0.06F));
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        //Update the disabled status on the Kraken button
        this.krakenButton.setDisabled(this.world.getPoints() < this.world.getPointsForKraken());
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer, float delta, float runTime)
    {
        shapeRenderer.setColor(this.color);
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
        
        float borderWidth = 0.0025F*Gdx.graphics.getWidth();
        
        shapeRenderer.setColor(this.borderColor);
        shapeRenderer.rect(this.x + this.width - borderWidth,
                this.y,
                borderWidth,
                this.height);
    }
}
