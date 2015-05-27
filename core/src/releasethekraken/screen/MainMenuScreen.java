/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import releasethekraken.ReleaseTheKraken;

/**
 * This screen is the main menu screen
 * @author Dalton
 */
public class MainMenuScreen extends AbstractScreen
{
    private int startupDelay = 2*60; //TODO: remove this once we have an actual main menu
    
    public MainMenuScreen(ReleaseTheKraken rtk)
    {
        super(rtk);
        Gdx.app.log("MainMenuScreen", "Waiting " + this.startupDelay/60F + " seconds to start the game!");
    }
    
    @Override
    public void render(float delta)
    {
        super.render(delta);
        
        if (this.startupDelay-- < 1)
        {
            Gdx.app.log("MainMenuScreen", "Pushing GameScreen");
            this.rtk.pushScreen(new GameScreen(rtk, "TestLevel")); //TODO: remove this when we have an actual main menu
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}
