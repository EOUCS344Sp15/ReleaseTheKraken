/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import releasethekraken.GameAssets;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.ui.tooltip.PowerUpToolTip;

/**
 * Represents a power up UI button.  Includes a counter to display how many
 * power ups you currently have.
 * @author Dalton
 */
public class PowerUpUiButton extends UiButton
{
    /** A local copy of how many power ups are stored */
    private int powerUpCount;
    /** The type of power up that the button is for */
    private EntityPowerUp.Ability powerUpType;
    /** The power up stats reference */
    private EntityPowerUp.PowerUpStats powerUpStats;
    /** A local reference to the player */
    private EntityPlayer player = null;
    
    /**
     * Constructs a new PowerUpUiButton.
     * @param renderer The GameRenderer reference
     * @param x The X coordinate, in pixels
     * @param y Tye Y coordinate, in pixels
     * @param width The width, in percentage of the screen width
     * @param height The height, in percentage of the screen height
     * @param powerUpType The type of power up the button is for
     */
    public PowerUpUiButton(GameRenderer renderer, float x, float y, float width, float height, EntityPowerUp.Ability powerUpType)
    {
        super(renderer, x, y, width, height, "Power\nUp", Color.PURPLE.cpy().sub(0.1F, 0.1F, 0.1F, 0));
        this.powerUpType = powerUpType;
        this.powerUpStats = EntityPowerUp.getStats(powerUpType);
        
        this.text = this.powerUpStats.name;
        this.setToolTip(new PowerUpToolTip(renderer, powerUpType));
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        //Draw the amount of power ups
        GameAssets.fontMain.draw(batch,
                this.powerUpCount + "",
                this.x,
                this.y + 0.22F*this.height,
                this.width - 0.02F*this.width,
                Align.right,
                false);
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        //Update the player reference
        this.player = this.world.getPlayer();
        
        //Update the local power up count
        this.powerUpCount = this.world.getPowerUps(this.powerUpType);
        
        //Disable the button if there aren't any power ups to use, or enable it if there are
        this.setDisabled(this.powerUpCount < 1);
    }
    
    @Override
    public void onClick(int mouseButton)
    {
        super.onClick(mouseButton);
        
        //Use the power up
        if (this.state != ButtonState.DISABLED && this.world.getPowerUps(this.powerUpType) > 0)
        {
            this.world.addPowerUps(this.powerUpType, -1); //Subtract 1 from the power up count
            EntityPowerUp.onUse(this.world, this.powerUpType); //Use the power up
        }
    }
    
    @Override
    public void onHover(float x, float y)
    {
        super.onHover(x, y);
        
        if (this.player != null && this.state != ButtonState.DISABLED) //Make the player render the preview
            this.player.powerUpPreview = this.powerUpType;
        else if (this.player != null) //Make the player not render it if the button is disabled
            this.player.powerUpPreview = null;
    }
    
    @Override
    public void onLeaveHover()
    {
        super.onLeaveHover();
        
        if (this.player != null) //Make the player no longer render the preview
            this.player.powerUpPreview = null;
    }
}
