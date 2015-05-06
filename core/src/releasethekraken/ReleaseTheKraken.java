package releasethekraken;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ReleaseTheKraken extends ApplicationAdapter
{
    //TODO: Move this to an asset loader
    public static Texture img;
    
    private GameWorld world;
    private GameRenderer renderer;

    @Override
    public void create()
    {
        Gdx.app.log(this.getClass().getSimpleName(), "Application Starting!");
        
        //TODO: Move this to an asset loader
        img = new Texture("badlogic.jpg");
        
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
    }
}
