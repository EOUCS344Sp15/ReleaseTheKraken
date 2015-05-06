/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * This class renders the Game World.
 * 
 * @author Dalton
 */
public class GameRenderer implements Disposable
{
    //The Game Renderer's reference to the game world it should be rendering
    private GameWorld world;
    
    private SpriteBatch uiSpriteBatch; //SpriteBatch to render UI sprites
    private SpriteBatch worldSpriteBatch; //SpriteBatch to render world sprites
    private ShapeRenderer uiShapeRenderer; //ShapeRenderer to render UI shapes
    private ShapeRenderer worldShapeRenderer; //ShapeRenderer to render world shapes
    
    //Constructor
    public GameRenderer(GameWorld world)
    {
        this.world = world;
        this.uiSpriteBatch = new SpriteBatch();
        this.worldSpriteBatch = new SpriteBatch();
        this.uiShapeRenderer = new ShapeRenderer();
        this.worldShapeRenderer = new ShapeRenderer();
    }
    
    /**
     * Renders the game world
     */
    public void render()
    {
        //Clears screen buffer
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Draws LibGDX logo
        this.uiSpriteBatch.begin();
        this.uiSpriteBatch.draw(ReleaseTheKraken.img, 0, 0);
        this.uiSpriteBatch.end();
    }
    
    @Override
    public void dispose()
    {
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.uiSpriteBatch.dispose();
        this.worldSpriteBatch.dispose();
        this.uiShapeRenderer.dispose();
        this.worldShapeRenderer.dispose();
    }
}
