/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.path;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Arrays;

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
        
        final float scale = 1/16F; //Scale it down to match the world units
        
        //Scale the polyline to match the world units.
        //This has to be done manually because Polyline.setScale() doesn't seem to work.
        float[] vertices = polyline.getTransformedVertices();
        for (int i=0; i<vertices.length; i++)
            vertices[i] *= scale;
        polyline = new Polyline(vertices);
        
        this.polyline = polyline;
        
        vertices = polyline.getVertices(); //Get an array of vertices, in x1, y1, x2, y2,... format
        Vector2[] points = new Vector2[vertices.length/2]; //Make an array of Vector2 objects
        
        //Convert the vertices to Vector2 objects
        for (int i=0; i<points.length; i++)
        {
            float x = vertices[i*2];
            float y = vertices[(i*2)+1];
            points[i] = new Vector2(x, y);
        }
        
        //Make the smooth path with the points
        this.path = new CatmullRomSpline<Vector2>(points, false);
        
        Gdx.app.log("SCPath " + this.toString(true), "Points: " + Arrays.toString(points));
    }
    
    /**
     * Fills the array with this path, and all paths connected to it in some way.
     * This method is recursive, as it calls itself on the parent path and all
     * next paths.
     * @param connectedPaths The list (preferably empty) to fill with a list of connected paths
     */
    public void getAllConnectedPaths(Array<SeaCreaturePath> connectedPaths)
    {
        //Add this path to the list if it isn't already there
        if (!connectedPaths.contains(this, true))
            connectedPaths.add(this);
        else
            return; //If this is already added, then it's already been called on this
        
        //Add parent's connected paths
        if (this.parent != null)
            this.parent.getAllConnectedPaths(connectedPaths);
        
        //Add next path's connected paths
        for (SeaCreaturePath nextPath : this.nextPaths)
            if (nextPath != null)
                nextPath.getAllConnectedPaths(connectedPaths);
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
    
    /**
     * Gets the path's Polyline
     * @return The path's Polyline
     */
    public Polyline getPolyline()
    {
        return this.polyline;
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
