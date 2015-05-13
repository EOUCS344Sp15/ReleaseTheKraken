/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.entity.Entity;
import releasethekraken.ui.tooltip.TextToolTip;

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
    
    public Array<UiObject> uiObjects; //The array of UiObjects
    
    //Whether the debug screen is visible or not
    public boolean debugScreenVisible = false;
    private DebugOverlay debugOverlay;
    
    //Constructor
    public GameRenderer(GameWorld world)
    {
        this.world = world;
        this.uiSpriteBatch = new SpriteBatch();
        this.worldSpriteBatch = new SpriteBatch();
        this.uiShapeRenderer = new ShapeRenderer();
        this.worldShapeRenderer = new ShapeRenderer();
        
        this.uiObjects = new Array<UiObject>();
        
        this.debugOverlay = new DebugOverlay(this);
        this.uiObjects.add(this.debugOverlay);
              
        UiButton pauseButton = new UiButton(
                this,
                Gdx.graphics.getWidth()-0.075F*Gdx.graphics.getWidth(), 
                Gdx.graphics.getHeight()-0.05F*Gdx.graphics.getHeight(), 
                0.075F, 0.05F, "Pause")
                {
                    @Override
                    public void onClick(int mouseButton, GameWorld world)
                    {
                        super.onClick(mouseButton, world);
                        Gdx.app.log("Pause Button", "onClick() called!");
                    }
                };
        pauseButton.setToolTip(new TextToolTip(this, "Pause the game"));
        
        this.uiObjects.add(pauseButton);
        
        UiButton debugMenuButton = new UiButton(
                this,
                Gdx.graphics.getWidth()-0.2323F*Gdx.graphics.getWidth(), 
                Gdx.graphics.getHeight()-0.05F*Gdx.graphics.getHeight(), 
                0.157F, 0.05F, "Debug Screen")
                {
                    @Override
                    public void onClick(int mouseButton, GameWorld world)
                    {
                        super.onClick(mouseButton, world);
                        this.renderer.debugScreenVisible = !this.renderer.debugScreenVisible; //Toggle visibility
                        Gdx.app.log("Debug Menu Button", "Debug screen " + (this.renderer.debugScreenVisible ? "ON" : "OFF"));
                    }
                };
        debugMenuButton.setToolTip(new TextToolTip(this, "Show/Hide Debug Screen"));
        
        this.uiObjects.add(debugMenuButton);
        
        this.uiObjects.add(new Sidebar(this)); //Add the sidebar
        
        this.uiObjects.sort(); //Sort the UI objects so that they render in the order of their render depths
    }
    
    /**
     * Renders the game world
     */
    public void render()
    {
        //Clears screen buffer
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.worldShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        //Draws Entity Shapes
        for (Entity entity : this.world.getEntitites())
            entity.renderShapes(this.worldShapeRenderer);
        
        this.worldShapeRenderer.end();
        
        //Draws Entity Sprites
        for (Entity entity : this.world.getEntitites())
            entity.renderSprites(this.worldSpriteBatch);
        
        this.worldSpriteBatch.begin();
        
        this.worldSpriteBatch.end();
        
        //Updates UI objects
        for (UiObject obj : this.uiObjects)
            obj.update(this.world);
        
        //Draws UI Shapes
        this.uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (UiObject obj : this.uiObjects)
            obj.renderShapes(this.uiShapeRenderer);
        
        this.uiShapeRenderer.end();
        
        //Draws UI Sprites
        this.uiSpriteBatch.begin();
        
        for (UiObject obj : this.uiObjects)
            obj.renderSprites(this.uiSpriteBatch);
        
        this.uiSpriteBatch.draw(GameAssets.texBadlogic, Gdx.graphics.getWidth() - GameAssets.texBadlogic.getWidth(), 0); //Draws LibGDX logo
        
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
