/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.seacreature.EntityPlayer;

/**
 * Listens to physics contact events, handling them if required
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
        Fixture contactA = contact.getFixtureA();
        Fixture contactB = contact.getFixtureB();
        Body contactABody = contactA.getBody();
        Body contactBBody = contactB.getBody();
        Object contactAUserData = null;
        Object contactBUserData = null;
        
        if (contactABody != null)
            contactAUserData = contactABody.getUserData();
        if (contactBBody != null)
            contactBUserData = contactBBody.getUserData();
        
        Gdx.app.debug("PhysicsContactListener", "beginContact()");
        Gdx.app.debug("PhysicsContactListener", "UserData A: " + contactAUserData);
        Gdx.app.debug("PhysicsContactListener", "UserData B: " + contactBUserData);
        
        //If one of the contacts is an EntityPlayer
        if (contactAUserData instanceof EntityPlayer || contactBUserData instanceof EntityPlayer)
        {
            Gdx.app.debug("PhysicsContactListener", "Player colliding with something!");
            
            //If one of the contacts is an EntityPowerUp
            if (contactAUserData instanceof EntityPowerUp || contactBUserData instanceof EntityPowerUp)
            {
                EntityPowerUp powerUp;
                
                //Set powerUp to whichever one is the power up
                if (contactAUserData instanceof EntityPowerUp)
                    powerUp = (EntityPowerUp)contactAUserData;
                else
                    powerUp = (EntityPowerUp)contactBUserData;
                
                powerUp.onPickUp(); //Tell powerUp about the collision
            }
        }
        
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
