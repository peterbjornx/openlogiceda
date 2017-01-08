package nl.peterbjornx.openlogiceda.util;

import java.io.Serializable;

/**
 * Provides an algorithm for allocating references.
 */
public abstract class RefAllocator implements Serializable {

    /**
     * Claims a reference, that is, mark it as already used and prevent it from being issued by alloc
     * @param ref The reference to claim.
     */
    public abstract void claim( long ref );

    /**
     * Allocates a reference.
     * @return A new project-wide unique reference.
     */
    public abstract long alloc() throws ModificationException;

    /**
     * Marks a reference as no longer used.
     * @param ref The reference to release.
     */
    public abstract void free( long ref );

}
