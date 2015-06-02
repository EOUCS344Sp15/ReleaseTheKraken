/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Loads the game assets and provides static references to them
 * 
 * @author Dalton
 */
public class GameAssets extends AssetManager
{
    //Static references to the game assets    
    public static Texture entityTextures;
    public static Texture uiTextures;
    
    public static TextureRegion entityPlayerTexture;
    public static TextureRegion entityFishTexture;
    public static TextureRegion entityTurtleTexture;
    public static TextureRegion entityOrcaTexture;
    public static TextureRegion entityGunTowerTexture;
    public static TextureRegion entityPirateBaseTexture;
    
    public static TextureRegion[] seaShellTextures;
    public static TextureRegion waterSquirtTexture;
    public static TextureRegion waterBombTexture;
    
    public static TextureRegion[] powerupTextures;
    public static TextureRegion coinTexture;
    public static TextureRegion heartTexture;
    public static TextureRegion strengthTexture;
    public static TextureRegion clockTexture;
    
    public static BitmapFont fontMain;
    public static BitmapFont fontDebug;
    
    public static ShaderProgram pauseBackgroundShader;
    public static ShaderProgram tilemapShader;
    
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
        
        this.load(fontMainDesc);
        this.load(fontDebugDesc);
        this.load("entities.png", Texture.class);
        this.load("hudSprites.png", Texture.class);
        
        this.finishLoading(); //Waits until all assets are loaded
        
        pauseBackgroundShader = loadShader("pause"); //Load the pause background shaders
        
        ShaderProgram.pedantic = false; //TODO: Change back
        tilemapShader = loadShader("tilemap"); //Load the tilemap shaders
        
        // Configure shader settings. See http://javadocmd.com/blog/libgdx-dynamic-textures-with-pixmap/
        tilemapShader.begin();
        tilemapShader.setUniformi("u_texture", 0);
        tilemapShader.setUniformi("u_mask", 1);
        tilemapShader.end();
        
        entityTextures = this.get("entities.png", Texture.class);
        uiTextures = this.get("hudSprites.png", Texture.class);
        
        //The multiplier to determine the scale for the text
        float textScaleMultiplier = Gdx.graphics.getWidth()/1280.0F;
        
        fontMain = (BitmapFont) this.get(fontMainDesc);
        fontMain.getData().setScale(0.5F*textScaleMultiplier);
        fontMain.getData().markupEnabled = true;
        
        fontDebug = (BitmapFont) this.get(fontDebugDesc);
        fontDebug.getData().setScale(0.25F*textScaleMultiplier);
        fontDebug.getData().markupEnabled = true;
        
        entityPlayerTexture = new TextureRegion(entityTextures, 0, 0, 32, 32);
        entityFishTexture = new TextureRegion(entityTextures, 32, 0, 32, 32);
        entityTurtleTexture = new TextureRegion(entityTextures, 64, 0, 32, 32);
        entityOrcaTexture = new TextureRegion(entityTextures, 0, 32, 128, 64);
        entityGunTowerTexture = new TextureRegion(entityTextures, 128, 0, 32, 32);
        entityPirateBaseTexture = new TextureRegion(entityTextures, 96, 160, 160, 96);
        
        waterSquirtTexture = new TextureRegion(entityTextures, 0, 96, 16, 8);
        waterBombTexture = new TextureRegion(entityTextures, 0, 112, 32, 32);
        
        seaShellTextures = new TextureRegion[6];
        for (int i=0; i<seaShellTextures.length; i++)
            seaShellTextures[i] = new TextureRegion(entityTextures, 32 + i*8, 96, 8, 8);
        
        powerupTextures = new TextureRegion[4];
        for (int i=0; i<powerupTextures.length; i++)
            powerupTextures[i] = new TextureRegion(uiTextures, i*32, 0, 32, 32);
        
        coinTexture = new TextureRegion(uiTextures, 0, 32, 16, 16);
        heartTexture = new TextureRegion(uiTextures, 16, 32, 16, 16);
        strengthTexture = new TextureRegion(uiTextures, 32, 32, 16, 16);
        clockTexture = new TextureRegion(uiTextures, 48, 32, 16, 16);
    }
    
    /**
     * Loads a ShaderProgram from the vertex and fragment shader files.  Crashes
     * the game if they fail to compile.
     * @param name The name of the prefix of the shader files
     * @return The loaded, compiled ShaderProgram
     * @throws RuntimeException if the shaders fail to compile
     */
    private static ShaderProgram loadShader(String name) throws RuntimeException
    {
        FileHandle vertexShader = Gdx.files.internal("shaders/" + name + "_vertex.glsl");
        FileHandle fragmentShader = Gdx.files.internal("shaders/" + name + "_fragment.glsl");
        
        ShaderProgram shader = new ShaderProgram(vertexShader.readString(), fragmentShader.readString());
        
        //Crash if the shader wasn't able to be compiled
        if (!shader.isCompiled())
            throw new RuntimeException("Failed to compile " + name + " shader files!\nDetails:\n\n"
                    + shader.getLog());
        
        return shader;
    }
}
