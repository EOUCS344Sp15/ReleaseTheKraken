/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.nio.ByteBuffer;
import releasethekraken.GameAssets;
import releasethekraken.InputHandler;
import releasethekraken.ReleaseTheKraken;
import releasethekraken.ui.UiButton;
import releasethekraken.ui.UiText;
import releasethekraken.ui.renderer.UiRenderer;
import releasethekraken.ui.tooltip.TextToolTip;

/**The settings screen provides the user with the ability to change settings.
 *
 * @author Mitch
 */
public class SettingsScreen extends AbstractScreen implements InputHandler.KeyListener
{
   
    
    /**
     * Constructs a new PauseScreen
     * @param rtk The ReleaseTheKraken instance.  This is final so that the
     * anonymous inner classes can access it.
     */
    public SettingsScreen(final ReleaseTheKraken rtk)
    {
        super(rtk);
        
        final Preferences settings = Gdx.app.getPreferences("ReleaseTheKraken_Settings.xml");
        boolean fullscreen = settings.getBoolean("fullscreen");
        
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
                "Welcome to the settings menu!"));
        
        float buttonWidth = 0.3F;
        float buttonHeight = 0.08F;
        float buttonY = 0.5F;
        
        //Add the resume button
        UiButton backButton = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                "Back",
                Color.GRAY)
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);
                        
                        //Start the game by pushing the game screen
                        rtk.popScreen();
                    }
                };
        
        backButton.setToolTip(new TextToolTip(this.renderer, "Return to Previous Screen"));
        this.renderer.uiObjects.add(backButton);
        
        buttonY = 0.3F;
        
        //Add the main menu button
        UiButton screenSize = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                fullscreen ? "Disable Fullscreen" : "Enable Fullscreen",
                Color.GRAY)
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);
               
                   //This alters the settings folder and crashes the game on mac. Needs some work.
                        boolean fullscreen = settings.getBoolean("fullscreen");
                        
                        fullscreen = !fullscreen;
                        settings.putBoolean("fullscreen", fullscreen);
                        
                        settings.flush();
                        
                        this.text = fullscreen ? "Disable Fullscreen" : "Enable Fullscreen";
                 
                    }
                };
        
        screenSize.setToolTip(new TextToolTip(this.renderer, "Requires you to restart the game."));
        this.renderer.uiObjects.add(screenSize);
        
        
        
        this.renderer.uiObjects.sort(); //Sort the UI objects once they are all added
        
        ReleaseTheKraken.inputHandler.registerKeyListener(this); //Register as a key listener
    }
    

    
    @Override
    public void keyDown(int keycode)
    {
        switch (keycode)
        {
        case Input.Keys.ESCAPE:
            this.rtk.popScreen(); //Pop the pause menu off of the stack
            break;
        }
    }

    @Override
    public void keyUp(int keycode) {}

    @Override
    public void keyHeld(int keycode) {}
}
  






