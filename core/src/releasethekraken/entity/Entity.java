/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.ui.Renderable;
/**
 * The skeleton class for all entities.
 * 
 * changelog:
 * 
 * --5/9/15--
 * -added variables
 *  -xLoc - X Location
 *  -yLoc - Y Location
 *  -sprite - Sprite for the entity to display
 * 
 * -added methods
 *  -render() - used to draw the sprite
 *  -update() - used to update (move, add/remove health/stats)
 *  -dispose() - overriden from the Disposable class
 * 
 * --5/10/15--
 * -removed methods
 *  -render() - removed and replaced by the following added methods
 * 
 * -added methods
 *  -renderShapes(ShapeRenderer)
 *  -renderSprite(SpriteBatch)
 * 
 * -implemented Renderable
 * 
 * @author Lucas Schuetz
 */
public class Entity implements Disposable, Renderable
{
    protected int xLoc;
    protected int yLoc;
    protected Sprite sprite;
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        //render shapes
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        //render sprites
    }

    /**
     * Updates the Entity object
     */
    public void update()
    {
        //update the entity
    }
    
    @Override
    public void dispose()
    {
        //dispose of the entity
    }
}
