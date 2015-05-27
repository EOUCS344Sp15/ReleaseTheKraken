/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import releasethekraken.util.Screenshots;

/**
 * Handles various types of input
 * 
 * @author Dalton
 */
public class InputHandler implements InputProcessor
{    
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
    
    /** The array of objects that implement KeyListener */
    private Array<KeyListener> keyListeners;
    
    /** The array of objects that implement TouchListener */
    private Array<TouchListener> touchListeners;
    
    public InputHandler()
    {
        this.activePointerLocations = new Array<Vector2>();
        for (int i=0; i<20; i++)
            this.activePointerLocations.add(new Vector2());
        this.pointerLocations = new Array<Vector2>();
        for (int i=0; i<20; i++)
            this.pointerLocations.add(new Vector2());
        
        this.heldKeys = new boolean[256];
        
        this.keyListeners = new Array<KeyListener>();
        this.touchListeners = new Array<TouchListener>();
    }
    
    @Override
    public boolean keyDown(int keycode)
    {
        this.heldKeys[keycode] = true; //The key is now pressed
        
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
        }
        
        //Tell all registered KeyListeners about this
        for (KeyListener keyListener : this.keyListeners)
            keyListener.keyDown(keycode);
        
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        this.heldKeys[keycode] = false; //The key is no longer pressed
        
        //Tell all registered KeyListeners about this
        for (KeyListener keyListener : this.keyListeners)
            keyListener.keyUp(keycode);
        
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
        //Tell all registered TouchListeners about this
        for (TouchListener touchListener : this.touchListeners)
            touchListener.touchDown(screenX, screenY, pointer, button);
        
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        //Tell all registered TouchListeners about this
        for (TouchListener touchListener : this.touchListeners)
            touchListener.touchUp(screenX, screenY, pointer, button);
        
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
        
        //Handle held keys, calling keyHeld()
        for (int i=0; i<this.heldKeys.length; i++)
            if (this.heldKeys[i])
                this.keyHeld(i);            
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
        //Tell all registered TouchListeners about this
        for (TouchListener touchListener : this.touchListeners)
            touchListener.touchHeld(screenX, screenY, pointer, button);
    }
    
    /**
     * Custom method.  Like keyDown except it repeats.
     * @param keycode The keycode that was pressed
     */
    public void keyHeld(int keycode)
    {
        //Tell all registered KeyListeners about this
        for (KeyListener keyListener : this.keyListeners)
            keyListener.keyHeld(keycode);
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

    /**
     * Gets the list of KeyListeners
     * @return An Array of KeyListeners
     */
    public Array<KeyListener> getKeyListeners()
    {
        return keyListeners;
    }

    /**
     * Sets the list of KeyListeners
     * @param keyListeners The new Array of KeyListeners
     */
    public void setKeyListeners(Array<KeyListener> keyListeners)
    {
        this.keyListeners = keyListeners;
    }

    /**
     * Gets the list of TouchListeners
     * @return An Array of TouchListeners
     */
    public Array<TouchListener> getTouchListeners()
    {
        return touchListeners;
    }

    /**
     * Sets the list of TouchListeners
     * @param touchListeners The new Array of TouchListeners
     */
    public void setTouchListeners(Array<TouchListener> touchListeners)
    {
        this.touchListeners = touchListeners;
    }
    
    /**
     * Registers a KeyListener with the InputHandler
     * @param keyListener The KeyListener to register
     */
    public void registerKeyListener(KeyListener keyListener)
    {
        this.keyListeners.add(keyListener);
    }
    
    /**
     * Unregisters a KeyListener with the InputHandler
     * @param keyListener The KeyListener to unregister
     */
    public void unregisterKeyListener(KeyListener keyListener)
    {
        this.keyListeners.removeValue(keyListener, true);
    }
    
    /**
     * Registers a TouchListener with the InputHandler
     * @param touchListener The TouchListener to register
     */
    public void registerTouchListener(TouchListener touchListener)
    {
        this.touchListeners.add(touchListener);
    }
    
    /**
     * Unregisters a TouchListener with the InputHandler
     * @param touchListener The TouchListener to unregister
     */
    public void unregisterTouchListener(TouchListener touchListener)
    {
        this.touchListeners.removeValue(touchListener, true);
    }
    
    /**
     * A static inner interface that provides touch listening methods that you
     * can implement.  Implement this, and then register with InputHandler to
     * have the methods be called.
     */
    public static interface TouchListener
    {
        /**
         * Called when a pointer is clicked/touched
         * @param x The X coordinate on the screen
         * @param y The Y coordinate on the screen
         * @param pointer The pointer index that was used
         * @param button The button that was pressed
         */
        public void touchDown(int x, int y, int pointer, int button);
        
        /**
         * Called when a pointer is unclicked/untouched
         * @param x The X coordinate on the screen
         * @param y The Y coordinate on the screen
         * @param pointer The pointer index that was used
         * @param button The button that was pressed
         */
        public void touchUp(int x, int y, int pointer, int button);
        
        /**
         * Called every tick that a pointer is clicked/touched
         * @param x The X coordinate on the screen
         * @param y The Y coordinate on the screen
         * @param pointer The pointer index that was used
         * @param button The button that was pressed
         */
        public void touchHeld(int x, int y, int pointer, int button);
    }
    
    /**
     * A static inner interface that provides key listening methods that you
     * can implement.  Implement this, and then register with InputHandler to
     * have the methods be called.
     * 
     * @author Dalton
     */
    public static interface KeyListener
    {
        /**
         * Called when a key is pressed
         * @param keycode The keycode of the key that was pressed
         */
        public void keyDown(int keycode);
        
        /**
         * Called when a key is unpressed
         * @param keycode The keycode of the key that was unpressed
         */
        public void keyUp(int keycode);
        
        /**
         * Called every tick that a key is held
         * @param keycode The keycode of the key that was held
         */
        public void keyHeld(int keycode);
    }
}
