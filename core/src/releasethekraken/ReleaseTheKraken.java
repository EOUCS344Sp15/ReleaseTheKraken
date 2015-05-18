package releasethekraken;

import releasethekraken.ui.GameRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityGunTower;
import releasethekraken.entity.seacreature.EntityFish;
import releasethekraken.entity.seacreature.EntityPlayer;

public class ReleaseTheKraken extends ApplicationAdapter
{    
    private GameWorld world;
    private GameRenderer renderer;
    private GameAssets gameAssets;
    private InputHandler inputHandler;
    
    /** The HashMap to store registered entities */
    private static HashMap<String, Class<? extends Entity>> entityMap = new HashMap<String, Class<? extends Entity>>();
    
    //Register game objects
    static
    {
        //Register entities
        registerEntity("EntityPowerup", EntityPowerUp.class);
        registerEntity("EntityFish", EntityFish.class);
        registerEntity("EntityGunTower", EntityGunTower.class);
        registerEntity("EntityPlayer", EntityPlayer.class);
    }

    @Override
    public void create()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Application Starting!");
        
        /*
            Create a class to load and handle assets.  All game assets will have
            been loaded beyond this point.
        */
        this.gameAssets = new GameAssets();
        
        String levelName = "TestLevel";
        
        //Load the world
        LevelLoader levelLoader = new LevelLoader(levelName);
        this.world = levelLoader.loadWorld();
        
        //Create game renderer for the world
        this.renderer = new GameRenderer(this.world);
        
        //Creates a class to handle user input. Tells LibGDX about it.
        this.inputHandler = new InputHandler(this.world, this.renderer);
        Gdx.input.setInputProcessor(this.inputHandler);
    }

    @Override
    public void render() //This gets called 60 times a second.  Consider this the game loop.
    {
        this.inputHandler.update();
        
        this.world.update();
        this.renderer.render();
    }
    
    @Override
    public void dispose() 
    {
        super.dispose();
        Gdx.app.log(this.getClass().getSimpleName(), "Application Closing!");
        
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.world.dispose();
        this.renderer.dispose();
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
