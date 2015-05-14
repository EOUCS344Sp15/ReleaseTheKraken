package releasethekraken.desktop;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.utils.ObjectMap;
import javax.swing.JOptionPane;
import releasethekraken.ReleaseTheKraken;

public class DesktopLauncher
{
    //Taken from LwjglApplication to allow loading of preferences before it is started
    private static ObjectMap<String, Preferences> preferences = new ObjectMap<String, Preferences>();

    public static void main(String[] args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        boolean validArguments = true;

        try //Try to configure the app config, using the command line arguments
        {
            loadConfig(config);
        } catch (IllegalArgumentException e) //If something went wrong, explain it in a JOptionPane
        {
            validArguments = false;

            //Print the entire thing to the log
            System.err.println("Error loading settings from settings file");
            e.printStackTrace();

            //Show the JOptionPane error dialog
            JOptionPane.showMessageDialog(null, e, "Error loading settings from settings file",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (validArguments) //If all arguments are valid, start the game
        {
            new LwjglApplication(new ReleaseTheKraken(), config);
        }
    }

    /**
     * Sets up the window configuration, loading settings from the settings file
     * <br><br>
     * <b>Available Startup Parameters:</b>
     * <ul>
     * <li>fullscreen=true|false</li>
     * <li>res=widthxheight (Ex: res=1920x1080)</li>
     * </ul>
     *
     * @param config The LWJGL App Config
     * @throws IllegalArgumentException If an argument is invalid
     */
    private static void loadConfig(LwjglApplicationConfiguration config)
            throws IllegalArgumentException
    {
        //Set default settings
        config.title = "Release The Kraken";
        config.resizable = false;
        config.foregroundFPS = 60; //This controls how many times ReleaseTheKraken.render() gets called
        //config.addIcon("Icon Path", Files.FileType.Internal); //TODO: We need an icon

        //Load settings file
        Preferences settings = getPreferences("ReleaseTheKraken_Settings.xml", config);
        System.out.println("Loading settings from: " + config.preferencesDirectory + "ReleaseTheKraken_Settings.xml");
        
        //Add default settings if they aren't present
        if (!settings.contains("width"))
            settings.putInteger("width", 1280);
        if (!settings.contains("height"))
            settings.putInteger("height", 720);
        if (!settings.contains("fullscreen"))
            settings.putBoolean("fullscreen", false);
        settings.flush(); //Make sure these are saved
        
        //Load settings from file
        config.width = settings.getInteger("width");
        config.height = settings.getInteger("height");
        config.fullscreen = settings.getBoolean("fullscreen");
    }

    /**
     * loads the preferences early so that the window config can be loaded from
     * the settings file.
     * This was taken from LwjglApplication
     * 
     * @param name The name of the settings file
     * @param config The LwgjlApplicationConfiguration to get the directory from
     * @return The preferences object
     */
    private static Preferences getPreferences(String name, LwjglApplicationConfiguration config)
    {
        if (preferences.containsKey(name))
        {
            return preferences.get(name);
        } else
        {
            Preferences prefs = new LwjglPreferences(name, config.preferencesDirectory);
            preferences.put(name, prefs);
            return prefs;
        }
    }
}
