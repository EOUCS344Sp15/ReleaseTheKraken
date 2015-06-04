/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

/**
 * This screen is the pause screen
 * @author Lucas Schuetz
 */
public class PauseScreen extends AbstractScreen implements InputHandler.KeyListener
{
    /** The background texture */
    private static final Texture background;
    
    static //Initialize the background texture to some default
    {
        Pixmap backgroundPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        background = new Texture(backgroundPixmap);
        
        backgroundPixmap.dispose(); //Dispose of the temporary pixmap
    }
    
    /**
     * Constructs a new PauseScreen
     * @param rtk The ReleaseTheKraken instance.  This is final so that the
     * anonymous inner classes can access it.
     * @param pixmap The Pixmap for the background screenshot
     */
    public PauseScreen(final ReleaseTheKraken rtk, final Pixmap pixmap)
    {
        super(rtk);
        
        applyBackgroundEffect(pixmap); //Modify the screenshot to make it look different
        background.draw(pixmap, 0, 0); //Draw the pixmap to the texture
        
        this.renderer = new UiRenderer()
        {
            @Override
            public void render(float delta)
            {
                this.renderTime++;

                //Clears screen buffer
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
                //Draw the background
                this.uiSpriteBatch.setShader(GameAssets.pauseBackgroundShader); //Use the custom shader
                this.uiSpriteBatch.begin();
                this.uiSpriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                this.uiSpriteBatch.end();
                this.uiSpriteBatch.setShader(null); //Set the shader back to default

                this.renderUi(delta);
            }
        };
        
        float scrWidth = Gdx.graphics.getWidth();
        float scrHeight = Gdx.graphics.getHeight();
        
        float textWidth = 0.2F;
        float textHeight = 0.2F;
        
        this.renderer.uiObjects.add(new UiText(this.renderer,
                scrWidth/2,
                scrHeight*0.75F,
                textWidth,
                textHeight,
                "Pause Menu"));
        
        float buttonWidth = 0.3F;
        float buttonHeight = 0.08F;
        float buttonY = 0.5F;
        
        //Add the resume button
        UiButton playButton = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                "Resume Game",
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
        
        playButton.setToolTip(new TextToolTip(this.renderer, "Resume the game"));
        this.renderer.uiObjects.add(playButton);
        
        buttonY = 0.3F;
        
        //Add the main menu button
        UiButton mainMenuButton = new UiButton(this.renderer,
                scrWidth/2 - (scrWidth*buttonWidth/2),
                scrHeight*buttonY,
                buttonWidth,
                buttonHeight,
                "Main Menu",
                Color.GRAY)
                {
                    @Override
                    public void onClick(int mouseButton)
                    {
                        super.onClick(mouseButton);
                        
                        AbstractScreen top = rtk.peekScreen();
                        
                        // Pop until currently screen is the Main Menu
                        while(top != null && !(top instanceof MainMenuScreen)) 
                        {
                            rtk.popScreen();
                            top = rtk.peekScreen();
                        } // end while
                    }
                };
        
        mainMenuButton.setToolTip(new TextToolTip(this.renderer, "Return to Main Menu"));
        this.renderer.uiObjects.add(mainMenuButton);
        
        
        buttonY = 0.2F;
        
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
        
        buttonY = 0.1F;
        
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
        
        ReleaseTheKraken.inputHandler.registerKeyListener(this); //Register as a key listener
    }
    
    /**
     * Applies a background effect to the background, so it is obviously the background
     * @param pixmap The pixmap representing the background
     */
    private static void applyBackgroundEffect(final Pixmap pixmap)
    {        
        Gdx.app.log("PauseScreen.applyBackgroundEffect", "Format: " + pixmap.getFormat());
        ByteBuffer pixels = pixmap.getPixels();
        int numBytes = pixmap.getWidth() * pixmap.getHeight() * 4;
        byte[] lines = new byte[numBytes];
        int numBytesPerLine = pixmap.getWidth() * 4;

        for (int i=0; i<pixmap.getHeight(); i++)
        {
            pixels.position((i) * numBytesPerLine);
            pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
        }
        
        //TODO: Find out how to make the pixels darker or something
        //for (int i=0; i<lines.length; i+= 4)
        //    Gdx.app.log("PauseScreen.applyBackgroundEffect", "Pixel " + i/4 + ": " + lines[i] + " " + lines[i+1] + " " + lines[i+2] + " " + lines[i+3]);

        pixels.clear();
        pixels.put(lines);
        pixels.rewind(); //Reset the buffer so it can be used again
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
