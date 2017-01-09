package nl.peterbjornx.openlogiceda.model;/*
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

/**
 * Describes the values a node can take
 * @author Peter Bosch
 */
public enum Value {

    HIGH(99, 'H', true, true),
    LOW(99, 'L', true, false),
    UNDEFINED(-99, '?', true, false),
    WEAK_HIGH(0, 'h', true, true),
    WEAK_LOW(0, 'l', true, false),
    HIGH_Z(-98, 'Z', false, false),
    CONFLICT(9001, 'E', false, false);

    private int strength;
    public final boolean valid, truth;
    private char repr;

    Value(int s, char repr, boolean valid, boolean truth) {
        strength = s;
        this.valid = valid;
        this.truth = truth;
        this.repr = repr;
    }

    public char getRepresentation() {
        return repr;
    }

    public Value combine(Value other){
        if ( other == this )
            return this;
        else if ( other.strength > strength )
            return other;
        else if ( other.strength == strength )
            return CONFLICT;
        else
            return this;
    }

}
