/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import releasethekraken.InputHandler;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.ui.renderer.UiRenderer;

/**
 * Represents an abstract screen for the game.  Each game screen should extend this
 * @author Dalton
 */
public abstract class AbstractScreen implements Screen
{
    /** The Array of KeyListeners specific to this screen */
    protected Array<InputHandler.KeyListener> keyListeners = new Array<InputHandler.KeyListener>();
    /** The Array of TouchListeners specific to this screen */
    protected Array<InputHandler.TouchListener> touchListeners = new Array<InputHandler.TouchListener>();
    /** The ReleaseTheKraken instance */
    protected ReleaseTheKraken rtk;
    /** The Renderer for the screen */
    protected UiRenderer renderer;
    
    /**
     * Constructs a new screen
     * @param rtk The ReleaseTheKraken instance
     */
    public AbstractScreen(ReleaseTheKraken rtk)
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Screen Created");
        
        //Set the InputHandler's listener lists to this screen's lists
        ReleaseTheKraken.inputHandler.setKeyListeners(this.keyListeners);
        ReleaseTheKraken.inputHandler.setTouchListeners(this.touchListeners);
        this.rtk = rtk;
    }

    @Override
    public void show()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "show() called");
        
        //Set the InputHandler's listener lists to this screen's lists
        ReleaseTheKraken.inputHandler.setKeyListeners(this.keyListeners);
        ReleaseTheKraken.inputHandler.setTouchListeners(this.touchListeners);
    }

    @Override
    public void render(float delta)
    {
        //Gdx.app.log(this.getClass().getSimpleName(), "render() called!");
        
        ReleaseTheKraken.inputHandler.update();
        
        if (this.renderer != null)
            this.renderer.render(delta);
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
        Gdx.app.log(this.getClass().getSimpleName(), "dispose() called");
        this.renderer.dispose();
    }
}
