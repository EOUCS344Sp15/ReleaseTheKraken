/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.path;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.utils.Array;

/**
 * Represents a path that sea creatures can go on.  Can end with 0 - n branching
 * paths that the player can choose to send sea creatures down.  
 * @author Dalton
 */
public class SeaCreaturePath
{
    /** The path id, from the level editor */
    public final int id;
    /** The path that leads to this path, or null if this is the first path */
    private SeaCreaturePath parent;
    /** A list of paths that can be gone on after this path.  The list is empty if there are none. */
    private Array<SeaCreaturePath> nextPaths;
    /** The LibGDX polyline object that the path object will be generated from */
    private Polyline polyline;
    /** The LibGDX path object that represents the actual path */
    private Path path;
    
    /**
     * Constructs a new SeaCreaturePath
     * @param id The path's ID, from the level editor
     * @param parent The path that leads to this path, or null if this is the first path
     * @param nextPaths A list of paths that can be gone on after this path.  The list is empty if there are none.
     * @param polyline The Polyline object that holds the path data
     */
    public SeaCreaturePath(int id, SeaCreaturePath parent, Array<SeaCreaturePath> nextPaths, Polyline polyline)
    {
        this.id = id;
        this.parent = parent;
        this.nextPaths = nextPaths;
        this.polyline = polyline;
    }

    /**
     * Gets the parent path
     * @return The parent path
     */
    public SeaCreaturePath getParent()
    {
        return parent;
    }

    /**
     * Sets the parent path
     * @param parent The parent path
     */
    public void setParent(SeaCreaturePath parent)
    {
        this.parent = parent;
    }

    /**
     * Gets the list of the next paths
     * @return The list of the next paths
     */
    public Array<SeaCreaturePath> getNextPaths()
    {
        return nextPaths;
    }

    /**
     * Sets the list of the next paths
     * @param nextPaths The list of the next paths
     */
    public void setNextPaths(Array<SeaCreaturePath> nextPaths)
    {
        this.nextPaths = nextPaths;
    }
    
    @Override
    public String toString()
    {
        return this.toString(false);
    }
    
    /**
     * Alternate toString, with an option to specify if the information displayed
     * is simple or not
     * @param simple If the returned string should be simple or not
     * @return The string representation of the object
     */
    public String toString(boolean simple)
    {
        if (simple)
            return "" + this.id;
        else
        {
            String parentPathStr = null;
            if (this.parent != null)
                parentPathStr = this.parent.id + "";
            
            //Generate a list of next path IDs
            String nextPathsStr = "";
            for (SeaCreaturePath nextPath : this.nextPaths)
                if (nextPath != null)
                    nextPathsStr += " " + nextPath.toString(true);
                else
                    nextPathsStr += " null";
            
            return this.getClass().getSimpleName() + "[ID: " + this.id + " ParentID: " + parentPathStr + " NextPathIDs:" + nextPathsStr + "]";
        }
    }
}
