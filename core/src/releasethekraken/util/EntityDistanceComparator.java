/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package releasethekraken.util;

import java.util.Comparator;
import releasethekraken.entity.Entity;

/**
 * Compares the distance between two entities and a target entity.
 * 
 * @author Dalton
 * @param <E> The entity to compare distances to
 */
public class EntityDistanceComparator<E extends Entity> implements Comparator<E>
{
    /** The source entity to compare distances to */
    private final Entity source;
    
    /**
     * Constructs a new EntityDistance comparator
     * @param source The source entity to compare distances to
     */
    public EntityDistanceComparator(Entity source)
    {
        this.source = source;
    }
    
    @Override
    public int compare(E ent1, E ent2)
    {
        return (int) (ent1.getPos().dst(this.source.getPos())
                - ent2.getPos().dst(this.source.getPos()));
    }
}
