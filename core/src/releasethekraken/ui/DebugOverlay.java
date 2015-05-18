/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import releasethekraken.GameAssets;
import releasethekraken.GameWorld;
import releasethekraken.InputHandler;

/**
 * Draws some useful debugging information on the screen.
 * @author Dalton
 */
public class DebugOverlay extends UiObject
{
    private static final Array<DebugEntry> debugData;
    
    private static final DebugEntry version1; //DebugEntry to hold the version info
    private static final DebugEntry version2; //DebugEntry to hold the version info
    private static final DebugEntry fpsEntry; //DebugEntry to hold the FPS
    private static final DebugEntry javaHeap; //DebugEntry to hold the Java Heap
    private static final DebugEntry nativeHeap; //DebugEntry to hold the Native Heap
    private static final DebugEntry worldStats1; //DebugEntry to hold the world stats
    private static final DebugEntry worldStats2; //DebugEntry to hold the world stats
    private static final DebugEntry worldStats3; //DebugEntry to hold the world stats
    private static final DebugEntry devPos; //DebugEntry to hold the developer position point
    private static final DebugEntry playerStats1; //DebugEntry to hold the player stats
    private static final DebugEntry playerStats2; //DebugEntry to hold the player stats
    
    static //Initialize debug info list
    {
        debugData = new Array<DebugEntry>();
        debugData.add(new DebugEntry("Debug Info"));
        
        debugData.add(version1 = new DebugEntry("Java: " + System.getProperty("java.version")
                + " " + System.getProperty("sun.arch.data.model") + " bit"));
        debugData.add(version2 = new DebugEntry("OS: " + System.getProperty("os.name")
                + " " + System.getProperty("os.arch")));
        
        debugData.add(fpsEntry = new DebugEntry());
        debugData.add(javaHeap = new DebugEntry());
        debugData.add(nativeHeap = new DebugEntry());
        debugData.add(worldStats1 = new DebugEntry());
        debugData.add(worldStats2 = new DebugEntry());
        debugData.add(worldStats3 = new DebugEntry());
        debugData.add(devPos = new DebugEntry());
        debugData.add(playerStats1 = new DebugEntry());
        debugData.add(playerStats2 = new DebugEntry());
    }
    
    public DebugOverlay(GameRenderer renderer)
    {
        super(renderer,
                Gdx.graphics.getWidth() - 0.2323F*Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight() - 0.06F*Gdx.graphics.getHeight(),
                0.2F*Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
    }
    
    @Override
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        if (this.renderer.debugScreenVisible)
        {
            //shapeRenderer.setColor(Color.valueOf("0000FF"));
            //shapeRenderer.rect(this.x, this.y - this.height, this.width, this.height);
        }
    }
    
    @Override
    public void renderSprites(SpriteBatch batch)
    {
        if (this.renderer.debugScreenVisible)
            GameAssets.fontDebug.draw(batch, debugData.toString("\n"), this.x, this.y, this.width, -1, true);
    }
    
    @Override
    public void update(GameWorld world)
    {
        if (this.renderer.debugScreenVisible)
        {
            fpsEntry.data = String.format("%-12.12s : %-6s", "FPS", Gdx.graphics.getFramesPerSecond());
            javaHeap.data = String.format("%-12.12s : %-6.3f MB", "Java Heap", Gdx.app.getJavaHeap()/1024F/1024F);
            nativeHeap.data = String.format("%-12.12s : %-6.3f MB", "Native Heap", Gdx.app.getNativeHeap()/1024F/1024F);
            worldStats1.data = "Level: " + world.getName();
            worldStats2.data = String.format("%-12.12s : %-6s", "World Time", world.getWorldTime());
            worldStats3.data = String.format("%-12.12s : %-6s", "Entities", world.getEntitites().size);
            devPos.data = String.format("Dev Pos: x:%-3.0f y:%-3.0f", InputHandler.DEV_POS.x, InputHandler.DEV_POS.y);
            playerStats1.data = String.format("Player Pos: X:%-2.1f Y:%-2.1f", world.getPlayer().getPos().x, world.getPlayer().getPos().y);
            playerStats2.data = String.format("Player Vel: X:%-2.1f Y:%-2.1f m/s", world.getPlayer().getVel().x, world.getPlayer().getVel().y);
        }
    }
    
    /**
     * Adds a DebugEntry to the list of debug info to show. Once added, any
     * changes to the DebugEntry will cause the entry in the debug overlay
     * to update.  Use this to add your own debug entries.
     * @param entry 
     */
    public static void addEntry(DebugEntry entry)
    {
        if (!debugData.contains(entry, true))
            debugData.add(entry);
    }
    
    /**
     * Removes a DebugEntry from the list of debug info to show.  
     * @param entry 
     */
    public static void removeEntry(DebugEntry entry)
    {
        debugData.removeValue(entry, true);
    }
    
    /**
     * Static inner class representing an entry in the debug overlay.  Contains
     * a string for data, which can be publicly modified.
     */
    public static class DebugEntry
    {
        public String data;
        
        public DebugEntry()
        {
            this.data = "Unchanged";
        }
        
        public DebugEntry(String data)
        {
            this.data = data;
        }
        
        @Override
        public String toString()
        {
            return data;
        }
    }
}
