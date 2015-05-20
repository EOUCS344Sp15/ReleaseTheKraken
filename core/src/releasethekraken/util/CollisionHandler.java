/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.util;

import releasethekraken.entity.Entity;

/**
 * This interface provides methods of handling collision events between this
 * entity and some other entity.
 * 
 * @author Dalton
 */
public interface CollisionHandler
{
    /**
     * Called when this entity collides with another entity.  Use this to pick
     * up power ups, make bullets deal damage and despawn, etc.
     * @param other The entity collided with
     */
    public void onCollide(Entity other);
}
