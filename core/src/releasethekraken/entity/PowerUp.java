/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import releasethekraken.GameWorld;

/**
 *
 * @author tarstarkes
 */
public class PowerUp extends Entity
{
    protected String description; //detailed description of the power up
    protected Ability type; //tracks the type of power up
    protected long despawnTimer; //amount of time before powerUp despawns
    
            
    public PowerUp(GameWorld world, float xLocation, float yLocation, Ability ptype, int seconds)
    {
        super(world, xLocation, yLocation);
        this.type = ptype;
        this.despawnTimer = seconds*60;
    }
    public PowerUp(GameWorld world, RectangleMapObject mapObject)
    {
        super(world, mapObject);
    }
    public static enum Ability
    {
        SPEEDUP, ATTACKUP, HEALUP
    }
    
    @Override
    public void update()
    {
        super.update();
        despawnTimer--;
        if (despawnTimer <= 0)
        {
            this.dispose();
        }
    }
    
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
}
