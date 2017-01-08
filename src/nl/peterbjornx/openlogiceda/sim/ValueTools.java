package nl.peterbjornx.openlogiceda.sim;/*
Part of OpenLogicEDA
Copyright (C) 2017 Peter Bosch

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import nl.peterbjornx.openlogiceda.model.Value;

/**
 * Implements utility routines to ease working with Value objects
 * @author Peter Bosch
 */
public class ValueTools {

    /**
     * Generates a strongly driven Value corresponding to state
     */
    public static Value drive(boolean state) {
        return state ? Value.HIGH : Value.LOW;
    }

    public static Value drivenCombinatorics(CombinatorialEquation eqn, Value... inputs ) {
        boolean[] bvalues = new boolean[ inputs.length ];
        for ( int i = 0; i < inputs.length; i++ ) {
            if ( !inputs[i].valid )
                return Value.UNDEFINED;
            bvalues[i] = inputs[i].truth;
        }
        return drive(eqn.compute( bvalues ));
    }
}
