/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Used for a Box2D ray cast to determine if the raycast collided with a fixture,
 * based on the specified collision bits.
 * @author Dalton
 */
public class RayCastCallbackCollider implements RayCastCallback
{
    /** The collision bits */
    public final short collisionBits;
    /** If the ray trace was blocked */
    private boolean blocked;
    
    /**
     * Constructor
     * @param collisionBits The collision bits
     */
    public RayCastCallbackCollider(short collisionBits)
    {
        this.collisionBits = collisionBits;
        this.blocked = false;
    }
    
    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction)
    {
        short fixtureBits = fixture.getFilterData().categoryBits;
        
        /* //Debugging code
        System.out.println();
        Gdx.app.log(this.getClass().getSimpleName(), "Ray trace encounted fixture: " + fixture);
        Gdx.app.log(this.getClass().getSimpleName(), "Fixture's body's userdata: " + fixture.getBody().getUserData());
        Gdx.app.log(this.getClass().getSimpleName(), "Collided fixture bits: " + Integer.toBinaryString(fixtureBits));
        Gdx.app.log(this.getClass().getSimpleName(), "Trace fixture bits: " + Integer.toBinaryString(this.collisionBits));
        Gdx.app.log(this.getClass().getSimpleName(), "It & Me: " + Integer.toBinaryString(fixtureBits & this.collisionBits));
        Gdx.app.log(this.getClass().getSimpleName(), "It | Me: " + Integer.toBinaryString(fixtureBits | this.collisionBits));
        Gdx.app.log(this.getClass().getSimpleName(), "It ^ Me: " + Integer.toBinaryString(fixtureBits ^ this.collisionBits));
        System.out.println();
        */
        
        if ((fixtureBits & this.collisionBits) == 0)
        {            
            //No collision
            return -1;
        }
        else
        {
            //Collision
            this.blocked = true;
            return 0;
        }
    }
    
    /**
     * Returns true if the ray trace was blocked
     * @return True if the ray trace was blocked
     */
    public boolean wasBlocked()
    {
        return this.blocked;
    }
}
