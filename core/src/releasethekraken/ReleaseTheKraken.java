package releasethekraken;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ReleaseTheKraken extends ApplicationAdapter
{

    SpriteBatch batch;
    Texture img;

    @Override
    public void create()
    {
        System.out.println("Test message: Application Created");
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render()
    {
        /*
            The game loop goes here.  This method gets called 60 times
            a second.
        */
        
        //Default LibGDX logo
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }
}
