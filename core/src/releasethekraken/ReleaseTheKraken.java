package releasethekraken;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityGunTower;
import releasethekraken.entity.pirate.EntityPirateBase;
import releasethekraken.entity.seacreature.EntityFish;
import releasethekraken.entity.seacreature.EntityOrca;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.entity.seacreature.EntityTurtle;
import releasethekraken.screen.GameScreen;

public class ReleaseTheKraken extends Game
{
    /** The HashMap to store registered entities */
    private static HashMap<String, Class<? extends Entity>> entityMap = new HashMap<String, Class<? extends Entity>>();
    
    /** How many times the game updates per second */
    public static final int TICK_RATE = 60;
    
    //Register game objects
    static
    {
        //Register entities
        registerEntity("EntityPowerup", EntityPowerUp.class);
        registerEntity("EntityFish", EntityFish.class);
        registerEntity("EntityTurtle", EntityTurtle.class);
        registerEntity("EntityOrca", EntityOrca.class);
        registerEntity("EntityGunTower", EntityGunTower.class);
        registerEntity("EntityPirateBase", EntityPirateBase.class);
        registerEntity("EntityPlayer", EntityPlayer.class);
    }
    
    private GameAssets gameAssets;
    
    @Override
    public void create()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Application Starting!");
        
        /*
            Create a class to load and handle assets.  All game assets will have
            been loaded beyond this point.
        */
        this.gameAssets = new GameAssets();
        
        this.setScreen(new GameScreen("TestLevel"));
    }
    
    @Override
    public void dispose() 
    {
        super.dispose();
        Gdx.app.log(this.getClass().getSimpleName(), "Application Closing!");
        this.gameAssets.dispose();
    }
    
    /**
     * Registers the entity with the game. The entity must have a "codename" to
     * be represented in XML, regardless if it is able to be loaded from XML or not.
     * 
     * @param name The name of the entity, in valid XML attribute format
     * @param entity The entity class being registered
     */
    public static void registerEntity(String name, Class<? extends Entity> entity)
    {
        entityMap.put(name, entity);
    }
    
    /**
     * Gets the Entity class registered for the given name, or null if none were
     * found.
     * 
     * @param name The entity name to search for
     * @return The entity class, or null
     */
    public static Class<? extends Entity> getEntityFromName(String name)
    {
        return entityMap.get(name);
    }
}
