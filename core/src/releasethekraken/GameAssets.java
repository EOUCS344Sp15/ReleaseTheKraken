/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Loads the game assets and provides static references to them
 * 
 * @author Dalton
 */
public class GameAssets extends AssetManager
{
    //Static references to the game assets
    public static Texture texBadlogic;
    
    public static Texture entityTextures;
    public static Texture uiTextures;
    
    public static TextureRegion entityPlayerTexture;
    public static TextureRegion entityFishTexture;
    public static TextureRegion entityGunTowerTexture;
    
    public static TextureRegion[] powerupTextures;
    public static TextureRegion coinTexture;
    
    public static BitmapFont fontMain;
    public static BitmapFont fontDebug;
    
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
        AssetDescriptor fontMainDesc = new AssetDescriptor(
                Gdx.files.internal("data/GameFont.fnt"), BitmapFont.class);
        AssetDescriptor fontDebugDesc = new AssetDescriptor(
                Gdx.files.internal("data/DebugFont.fnt"), BitmapFont.class);
        
        this.load("badlogic.jpg", Texture.class);
        this.load(fontMainDesc);
        this.load(fontDebugDesc);
        this.load("entities.png", Texture.class);
        this.load("hudSprites.png", Texture.class);
        
        this.finishLoading(); //Waits until all assets are loaded
        
        texBadlogic = this.get("badlogic.jpg", Texture.class);
        entityTextures = this.get("entities.png", Texture.class);
        uiTextures = this.get("hudSprites.png", Texture.class);
        
        //The multiplier to determine the scale for the text
        float textScaleMultiplier = Gdx.graphics.getWidth()/1280.0F;
        
        fontMain = (BitmapFont) this.get(fontMainDesc);
        fontMain.getData().setScale(0.5F*textScaleMultiplier);
        
        fontDebug = (BitmapFont) this.get(fontDebugDesc);
        fontDebug.getData().setScale(0.25F*textScaleMultiplier);
        
        entityPlayerTexture = new TextureRegion(entityTextures, 0, 0, 32, 32);
        entityFishTexture = new TextureRegion(entityTextures, 32, 0, 32, 32);
        entityGunTowerTexture = new TextureRegion(entityTextures, 64, 0, 32, 32);
        
        powerupTextures = new TextureRegion[4];
        for (int i=0; i<4; i++)
            powerupTextures[i] = new TextureRegion(uiTextures, i*32, 0, 32, 32);
        
        coinTexture = new TextureRegion(uiTextures, 0, 32, 16, 16);
    }
}
