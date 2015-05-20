/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.projectile;

import releasethekraken.util.CollisionHandler;
import releasethekraken.GameWorld;
import releasethekraken.entity.Entity;
import releasethekraken.entity.pirate.EntityPirate;
import releasethekraken.entity.seacreature.EntitySeaCreature;

/**
 * A base projectile class that can be extended to provide unique projectile types.
 * Has an owner (which can be null) which will determine what entities it collides with.
 * 
 * @author Dalton
 */
public class EntityProjectile extends Entity implements CollisionHandler
{
    /** The owner of the projectile, can be null */
    private Entity owner;
    /** How much damage the projectile does */
    private int damage;
    /** How long, in ticks, the projectile has left before it "dies".  If set to -1, it will never die */
    private long despawnTimer;
    
    /**
     * Constructs a new projectile
     * @param world The world that the projectile is in
     * @param xLoc The X location of the projectile
     * @param yLoc The Y location of the projectile
     * @param xVel The X velocity of the projectile
     * @param yVel The Y velocity of the projectile
     * @param owner The owner of the projectile, or null
     */
    public EntityProjectile(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner)
    {
        super(world, xLoc, yLoc);
        this.vel.set(xVel, yVel); //Set the velocity
        this.owner = owner; //Sets the owner of the projectile
        this.despawnTimer = -1; //Default despawn timer (never despawns)
        this.damage = 5; //Default damage
    }
    
    @Override
    public void update()
    {
        super.update();
        this.despawnTimer--; //Update despawn timer
        
        if (this.despawnTimer == 0) //Destroy the projectile if the timer reaches 0
            this.dispose();
    }

    /**
     * Gets the owner of the projectile, which can be null
     * @return The owner of the projectile
     */
    public Entity getOwner()
    {
        return owner;
    }

    /**
     * Sets the owner of the projectile, which can be null
     * @param owner The new owner of the projectile
     */
    public void setOwner(Entity owner)
    {
        this.owner = owner;
    }

    /**
     * Gets the amount of time, in ticks, that is left before the projectile despawns
     * @return The time, in ticks, before the projectile despawns
     */
    public long getDespawnTimer()
    {
        return despawnTimer;
    }

    /**
     * Sets the amount of time, in ticks, that is left before the projectile despawns
     * @param despawnTimer The time, in ticks, before the projectile despawns
     */
    public void setDespawnTimer(long despawnTimer)
    {
        this.despawnTimer = despawnTimer;
    }

    @Override
    public void onCollide(Entity other)
    {
        if (other == this.owner) //Ignore collisions with the projectile's owner
            return;
        
        if (other instanceof EntitySeaCreature) //If the other entity is a sea creature
        {
            if (this.owner instanceof EntityPirate) //Only deal damage if this projectile's owner is a pirate
            {
                ((EntitySeaCreature)other).onDamage(this.damage);
                this.dispose();
            }
        }
        else if (other instanceof EntityPirate) //If the other entity is a pirate
        {
            if (this.owner instanceof EntitySeaCreature) //Only deal damage if this projectile's owner is a sea creature
            {
                ((EntityPirate)owner).onDamage(this.damage);
                this.dispose();
            }
        }
        else
            this.dispose(); //Collide with anything else
    }
}
