/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import releasethekraken.ui.GameRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import releasethekraken.entity.Entity;
import releasethekraken.entity.projectile.EntityProjectile;
import releasethekraken.entity.projectile.EntitySeaShell;
import releasethekraken.entity.seacreature.EntityFish;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.ui.InteractiveUiObject;
import releasethekraken.ui.UiButton;
import releasethekraken.ui.UiObject;
import releasethekraken.util.Screenshots;

/**
 * Handles various types of input
 * 
 * @author Dalton
 */
public class InputHandler implements InputProcessor
{
    //The input handler's private references to the game world and renderer
    private GameWorld world;
    private GameRenderer renderer;
    
    /** 
     * A debugging position that is controllable with the numberpad 8 4 5 6 keys.
     * Use as an offset for positioning UI stuff.
     */
    public static final Vector2 DEV_POS = new Vector2(0, 0);
    
    //The array of screen activePointerLocations.  For a mouse, only the first one will be active
    private Array<Vector2> activePointerLocations;
    
    //The array of screen pointer locations.  For a mouse, only the first one will be active
    private Array<Vector2> pointerLocations;
    
    /** An array of held keys.  Keycode = index */
    private boolean[] heldKeys;
    
    public InputHandler(GameWorld world, GameRenderer renderer)
    {
        this.world = world;
        this.renderer = renderer;
        this.activePointerLocations = new Array<Vector2>();
        for (int i=0; i<20; i++)
            this.activePointerLocations.add(new Vector2());
        this.pointerLocations = new Array<Vector2>();
        for (int i=0; i<20; i++)
            this.pointerLocations.add(new Vector2());
        
        this.heldKeys = new boolean[256];
    }
    
    @Override
    public boolean keyDown(int keycode)
    {
        this.heldKeys[keycode] = true; //The key is now pressed
        EntityPlayer player = this.world.getPlayer();
        
        switch(keycode)
        {
        case Input.Keys.NUMPAD_8:
            DEV_POS.add(0, 1);
            break;
        case Input.Keys.NUMPAD_4:
            DEV_POS.add(-1, 0);
            break;
        case Input.Keys.NUMPAD_5:
            DEV_POS.add(0, -1);
            break;
        case Input.Keys.NUMPAD_6:
            DEV_POS.add(1, 0);
            break;
        case Input.Keys.F12: //Screenshot
            Screenshots.saveScreenshot();
            break;
            
        //Player projectiles
        case Input.Keys.SPACE:
            Vector2 velocity = player.getAimPos().cpy().sub(player.getPos()).nor().scl(500); //Calculate direction and velocity to fire at
            float spread = 10F; //The amount of possible spread, in degrees
            velocity.rotate(this.world.random.nextFloat()*spread - spread/2); //Add +- spread/2 degrees of spread
            new EntitySeaShell(this.world, player.getPos().x, player.getPos().y, velocity.x, velocity.y, player);
            break;
        }
        
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        this.heldKeys[keycode] = false; //The key is no longer pressed
        
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        boolean handled = true;
        
        for (UiObject obj : this.renderer.uiObjects) //Iterate over all UI Objects
        {
            if (obj instanceof UiButton)
                if (((UiButton)obj).isInBounds(screenX, screenY)) //If the click was within the button's bounds, call onClick()
                {
                    ((UiButton)obj).onClick(button, this.world);
                }
        }
        return handled;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
    
    public void update()
    {
        this.updateActivePointerLocations();
        
        //Update pointer states
        for (int i=0; i<20; i++)
        {
            //Call touchHeld() for active pointers
            int touchX = (int)this.activePointerLocations.get(i).x;
            int touchY = (int)this.activePointerLocations.get(i).y;
            if (touchX >= 0 && touchY >= 0)
                this.touchHeld(touchX, touchY, i, 0);
        }
        
        //Handle pointer hovers, but only for the first one (for now)
        for (UiObject obj : this.renderer.uiObjects)
        {
            if (obj instanceof InteractiveUiObject)
            {
                Vector2 pointer0 = this.getPointerLocations().get(0);
                
                //Call onHover() if the mouse is in the bounds of the object
                if (((InteractiveUiObject)obj).isInBounds(pointer0.x, pointer0.y))
                {
                    ((InteractiveUiObject)obj).onHover(pointer0.x, pointer0.y);
                }
                else if (((InteractiveUiObject)obj).isHoverActive()) //If the object was still being hovered over, call onLeaveHover()
                {
                    ((InteractiveUiObject)obj).onLeaveHover();
                } 
            }
        }
        
        //Handle held keys, calling keyHeld()
        for (int i=0; i<this.heldKeys.length; i++)
            if (this.heldKeys[i])
                this.keyHeld(i);
        
        //Update player's aim position
        Vector3 mousePos3D = new Vector3(this.pointerLocations.first(), 0); //Convert mouse 0 to Vector 3
        Vector3 worldMousePos3D = this.renderer.getCamera().unproject(mousePos3D); //Have the camera unproject the coordinates
        this.world.getPlayer().getAimPos().x = worldMousePos3D.x;
        this.world.getPlayer().getAimPos().y = worldMousePos3D.y;
            
    }
    
    /**
     * Custom method.  Like touchDown except it repeats.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button Not Yet Implemented
     */
    public void touchHeld(int screenX, int screenY, int pointer, int button)
    {
        for (UiObject obj : this.renderer.uiObjects) //Iterate over all UI Objects
        {
            if (obj instanceof UiButton)
                if (((UiButton)obj).isInBounds(screenX, screenY)) //If the click was within the button's bounds, call onClick()
                {
                    ((UiButton)obj).onClickHeld(button, this.world);
                }
        }
    }
    
    /**
     * Custom method.  Like keyDown except it repeats.
     * @param keycode The keycode that was pressed
     */
    public void keyHeld(int keycode)
    {
        EntityPlayer player = this.world.getPlayer();
        
        switch (keycode)
        {
            case Input.Keys.UP:
            case Input.Keys.W:
                //if (player.getPhysBody().getLinearVelocity().y < player.maxSpeed)
                    player.getPhysBody().applyForceToCenter(0, player.moveForce, true);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                //if (player.getPhysBody().getLinearVelocity().y > 0 - player.maxSpeed)
                    player.getPhysBody().applyForceToCenter(0, -player.moveForce, true);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                //if (player.getPhysBody().getLinearVelocity().x > 0 - player.maxSpeed)
                    player.getPhysBody().applyForceToCenter(-player.moveForce, 0, true);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                //if (player.getPhysBody().getLinearVelocity().x < player.maxSpeed)
                    player.getPhysBody().applyForceToCenter(player.moveForce, 0, true);
                break;
                
        }
    }
    
    /**
     * Gets an array of touch/mouse coordinates that are currently pressed.
     */
    public Array<Vector2> getActivePointerLocations()
    {
        this.updateActivePointerLocations();
        return this.activePointerLocations;
    }
    
    /**
     * Updates the list of active pointer locations
     */
    public void updateActivePointerLocations()
    {
        for (int i=0; i<20; i++)
            if (Gdx.input.isTouched(i))
                this.activePointerLocations.get(i).set(Gdx.input.getX(i), Gdx.input.getY(i)); //Add touch data
            else
                this.activePointerLocations.get(i).set(-1000, -1000); //Reset any previous touch data
    }
    
    /**
     * Gets an array of pointer coordinates.
     */
    public Array<Vector2> getPointerLocations()
    {
        this.updatePointerLocations();
        return this.pointerLocations;
    }
    
    /**
     * Updates the list of pointer locations
     */
    public void updatePointerLocations()
    {
        for (int i=0; i<20; i++)
            this.pointerLocations.get(i).set(Gdx.input.getX(i), Gdx.input.getY(i)); //Add touch data
    }
}
