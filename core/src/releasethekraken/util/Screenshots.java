/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;
import java.nio.ByteBuffer;

/**
 * Provides methods of taking and saving screenshots.
 * Based off of the code on <a href="https://github.com/libgdx/libgdx/wiki/Take-a-Screenshot">This LibGDX wiki page</a>
 * @author Dalton
 */
public class Screenshots
{
    /** Variable to control screenshot naming */
    private static int counter = 1;
    
    /**
     * Takes a screenshot, and saves it to a folder in the documents directory
     */
    public static void saveScreenshot()
    {
        try
        {
            FileHandle fileHandle;
            
            do //Repeatedly increment the counter until we get to a screenshot that hasn't been saved yet
            {
                //Create the file handle.  Is there a better place to put screenshots?
                fileHandle = new FileHandle(System.getProperty("user.home") + "/RTK Screenshots/" + "screenshot-" + counter + ".png");
                counter++;
            }
            while (fileHandle.exists());
            
            Pixmap pixmap = getScreenshot(true); //Get Pixmap
            PixmapIO.writePNG(fileHandle, pixmap); //Write to the file handle
            pixmap.dispose(); //Dispose of the pixmap
            
            Gdx.app.log("Screenshots", "Saved " + fileHandle.path());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a Pixmap of the whole screen.  Make sure you dispose of it when you're done!
     * @param yDown If the screenshot should be flipped vertically or not
     * @return A pixmap of the whole screen
     */
    public static Pixmap getScreenshot(boolean yDown)
    {
        return getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), yDown);
    }
    
    /**
     * Gets a Pixmap of a section of the screen.  Make sure you dispose of it when you're done!
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param width The width
     * @param height The height
     * @param yDown If the screenshot should be flipped vertically or not
     * @return A pixmap of the section of the screen
     */
    public static Pixmap getScreenshot(int x, int y, int width, int height, boolean yDown)
    {
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, width, height);
        
        //Flip the pixmap vertically
        if (yDown)
        {
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = width * height * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = width * 4;
            
            for (int i=0; i<height; i++)
            {
                pixels.position((height - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            
            pixels.clear();
            pixels.put(lines);
        }
        
        return pixmap;
    }
}
