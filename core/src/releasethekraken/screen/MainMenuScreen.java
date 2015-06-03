/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.ui.UiButton;
import releasethekraken.ui.UiText;
import releasethekraken.ui.renderer.UiRenderer;
import releasethekraken.ui.tooltip.TextToolTip;

/**
 * This screen is the main menu screen
 * @author Dalton
 */
public class MainMenuScreen extends AbstractScreen
{    
    /**
     * Constructs a new MainMenuScreen
     * @param rtk The ReleaseTheKraken instance.  This is final so that the
     * anonymous inner classes can access it.
     */
    public MainMenuScreen(final ReleaseTheKraken rtk)
    {
        super(rtk);
        
        this.renderer = new UiRenderer();
        
        float scrWidth = Gdx.graphics.getWidth();
        float scrHeight = Gdx.graphics.getHeight();
        
        float textWidth = 0.2F;
        float textHeight = 0.2F;
        
        this.renderer.uiObjects.add(new UiText(this.renderer,
                scrWidth/2,
                scrHeight*0.75F,
                textWidth,
                textHeight,
                "Release The Kraken"));
        
        this.renderer.uiObjects.add(new UiText(this.renderer,
                scrWidth/2,
                scrHeight*0.55F,
                textWidth,
                textHeight,
                "Main Menu"));
        
        float buttonWidth = 0.3F;
        float buttonHeight = 0.08F;
        float buttonY = 0.4F;
        
        //Add the play button
        UiButton playButton = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                "Play Game",
                Color.GRAY)
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);
                        
                        //Start the game by pushing the game screen
                        rtk.pushScreen(new GameScreen(rtk, "TestLevel"));
                    }
                };
        
        playButton.setToolTip(new TextToolTip(this.renderer, "Start the game"));
        this.renderer.uiObjects.add(playButton);
        
        buttonY = 0.3F;
        
        //Add the settings button
        UiButton settingsButton = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                "Settings",
                Color.GRAY)
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);
                        
                        //Open the settings screen
                        rtk.pushScreen(new SettingsScreen(rtk));
                    }
                };
        
        settingsButton.setToolTip(new TextToolTip(this.renderer, "Change settings"));
        this.renderer.uiObjects.add(settingsButton);
        
        buttonY = 0.2F;
        
        //Add the quit button
        UiButton quitButton = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                "Quit",
                Color.GRAY)
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);
                        
                        Gdx.app.exit(); //Close the application
                    }
                };
        
        quitButton.setToolTip(new TextToolTip(this.renderer, "Exit the game"));
        this.renderer.uiObjects.add(quitButton);
        
        this.renderer.uiObjects.sort(); //Sort the UI objects once they are all added
    }
}
