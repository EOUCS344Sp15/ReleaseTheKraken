/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import releasethekraken.ui.renderer.GameRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import releasethekraken.GameAssets;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import releasethekraken.ui.tooltip.PurchaseUnitToolTip;

/**
 * Represents a sea creature unit UI button that lets you purchase sea creatures.
 * @author Dalton
 */
public class PurchaseUnitUiButton extends UiButton
{
    /** A local copy of how much the unit costs */
    private int cost;
    /** The sea creature that the button sells */
    private final Class<? extends EntitySeaCreature> seaCreature;
    /** The sea creature stats reference */
    private EntitySeaCreature.SeaCreatureStats seaCreatureStats;

    /**
     * Constructs a new PurchaseUnitUiButton
     * @param renderer The GameRenderer reference
     * @param x the X coordinate, in pixels
     * @param y the Y coordinate, in pixels
     * @param width The width, in percentage of the screen width
     * @param height The height, in percentage of the screen height
     * @param seaCreature The sea creature that the button sells
     */
    public PurchaseUnitUiButton(GameRenderer renderer, float x, float y, float width, float height, Class<? extends EntitySeaCreature> seaCreature)
    {
        super(renderer, x, y, width, height, "Unit", Color.BLUE.cpy().sub(0.1F, 0.1F, 0.1F, 0));
        this.seaCreature = seaCreature;
        this.seaCreatureStats = EntitySeaCreature.getStats(seaCreature);
        this.cost = this.seaCreatureStats.cost;
        
        this.text = this.seaCreatureStats.name;
        this.setToolTip(new PurchaseUnitToolTip(renderer, seaCreature));
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        super.renderSprites(batch);
        
        //Draw the cost of the sea creature
        GameAssets.fontMain.draw(batch,
                "" + this.cost,
                this.x + 0.2F*this.width,
                this.y + 0.22F*this.height,
                this.width,
                -1,
                false);
        
        //Draw a coin
        batch.draw(GameAssets.coinTexture,
                this.x + 0.035F*this.width,
                this.y + 0.05F*this.height,
                0.15F*this.width,
                0.15F*this.width);
    }
    
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        //Disable the button if the unit can't be afforded, or enable it if it can be
        this.setDisabled(this.world.getCoins() < this.seaCreatureStats.cost);
    }
    
    @Override
    public void onClick(int mouseButton)
    {
        super.onClick(mouseButton);
        
        //Purchase the unit
        if (this.state != ButtonState.DISABLED && this.world.getCoins() >= this.seaCreatureStats.cost)
        {
            this.world.removeCoins(this.seaCreatureStats.cost); //Remove how many coins it costs
            
            //TODO: spawn the sea creature in x seconds
        }
    }
}
