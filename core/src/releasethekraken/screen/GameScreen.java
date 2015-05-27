/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import releasethekraken.GameWorld;
import releasethekraken.InputHandler;
import releasethekraken.LevelLoader;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.ui.GameRenderer;

/**
 * This screen is the actual game.
 * @author Dalton
 */
public class GameScreen implements Screen
{
    private GameWorld world;
    private GameRenderer renderer;
    private InputHandler inputHandler;
    
    public GameScreen(String levelName)
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Game Starting!");
        
        //Load the world
        LevelLoader levelLoader = new LevelLoader(levelName);
        this.world = levelLoader.loadWorld();
        
        //Create game renderer for the world
        this.renderer = new GameRenderer(this.world);
        
        this.inputHandler = ReleaseTheKraken.inputHandler;
    }
    
    @Override
    public void show()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "show() called");
    }

    @Override
    public void render(float delta) //This gets called 60 times a second.  Consider this the game loop.
    {
        this.inputHandler.update();
        
        //Update player's aim position
        Vector3 mousePos3D = new Vector3(ReleaseTheKraken.inputHandler.getPointerLocations().first(), 0); //Convert mouse 0 to Vector 3
        Vector3 worldMousePos3D = this.renderer.getCamera().unproject(mousePos3D); //Have the camera unproject the coordinates
        this.world.getPlayer().getAimPos().x = worldMousePos3D.x;
        this.world.getPlayer().getAimPos().y = worldMousePos3D.y;
        
        this.world.update();
        this.renderer.render();
    }

    @Override
    public void resize(int width, int height)
    {
        Gdx.app.log(this.getClass().getSimpleName(), "resize(" + width + ", " + height + ") called");
    }

    @Override
    public void pause()
    {
       Gdx.app.log(this.getClass().getSimpleName(), "pause() called");
    }

    @Override
    public void resume()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "resume() called");
    }

    @Override
    public void hide()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "hide() called");
    }

    @Override
    public void dispose()
    {        
        Gdx.app.log(this.getClass().getSimpleName(), "Game Ending!");
        
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.world.dispose();
        this.renderer.dispose();
    }
}
