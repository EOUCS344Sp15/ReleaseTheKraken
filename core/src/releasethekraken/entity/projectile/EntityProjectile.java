/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.entity.projectile;

import releasethekraken.GameWorld;
import releasethekraken.entity.Entity;

/**
 * A base projectile class that can be extended to provide unique projectile types.
 * Has an owner (which can be null) which will determine what entities it collides with.
 * 
 * @author Dalton
 */
public class EntityProjectile extends Entity
{
    /** The owner of the projectile, can be null */
    protected Entity owner;
    /** How much damage the projectile does */
    protected int damage;
    /** How long, in ticks, the projectile has left before it "dies".  If set to -1, it will never die */
    protected long despawnTimer;
    /** The minimum velocity of the projectile.  It will despawn when going less than this speed.  Units are m/s */
    protected float minVel = 0.0F;
    
    /**
     * Constructs a new projectile
     * @param world The world that the projectile is in
     * @param xLoc The X location of the projectile
     * @param yLoc The Y location of the projectile
     * @param xVel The X velocity of the projectile
     * @param yVel The Y velocity of the projectile
     * @param owner The owner of the projectile, or null
     * @param damage The damage that the projectile does
     */
    public EntityProjectile(GameWorld world, float xLoc, float yLoc, float xVel, float yVel, Entity owner, int damage)
    {
        super(world, xLoc, yLoc);
        this.owner = owner; //Sets the owner of the projectile
        this.despawnTimer = -1; //Default despawn timer (never despawns)
        this.damage = damage;
        //Call spawnInWorld() in subclasses!
    }
    
    @Override
    protected void spawnInWorld(float x, float y, float xVel, float yVel)
    {
        //TODO: Copy and modify the code from EntityPlayer
    }
    
    @Override
    public void update()
    {
        super.update();
        this.despawnTimer--; //Update despawn timer
        
        if (this.despawnTimer == 0) //Destroy the projectile if the timer reaches 0
            this.onDespawn();
        
        if (this.getVel().len() < this.minVel) //Destroy the projectile if it goes slower than the minimum speed
            this.onDespawn();
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
    
    /**
     * Gets the amount of damage that the projectile deals
     * @return The integer amount of damage that the projectile deals
     */
    public int getDamage()
    {
        return this.damage;
    }
    
    /**
     * Called when the projectile impacts something
     */
    public void onImpact()
    {
        this.dispose();
    }
    
    /**
     * Called when the projectile despawns from existing too long
     */
    public void onDespawn()
    {
        this.dispose();
    }
}
