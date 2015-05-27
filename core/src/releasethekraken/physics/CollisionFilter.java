/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.physics;

/**
 * Provides bit constants for Box2D collision filtering.<br>
 * <br><strong>To Use:</strong><br>
 * <br>
 * First, import this class, optionally as a static import:<br>
 * <code>import static releasethekraken.physics.CollisionFilter.*;</code><br>
 * <br>
 * There are two bit masks that you can set:<br>
 * <code>FixtureDef.filter.categoryBits</code>, specifies "what you are".<br>
 * <code>FixtureDef.filter.maskBits</code>, specifies what you collide with.<br>
 * <br>
 * You can add and subtract from what you are or collide with by using the bitwise
 * OR and XOR operators.  In Java, OR is |, and XOR is ^.<br>
 * <br><strong>Examples:</strong><br>
 * <code>fixtureDef.filter.categoryBits = COL_SEA_PROJECTILE;</code>  This specifies 
 * that the object is a sea creature projectile.<br>
 * <code>fixtureDef.filter.categoryBits = COL_PLAYER | COL_SEA_CREATURE;</code>
 * This specifies that the object is both a player and a sea creature.<br>
 * <code>fixtureDef.filter.maskBits = COL_ALL ^ (COL_SEA_CREATURE | COL_PLAYER);</code>
 * This specifies that it collides with everything EXCEPT sea creatures and players.<br>
 * <strong>Note:</strong> Since the player is specified as both a sea creature and
 * a player, The above example only really needs to collide with everything except
 * sea creatures.
 * 
 * @author Dalton
 */
public final class CollisionFilter
{
    //These constants must all be powers of two to account for all possible combinations
    
    public static final short COL_NONE                 = 0x0000; //Collide with nothing
    
    public static final short COL_DEFAULT              = 0x0001; //Default settings
    public static final short COL_PLAYER               = 0x0002;
    public static final short COL_OBSTACLE             = 0x0004;
    public static final short COL_POWERUP              = 0x0008;
    public static final short COL_SEA_CREATURE         = 0x0010;
    public static final short COL_PIRATE               = 0x0020;
    public static final short COL_SEA_PROJECTILE       = 0x0040;
    public static final short COL_PIRATE_PROJECTILE    = 0x0080;
    
    public static final short COL_ALL                  = 0x7FFF; //Collide with everything
}
