package releasethekraken.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.util.Arrays;
import javax.swing.JOptionPane;
import releasethekraken.ReleaseTheKraken;

public class DesktopLauncher
{

    public static void main(String[] args)
    {
        //args = new String[] {"fullscreen=true", "res=1920x1080"};
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        boolean validArguments = true;
        
        try //Try to configure the app config, using the command line arguments
        {
            loadConfig(config, args);
        }
        catch (IllegalArgumentException e) //If something went wrong, explain it in a JOptionPane
        {
             validArguments = false;
             
            //Print the entire thing to the log
            System.err.println("Error parsing command line arguments:");
            e.printStackTrace();
            
            //Show the JOptionPane error dialog
            JOptionPane.showMessageDialog(null, e, "Error Parsing Startup Parameters", 
                    JOptionPane.ERROR_MESSAGE);
        }
        
        if (validArguments) //If all arguments are valid, start the game
            new LwjglApplication(new ReleaseTheKraken(), config);
    }
    
    /**
     * Sets up the window configuration, optionally using values from the startup parameters
     * <br><br>
     * <b>Available Startup Parameters:</b>
     * <ul>
     *  <li>fullscreen=true|false</li>
     *  <li>res=widthxheight (Ex: res=1920x1080)</li>
     * </ul>
     * 
     * @param config The LWJGL App Config
     * @param args The array of String arguments
     * @throws IllegalArgumentException If an argument is invalid
     */
    private static void loadConfig(LwjglApplicationConfiguration config, String[] args) 
            throws IllegalArgumentException
    {
        //Set default settings
        config.title = "Release The Kraken";
        config.width = 1280;
        config.height = 720;
        config.resizable = false;
        config.foregroundFPS = 60; //This controls how many times ReleaseTheKraken.render() gets called
        //config.fullscreen = true;
        //config.addIcon("Icon Path", Files.FileType.Internal); //TODO: We need an icon
        
        //Parse the arguments if there are any
        if (args.length > 0)
        {
            System.out.println("Startup Parameters: " + Arrays.toString(args));
            
            for (String string : args) //Iterate over every String in the array
            {
                try
                {
                    //Split the string into the option and value
                    String option = string.split("=")[0];
                    String value = string.split("=")[1];

                    //Handle any options
                    if (option.equals("fullscreen"))
                    {
                        config.fullscreen = Boolean.parseBoolean(value);
                    }
                    else if (option.equals("res"))
                    {
                        String[] dimensions = value.split("x");
                        config.width = Integer.parseInt(dimensions[0]);
                        config.height = Integer.parseInt(dimensions[1]);
                    }
                }
                catch (Exception e)
                {
                    //Throw a new IllegalArgumentException explaining which parameter is invalid
                    throw new IllegalArgumentException("Parameter \"" + string 
                            + "\" is invalid!", e);
                }
            }
        }
    }
}
