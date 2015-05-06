package releasethekraken.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import releasethekraken.ReleaseTheKraken;

public class DesktopLauncher
{

    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        
        config.title = "Release The Kraken";
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        config.foregroundFPS = 60; //This controls how many times ReleaseTheKraken.render() gets called
        //config.fullscreen = true;
        //config.addIcon("Icon Path", Files.FileType.Internal); //TODO: We need an icon
        
        new LwjglApplication(new ReleaseTheKraken(), config);
    }
}
