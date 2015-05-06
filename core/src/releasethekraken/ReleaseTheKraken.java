package releasethekraken;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ReleaseTheKraken extends ApplicationAdapter
{    
    private GameWorld world;
    private GameRenderer renderer;
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
        
        //Create game world and game renderer
        this.world = new GameWorld();
        this.renderer = new GameRenderer(this.world);
    }

    @Override
    public void render() //This gets called 60 times a second.  Consider this the game loop.
    {
        this.world.update();
        this.renderer.render();
    }
    
    @Override
    public void dispose() 
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Application Closing!");
        
        //Dispose of any LibGDX disposeable stuff here to avoid memory leaks
        this.world.dispose();
        this.renderer.dispose();
        this.gameAssets.dispose();
    }
}
