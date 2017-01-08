package nl.peterbjornx.openlogiceda.util.impl;

import nl.peterbjornx.openlogiceda.util.ModificationException;
import nl.peterbjornx.openlogiceda.util.RefAllocator;

/**
 * Implements a reference allocator that does not support free and simply increments
 * a counter to provide unique references.
 */
public class SimpleRefAlloc extends RefAllocator {

    private long top = -1;

    @Override
    public void claim(long ref) {
             if ( ref >= top )
                 top = ref;
             //else
             //   ;//TODO: Warn
    }

    @Override
    public long alloc() throws ModificationException {
        if ( top == Long.MAX_VALUE )
            throw new ModificationException("Ran out of references");
        return ++top;
    }

    @Override
    public void free(long ref) {
        if ( ref < 0 )
            throw new IllegalArgumentException("ref must not be negative");
        if ( ref == top )
            top--;
    }
}
