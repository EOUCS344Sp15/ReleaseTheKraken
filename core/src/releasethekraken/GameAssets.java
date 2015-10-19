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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
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
    public static TextureRegion[] entityFishTextures;
    public static TextureRegion[] entityFishLayer2Textures;
    public static TextureRegion[] entityTurtleTextures;
    public static TextureRegion[] entityOrcaTextures;
    public static TextureRegion entityGunTowerTexture;
    public static TextureRegion entityPirateBaseTexture;
    public static TextureRegion entityPirateCannonTexture;
    public static TextureRegion entityShipCannonTexture;
    
    public static TextureRegion entityKrakenBodyTexture;
    public static TextureRegion entityKrakenTenticle1Texture;
    public static TextureRegion entityKrakenTenticle2Texture;
    public static TextureRegion entityKrakenGripperTexture;
    
    public static TextureRegion[] seaShellTextures;
    public static TextureRegion waterSquirtTexture;
    public static TextureRegion waterBombTexture;
    public static TextureRegion cannonBallTexture;
    public static TextureRegion bulletTexture;
    
    public static TextureRegion[] powerupTextures;
    public static TextureRegion coinTexture;
    public static TextureRegion heartTexture;
    public static TextureRegion strengthTexture;
    public static TextureRegion clockTexture;
    
    public static Animation entityFishAnimation;
    public static Animation entityFishLayer2Animation;
    public static Animation entityTurtleAnimation;
    public static Animation entityOrcaAnimation;
    
    public static BitmapFont fontMain;
    public static BitmapFont fontDebug;
    public static BitmapFont fontWorldSmall;
    
    public static ShaderProgram pauseBackgroundShader;
    public static ShaderProgram tilemapShader;
    
    public static ParticleEffect effectExplosionCannonBall;
    
    public static ParticleEffectPool effectExplosionCannonBallPool;
    
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
        AssetDescriptor fontWorldSmallDesc = new AssetDescriptor(
                Gdx.files.internal("data/WorldFontSmall.fnt"), BitmapFont.class);
        
        this.load(fontMainDesc);
        this.load(fontDebugDesc);
        this.load(fontWorldSmallDesc);
        this.load("entities.png", Texture.class);
        this.load("hudSprites.png", Texture.class);
        
        this.finishLoading(); //Waits until all assets are loaded
        
        effectExplosionCannonBall = new ParticleEffect();
        effectExplosionCannonBall.load(Gdx.files.internal("effects/Explosion.p"), Gdx.files.internal("effects"));
        
        effectExplosionCannonBallPool = new ParticleEffectPool(effectExplosionCannonBall, 1, 20);
        
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
        
        fontWorldSmall = ((BitmapFont)this.get(fontWorldSmallDesc));
        fontWorldSmall.getData().setScale(1.0F); //TODO: How can this be scaled small enough to be drawn in the world?
        fontWorldSmall.getData().markupEnabled = true;
        
        entityPlayerTexture = new TextureRegion(entityTextures, 0, 0, 32, 32);
        entityGunTowerTexture = new TextureRegion(entityTextures, 128, 0, 32, 32);
        entityPirateBaseTexture = new TextureRegion(entityTextures, 96, 160, 160, 96);
        entityPirateCannonTexture = new TextureRegion(entityTextures, 160, 0, 32, 32);
        entityShipCannonTexture = new TextureRegion(entityTextures, 160, 192, 32, 16);
        
        entityKrakenBodyTexture = new TextureRegion(entityTextures, 256, 0, 112, 64);
        entityKrakenTenticle1Texture = new TextureRegion(entityTextures, 368, 0, 48, 16);
        entityKrakenTenticle2Texture = new TextureRegion(entityTextures, 368, 16, 64, 16);
        entityKrakenGripperTexture = new TextureRegion(entityTextures, 368, 32, 64, 32);
        
        waterSquirtTexture = new TextureRegion(entityTextures, 0, 176, 16, 8);
        waterBombTexture = new TextureRegion(entityTextures, 0, 192, 32, 32);
        bulletTexture = new TextureRegion(entityTextures, 16, 176, 16, 16);
        cannonBallTexture = new TextureRegion(entityTextures, 32, 192, 16, 16);
        
        entityFishTextures = new TextureRegion[2];
        for (int i=0; i<entityFishTextures.length; i++)
            entityFishTextures[i] = new TextureRegion(entityTextures, i*32, 48, 32, 16);
        entityFishAnimation = new Animation(0.1F, entityFishTextures);
        entityFishAnimation.setPlayMode(Animation.PlayMode.LOOP);
        
        entityFishLayer2Textures = new TextureRegion[2];
        for (int i=0; i<entityFishLayer2Textures.length; i++)
            entityFishLayer2Textures[i] = new TextureRegion(entityTextures, 64 + i*32, 48, 32, 16);
        entityFishLayer2Animation = new Animation(0.1F, entityFishLayer2Textures);
        entityFishLayer2Animation.setPlayMode(Animation.PlayMode.LOOP);
        
        entityTurtleTextures = new TextureRegion[2];
        for (int i=0; i<entityTurtleTextures.length; i++)
            entityTurtleTextures[i] = new TextureRegion(entityTextures, i*32, 64, 32, 32);
        entityTurtleAnimation = new Animation(0.2F, entityTurtleTextures);
        entityTurtleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        
        entityOrcaTextures = new TextureRegion[2];
        for (int i=0; i<entityOrcaTextures.length; i++)
            entityOrcaTextures[i] = new TextureRegion(entityTextures, i*112, 96, 112, 64);
        entityOrcaAnimation = new Animation(0.5F, entityOrcaTextures);
        entityOrcaAnimation.setPlayMode(Animation.PlayMode.LOOP);
        
        seaShellTextures = new TextureRegion[6];
        for (int i=0; i<seaShellTextures.length; i++)
            seaShellTextures[i] = new TextureRegion(entityTextures, 32 + i*8, 176, 8, 8);
        
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
