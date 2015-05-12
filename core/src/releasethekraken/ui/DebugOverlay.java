/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.ui;

import com.badlogic.gdx.utils.Array;

/**
 * Draws some useful debugging information on the screen.
 * @author Dalton
 */
public class DebugOverlay extends UiObject
{
    private Array<String> debugData;
    
    public DebugOverlay(GameRenderer renderer, float x, float y, float width, float height)
    {
        super(renderer, x, y, width, height);
        
        this.debugData = new Array<String>();
        this.debugData.add("Debug Info");
    }
    
    
}
