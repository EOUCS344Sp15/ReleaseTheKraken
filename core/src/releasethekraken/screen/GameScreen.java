/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import releasethekraken.GameWorld;
import releasethekraken.InputHandler;
import releasethekraken.LevelLoader;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.ui.renderer.GameRenderer;
import releasethekraken.util.Screenshots;

/**
 * This screen is the actual game.
 * @author Dalton
 */
public class GameScreen extends AbstractScreen implements InputHandler.KeyListener
{
    private GameWorld world;
    private GameRenderer renderer;
    
    public GameScreen(ReleaseTheKraken rtk, String levelName)
    {
        super(rtk);
        
        //Load the world
        LevelLoader levelLoader = new LevelLoader(levelName);
        this.world = levelLoader.loadWorld();
        
        //Create game renderer for the world
        this.renderer = new GameRenderer(rtk, this.world);
        
        ReleaseTheKraken.inputHandler.registerKeyListener(this); //Register as a key listener
    }

    @Override
    public void render(float delta) //This gets called 60 times a second.  Consider this the game loop.
    {
        super.render(delta);
        
        //Update player's aim position
        Vector3 mousePos3D = new Vector3(ReleaseTheKraken.inputHandler.getPointerLocations().first(), 0); //Convert mouse 0 to Vector 3
        Vector3 worldMousePos3D = this.renderer.getCamera().unproject(mousePos3D); //Have the camera unproject the coordinates
        this.world.getPlayer().getAimPos().x = worldMousePos3D.x;
        this.world.getPlayer().getAimPos().y = worldMousePos3D.y;
        
        this.world.update();
        this.renderer.render();
    }

    @Override
    public void dispose()
    {        
        super.dispose();
        
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.world.dispose();
        this.renderer.dispose();
    }

    @Override
    public void keyDown(int keycode)
    {
        switch (keycode)
        {
        case Input.Keys.ESCAPE:
            //Take a screenshot of the game
            Pixmap pixmap = Screenshots.getScreenshot(true);

            //Push a new pause screen onto the screen stack
            this.rtk.pushScreen(new PauseScreen(rtk, pixmap));
            break;
        }
    }

    @Override
    public void keyUp(int keycode) {}

    @Override
    public void keyHeld(int keycode) {}
}
