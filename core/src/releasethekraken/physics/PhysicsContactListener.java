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
import releasethekraken.entity.Entity;
import releasethekraken.entity.EntityPowerUp;
import releasethekraken.entity.pirate.EntityPirate;
import releasethekraken.entity.projectile.EntityProjectile;
import releasethekraken.entity.seacreature.EntityPlayer;
import releasethekraken.entity.seacreature.EntitySeaCreature;
import releasethekraken.entity.seacreature.kraken.EntityKrakenGripper;
import releasethekraken.entity.seacreature.kraken.EntityKrakenTenticle;

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
        
        //Projectile collisions
        if (contactAUserData instanceof EntityProjectile || contactBUserData instanceof EntityProjectile)
        {
            boolean aIsProjectile = contactAUserData instanceof EntityProjectile;
            EntityProjectile projectile = null;
            
            if (aIsProjectile)
                projectile = (EntityProjectile)contactAUserData;
            else
                projectile = (EntityProjectile)contactBUserData;
            
            Entity other = (Entity)(aIsProjectile ? contactBUserData : contactAUserData);
            
            if (other instanceof EntitySeaCreature) //If the other entity is a sea creature
            {
                if (projectile.getOwner() instanceof EntityPirate) //Only deal damage if this projectile's owner is a pirate
                {
                    ((EntitySeaCreature)other).onDamage(projectile.getDamage());
                    projectile.onImpact();
                }
            }
            else if (other instanceof EntityPirate) //If the other entity is a pirate
            {
                if (projectile.getOwner() instanceof EntitySeaCreature) //Only deal damage if this projectile's owner is a sea creature
                {
                    ((EntityPirate)other).onDamage(projectile.getDamage());
                    projectile.onImpact();
                }
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
        
        //Kraken gripper collision damage
        if (contactAUserData instanceof EntityKrakenGripper || contactBUserData instanceof EntityKrakenGripper)
        {
            if (contactAUserData instanceof EntityPirate)
                ((EntityPirate)contactAUserData).onDamage(1);
            else if (contactBUserData instanceof EntityPirate)
                ((EntityPirate)contactBUserData).onDamage(1);
        }
        
        //Kraken tenticle collision damage
        if (contactAUserData instanceof EntityKrakenTenticle || contactBUserData instanceof EntityKrakenTenticle)
        {
            EntityKrakenTenticle tenticle;
            
            if (contactAUserData instanceof EntityKrakenTenticle)
                tenticle = (EntityKrakenTenticle) contactAUserData;
            else
                tenticle = (EntityKrakenTenticle) contactBUserData;
            
            if (tenticle.getWorld().getWorldTime() % 5 == 0) //Only deal damage once every 5 ticks
            {
                if (contactAUserData instanceof EntityPirate)
                ((EntityPirate)contactAUserData).onDamage(1);
            else if (contactBUserData instanceof EntityPirate)
                ((EntityPirate)contactBUserData).onDamage(1);
            }
        }
    }
    
}
