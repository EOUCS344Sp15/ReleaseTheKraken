package releasethekraken;

import releasethekraken.ui.GameRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ReleaseTheKraken extends ApplicationAdapter
{    
    private GameWorld world;
    private GameRenderer renderer;
    private GameAssets gameAssets;
    private InputHandler inputHandler;

    @Override
    public void create()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Application Starting!");
        
        /*
            Create a class to load and handle assets.  All game assets will have
            been loaded beyond this point.
        */
        this.gameAssets = new GameAssets();
                
        //Create game world and game renderer
        this.world = new GameWorld("HardCodeLand", 200, 100);
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
}
