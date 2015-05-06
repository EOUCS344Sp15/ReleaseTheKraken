/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Loads the game assets and provides static references to them
 * 
 * @author Dalton
 */
public class GameAssets extends AssetManager
{
    //Static references to the game assets
    public static Texture texBadlogic;
    
    //Constructor
    public GameAssets()
    {
        super(); //Calls super class constructor
        this.loadAssets(); //Loads the game assets
    }
    
    /**
     * Loads the game assets.  Doesn't return until they are all loaded.
     */
    private void loadAssets()
    {
        this.load("badlogic.jpg", Texture.class);
        
        this.finishLoading(); //Waits until all assets are loaded
        
        texBadlogic = this.get("badlogic.jpg", Texture.class);
    }
}
