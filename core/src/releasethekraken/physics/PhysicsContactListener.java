/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Listens to physics contact events, handling them if 
 * 
 * @author Dalton
 */
public class PhysicsContactListener implements ContactListener
{

    public PhysicsContactListener()
    {
    }

    @Override
    public void beginContact(Contact contact)
    {
        
    }

    @Override
    public void endContact(Contact contact)
    {
        
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        
    }
    
}
