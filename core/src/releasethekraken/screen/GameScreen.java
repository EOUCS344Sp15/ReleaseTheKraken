/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import releasethekraken.GameWorld;
import releasethekraken.InputHandler;
import releasethekraken.LevelLoader;
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
        
        //Creates a class to handle user input. Tells LibGDX about it.
        this.inputHandler = new InputHandler(this.world, this.renderer);
        Gdx.input.setInputProcessor(this.inputHandler);
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
