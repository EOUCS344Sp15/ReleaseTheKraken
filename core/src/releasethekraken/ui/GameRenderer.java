/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import releasethekraken.GameWorld;
import releasethekraken.entity.Entity;
import releasethekraken.path.SeaCreaturePath;
import releasethekraken.ui.tooltip.TextToolTip;
import releasethekraken.ui.tooltip.ToolTip;

/**
 * This class renders the Game World.
 * 
 * @author Dalton
 */
public class GameRenderer implements Disposable
{
    /** The Game Renderer's reference to the game world it should be rendering */
    private GameWorld world;
    /** SpriteBatch to render UI sprites */
    private SpriteBatch uiSpriteBatch;
    /** SpriteBatch to render world sprites */
    private SpriteBatch worldSpriteBatch;
    /** ShapeRenderer to render UI shapes */
    private ShapeRenderer uiShapeRenderer;
    /** ShapeRenderer to render world shapes */
    private ShapeRenderer worldShapeRenderer;
    /** The camera to view the world */
    private OrthographicCamera camera;
    /** The Box2D Debug Renderer */
    Box2DDebugRenderer box2DDebugRenderer;
    /** The OrthogonalTiledMapRenderer to render the tile map */
    OrthogonalTiledMapRenderer tiledMapRenderer;
    
    /** The array of UiObjects */
    public Array<UiObject> uiObjects;
    
    /** Whether the debug screen is visible or not*/
    public boolean debugScreenVisible = false;
    private DebugOverlay debugOverlay;
    
    /** A list of all paths to render */
    private Array<SeaCreaturePath> seaCreaturePaths = new Array<SeaCreaturePath>();
    
    /**
     * Constructs a new GameRenderer
     * @param world The world to be rendered
     */
    public GameRenderer(GameWorld world)
    {
        this.world = world;
        
        //Populate the list of connected paths
        this.world.getFirstPath().getAllConnectedPaths(this.seaCreaturePaths);
        
        //Create the camera to view the world
        this.camera = new OrthographicCamera();
        float cameraWidth = 80.0F;
        float cameraHeight = (Gdx.graphics.getHeight()*1F/Gdx.graphics.getWidth())*cameraWidth;
        
        this.camera.setToOrtho(false, cameraWidth, cameraHeight);
        Gdx.app.log("GameRenderer", "Calculated camera dimensions: " + cameraWidth + " x " + cameraHeight);
        
        this.uiSpriteBatch = new SpriteBatch();
        this.worldSpriteBatch = new SpriteBatch();
        this.uiShapeRenderer = new ShapeRenderer();
        this.worldShapeRenderer = new ShapeRenderer();
        
        //Set the world renderers to render to the camera's projection matrix
        this.worldSpriteBatch.setProjectionMatrix(this.camera.combined);
        this.worldShapeRenderer.setProjectionMatrix(this.camera.combined);
        
        //Create the Box2D debug renderer
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.box2DDebugRenderer.setDrawVelocities(true);
        
        //Create the tile map renderer
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(world.getTiledMap(), 1/(world.getTiledMapUnitScale()));
        
        this.uiObjects = new Array<UiObject>();
        
        this.debugOverlay = new DebugOverlay(this);
        this.uiObjects.add(this.debugOverlay);
          
        UiButton pauseButton = new UiButton(
                this,
                Gdx.graphics.getWidth()-0.075F*Gdx.graphics.getWidth(), 
                Gdx.graphics.getHeight()-0.05F*Gdx.graphics.getHeight(), 
                0.075F, 0.05F, "Pause", Color.GRAY.cpy().sub(0.1F, 0.1F, 0.1F, 0))
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
                0.157F, 0.05F, "Debug Screen", Color.GRAY.cpy().sub(0.1F, 0.1F, 0.1F, 0))
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
        //The width of the sidebar in local camera units
        float sidebarLocalWidth = 0.2F*this.camera.viewportWidth;
        
        //The amount to offset the player focus so that it is centered on the screen taking into account the sidebar
        float playerXOffset = sidebarLocalWidth/2;
        
        //Calculate the center position of the camera
        float cameraX = MathUtils.clamp(
                this.world.getPlayer().getPos().x - playerXOffset,
                this.camera.viewportWidth/2 - sidebarLocalWidth,
                this.world.getWidth() - this.camera.viewportWidth/2);
        float cameraY = MathUtils.clamp(
                this.world.getPlayer().getPos().y,
                this.camera.viewportHeight/2,
                this.world.getHeight() - this.camera.viewportHeight/2);
        
        //Position the camera
        this.camera.position.set(cameraX, cameraY, 0);
        this.camera.update();
        
        //Clears screen buffer
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Update the renderer's projection matrices
        this.worldSpriteBatch.setProjectionMatrix(this.camera.combined);
        this.worldShapeRenderer.setProjectionMatrix(this.camera.combined);
        
        //Render tiles
        this.tiledMapRenderer.setView(this.camera);
        this.tiledMapRenderer.render();
        
        this.worldShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        //Draws Entity Shapes
        for (Body body : this.world.getPhysBodies())
        {
            Object object = body.getUserData();
            if (object instanceof Entity)
                ((Entity)object).renderShapes(this.worldShapeRenderer);
        }
        
        this.worldShapeRenderer.end();
        
        this.worldSpriteBatch.begin();
        
        //Draws Entity Sprites
        for (Body body : this.world.getPhysBodies())
        {
            Object object = body.getUserData();
            if (object instanceof Entity)
                ((Entity)object).renderSprites(this.worldSpriteBatch);
        }
        
        this.worldSpriteBatch.end();
        
        //Draw debug world overlay stuff
        if (this.debugScreenVisible)
        {
            this.worldShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        
            //Draw paths
            this.worldShapeRenderer.setColor(Color.RED);
            for (SeaCreaturePath scPath : this.seaCreaturePaths)
                this.worldShapeRenderer.polyline(scPath.getPolyline().getTransformedVertices());

            this.worldShapeRenderer.end();
            
            //Render Box2D debug renderer
            this.box2DDebugRenderer.render(this.world.getPhysWorld(), this.camera.combined);
        }
        
        //Updates UI objects
        for (UiObject obj : this.uiObjects)
            obj.update(this.world);
        
        //Draws UI Shapes
        this.uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (UiObject obj : this.uiObjects)
            if (!(obj instanceof ToolTip))
                obj.renderShapes(this.uiShapeRenderer);
        
        this.uiShapeRenderer.end();
        
        //Draws UI Sprites
        this.uiSpriteBatch.begin();
        
        for (UiObject obj : this.uiObjects)
            if (!(obj instanceof ToolTip))
                obj.renderSprites(this.uiSpriteBatch);
        
        //this.uiSpriteBatch.draw(GameAssets.texBadlogic, Gdx.graphics.getWidth() - GameAssets.texBadlogic.getWidth(), 0); //Draws LibGDX logo
        
        this.uiSpriteBatch.end();
        
        //Draws UI ToolTip Shapes
        this.uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (UiObject obj : this.uiObjects)
            if (obj instanceof ToolTip)
                obj.renderShapes(this.uiShapeRenderer);
        
        this.uiShapeRenderer.end();
        
        //Draws UI ToolTip Sprites
        this.uiSpriteBatch.begin();
        
        for (UiObject obj : this.uiObjects)
            if (obj instanceof ToolTip)
                obj.renderSprites(this.uiSpriteBatch);

        this.uiSpriteBatch.end();
    }

    /**
     * Gets the camera instance
     * @return The OrthographicCamera instance
     */
    public OrthographicCamera getCamera()
    {
        return camera;
    }
    
    @Override
    public void dispose()
    {
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.uiSpriteBatch.dispose();
        this.worldSpriteBatch.dispose();
        this.uiShapeRenderer.dispose();
        this.worldShapeRenderer.dispose();
        this.tiledMapRenderer.dispose();
        this.box2DDebugRenderer.dispose();
    }
}
