/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import releasethekraken.entity.Entity;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.path.SeaCreaturePath;

/**
 * Loads a level from a .tmx file.
 * 
 * @author Dalton
 */
public class LevelLoader
{
    /** The name of the level */
    private String levelName;
    
    /** The HashMap to store the entity tile IDs */
    private HashMap<Integer, Class<? extends Entity>> entityTileIDs = new HashMap<Integer, Class<? extends Entity>>();
    
    /**
     * Constructs a new level loader for the specified level.
     * @param levelName The name of the level to load
     */
    public LevelLoader(String levelName)
    {
        this.levelName = levelName;
    }
    
    /**
     * Parses the level file and constructs a game world from it, and returns it.
     * Throws an exception if loading failed.
     * @return The newly loaded GameWorld
     */
    public GameWorld loadWorld()
    {
        Gdx.app.log("LevelLoader", "Loading level \"" + this.levelName + "\"");
        
        GameWorld newWorld = new GameWorld();
        
        //Make a new TiledMap
        TiledMap map = new TmxMapLoader().load(this.levelName + ".tmx");
        
        float unitScale = 16.0F; //16 pixels = 1 meter
        
        //Get the map properties
        MapProperties properties = map.getProperties();
        
        //Load world properties
        newWorld.setName(properties.get("levelname", String.class));
        newWorld.setPointsForKraken(Integer.parseInt(properties.get("krakenpoints", String.class)));
        newWorld.setWidth(properties.get("width", Integer.class)*2);
        newWorld.setHeight(properties.get("height", Integer.class)*2);
        newWorld.setTiledMap(map);
        newWorld.setTiledMapUnitScale(unitScale);
        
        //Load Entity Tile Data
        Gdx.app.log("LevelLoader", "Parsing entity tile data");
        
        TiledMapTileSet entityTileSet = map.getTileSets().getTileSet("Entities");
        Iterator<TiledMapTile> entityTileIterator = entityTileSet.iterator();
        
        while (entityTileIterator.hasNext()) //Iterate through entity tiles
        {
            TiledMapTile tile = entityTileIterator.next();
            String type = tile.getProperties().get("type", String.class);
            
            if (type == null) //Only check tiles that have a type set
                continue;
            
            //See if there is an entity class for a given name
            Class<? extends Entity> entityClass = ReleaseTheKraken.getEntityFromName(type);
            
            //If there is, register the ID
            if (entityClass != null)
                this.entityTileIDs.put(tile.getId(), entityClass);
            
            Gdx.app.log("LevelLoader", "Tile ID: " + tile.getId() + " type: " + type);
        }
        
        //Load world entities
        Gdx.app.log("LevelLoader", "Parsing world entities");
        MapLayer entityLayer = map.getLayers().get("entities");
        
        //If the layer doesn't exist, throw an exception
        if (entityLayer == null)
            throw new NullPointerException("entities layer is null");
        
        MapObjects entityObjects = entityLayer.getObjects();
        Iterator<MapObject> entityObjIterator = entityObjects.iterator();
        
        while (entityObjIterator.hasNext()) //Iterate over entities on the map
        {
            MapObject entityObject = entityObjIterator.next();
            
            //Gdx.app.log("LevelLoader", "Parsing map object: " + entityObject);
            
            if (entityObject instanceof TextureMapObject) //If the mapObject is of the correct type
            {
                //Get the entity class from the mapObject's tile ID
                int gid = ((TextureMapObject)entityObject).getProperties().get("gid", Integer.class);
                Class<? extends Entity> entityClass = this.entityTileIDs.get(gid);
                
                if (entityClass != null)
                {
                    Gdx.app.log("LevelLoader", "Attempting to spawn " + entityClass.getSimpleName());
                    
                    try
                    {
                        //Construct a new entity by finding the correct constructor from its class, and reflectively instantiating it.  Magic!
                        Constructor<? extends Entity> constructor = entityClass.getConstructor(GameWorld.class, TextureMapObject.class);
                        Entity entity = constructor.newInstance(newWorld, entityObject);
                        newWorld.addEntity(entity); //Add the entity to the world
                        
                        if (entity instanceof EntityPlayer) //Set the player if the entity is the player
                            newWorld.setPlayer((EntityPlayer)entity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        //If you thought entity loading was complicated, wait until you see what's next: paths!
        
        //Load world paths
        Gdx.app.log("LevelLoader", "Parsing world paths");
        MapLayer pathLayer = map.getLayers().get("paths");
        
        //If the layer doesn't exist, throw an exception
        if (pathLayer == null)
            throw new NullPointerException("paths layer is null");
        
        MapObjects pathObjects = pathLayer.getObjects();
        Iterator<MapObject> pathObjIterator = pathObjects.iterator();
        
        //Create an IntMap to map integer path IDs to paths
        IntMap<SeaCreaturePath> pathMap = new IntMap<SeaCreaturePath>();
        //Create an IntMap to map integer path IDs to arrays of next path IDs
        IntMap<int[]> nextPathsMap = new IntMap<int[]>();
        
        while (pathObjIterator.hasNext()) //Iterate over paths on the map
        {
            MapObject pathObject = pathObjIterator.next();
            
            //Gdx.app.log("LevelLoader", "Parsing map object: " + pathObject);
            
            if (pathObject instanceof PolylineMapObject) //If the mapObject is of the correct type
            {
                PolylineMapObject polylineMapObject = (PolylineMapObject) pathObject;
                
                int pathID = polylineMapObject.getProperties().get("id", Integer.class);
                
                Gdx.app.log("LevelLoader", "Attempting to load path with ID: " + pathID);
                
                //Get and parse the list of next paths
                String nextPaths = polylineMapObject.getProperties().get("next", String.class);
                String[] nextPathIDs = nextPaths.split(",");
                
                //Make an array of next path integer IDs
                int[] nextPathIntIDs = new int[nextPathIDs.length];
                
                //Parse the integer IDs from the string IDs
                for (int i=0; i<nextPathIDs.length; i++)
                    if (!nextPathIDs[i].equals(""))
                        nextPathIntIDs[i] = Integer.parseInt(nextPathIDs[i]);
                
                //Create a new SeaCreaturePath object
                SeaCreaturePath seaCreaturePath = new SeaCreaturePath(pathID, null, null, polylineMapObject.getPolyline());
                
                //Add the SeaCreaturePath object and its next paths to the IntMaps
                pathMap.put(pathID, seaCreaturePath); //Add the path to the IntMap so that it can be connected later
                nextPathsMap.put(pathID, nextPathIntIDs); //Add the next paths to the IntMap so that the paths can be connected later
            }
        }
        
        //Gdx.app.log("LevelLoader", "Done creating paths");
        //Gdx.app.log("LevelLoader", "pathMap: " + pathMap);
        //Gdx.app.log("LevelLoader", "nextPathsMap: " + nextPathsMap);
        
        //Now that all of the SeaCreaturePaths are created, it's time to connect them
        
        SeaCreaturePath firstPath = null; //The first path, which will be the one added to the world
        Iterator<IntMap.Entry<SeaCreaturePath>> pathIterator = pathMap.iterator();
        
        //Iterate over each path, connecting it to the other paths it should be connected to
        while (pathIterator.hasNext())
        {
            IntMap.Entry<SeaCreaturePath> currentPathEntry = pathIterator.next();
            
            int pathID = currentPathEntry.key;
            SeaCreaturePath currentPath = currentPathEntry.value;
            
            //Create the next paths list
            Array<SeaCreaturePath> nextPaths = new Array<SeaCreaturePath>();
            
            //Get the array of next path IDs
            int[] nextPathIDs = nextPathsMap.get(pathID);
            
            //Connect the paths
            for (int i=0; i<nextPathIDs.length; i++)
            {
                nextPaths.add(pathMap.get(nextPathIDs[i])); //Add the path to the list of next paths
                
                if (pathMap.get(nextPathIDs[i]) != null)
                    pathMap.get(nextPathIDs[i]).setParent(currentPath); //Tell the next path that this current path is the parent
                //BUG: Each next path overwrites this, only the last "next path" gets to be the parent
            }
            
            currentPath.setNextPaths(nextPaths); //Set the next paths list for the path
            
            //Set the first path to the path that has a null parent
            if (currentPath.getParent() == null)
                firstPath = currentPath;
            //BUG: Only the last path that has a null parent becomes the first path.  
            //This might not be a problem, as only one path should ever have a null parent.
        }
        
        newWorld.setFirstPath(firstPath); //Set the first path in the world
        
        Gdx.app.log("LevelLoader", "Successfully loaded the world!");
        Gdx.app.log("LevelLoader", newWorld.toString());
        
        return newWorld;
    }
}
