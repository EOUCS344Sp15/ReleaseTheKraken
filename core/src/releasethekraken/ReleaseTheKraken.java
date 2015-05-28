package releasethekraken;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityGunTower;
import releasethekraken.entity.pirate.EntityPirateBase;
import releasethekraken.entity.seacreature.EntityFish;
import releasethekraken.entity.seacreature.EntityOrca;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.entity.seacreature.EntityTurtle;
import releasethekraken.screen.AbstractScreen;
import releasethekraken.screen.MainMenuScreen;

public class ReleaseTheKraken extends Game
{
    /** The HashMap to store registered entities */
    private static HashMap<String, Class<? extends Entity>> entityMap = new HashMap<String, Class<? extends Entity>>();
    
    /** How many times the game updates per second */
    public static final int TICK_RATE = 60;
    
    /** The stack of screens */
    private Stack<AbstractScreen> screenStack = new Stack<AbstractScreen>();
    
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
    
    /** The static InputHandler for the game */
    public static InputHandler inputHandler;
    
    @Override
    public void create()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Application Starting!");
        
        /*
            Create a class to load and handle assets.  All game assets will have
            been loaded beyond this point.
        */
        this.gameAssets = new GameAssets();
        
        //Create a class to handle input.  Tell LibGDX about it
        inputHandler = new InputHandler();
        Gdx.input.setInputProcessor(inputHandler);
        
        //Push the main menu onto the stack
        this.pushScreen(new MainMenuScreen(this));
    }
    
    @Override
    public void dispose() 
    {
        super.dispose();
        Gdx.app.log(this.getClass().getSimpleName(), "Application Closing!");
        this.gameAssets.dispose();
    }
    
    /**
     * Pushes a new AbstractScreen onto the stack
     * @param screen The new AbstractScreen to push
     */
    public void pushScreen(AbstractScreen screen)
    {
        this.screenStack.push(screen);
        this.setScreen(screen); //Update the screen being rendered
        
        Gdx.app.log("ReleaseTheKraken", "pushScreen(" + screen.getClass().getSimpleName() + ") called");
        Gdx.app.log("ReleaseTheKraken", "Screen Stack: " + this.screenStack);
    }
    
    /**
     * Pops an AbstractScreen off of the stack
     * @return The AbstractScreen popped off the stack
     */
    public AbstractScreen popScreen()
    {
        AbstractScreen stackScreen = null;
        
        try
        {
            stackScreen = this.screenStack.pop();
        }
        catch (EmptyStackException e)
        {
            Gdx.app.log("ReleaseTheKraken", "popScreen() called, but the screen stack was empty!");
        }
        
        this.setScreen(this.screenStack.peek()); //Update the screen being rendered
        
        Gdx.app.log("ReleaseTheKraken", "popScreen() called");
        Gdx.app.log("ReleaseTheKraken", "Screen Stack: " + this.screenStack);

        return stackScreen;
    }
    
    /**
     * Peeks at the AbstractScreen on the top of the stack
     * @return The AbstractScreen on top of the stack
     */
    public AbstractScreen peekScreen()
    {
        AbstractScreen stackScreen = null;
        
        try
        {
            stackScreen = this.screenStack.pop();
        }
        catch (EmptyStackException e)
        {
            Gdx.app.log("ReleaseTheKraken", "peekScreen() called, but the screen stack was empty!");
        }

        return stackScreen;
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
