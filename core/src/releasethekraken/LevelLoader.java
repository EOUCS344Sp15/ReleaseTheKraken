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
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import releasethekraken.entity.Entity;

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
        newWorld.setWidth(properties.get("width", Integer.class));
        newWorld.setHeight(properties.get("height", Integer.class));
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
                        
                        //if (entity instanceof EntityPlayer) //Set the player if the entity is the player
                        //    newWorld.setPlayer((EntityPlayer)entity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        Gdx.app.log("LevelLoader", "Successfully loaded the world!");
        
        return newWorld;
    }
}
