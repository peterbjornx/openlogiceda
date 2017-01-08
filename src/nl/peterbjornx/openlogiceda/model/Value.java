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

    HIGH(99, true, true),
    LOW(99, true, false),
    UNDEFINED(-99 ,true, false),
    WEAK_HIGH(0, true, true),
    WEAK_LOW(0, true, false),
    HIGH_Z(-98, false, false),
    CONFLICT(9001, false, false);

    private int strength;
    public final boolean valid, truth;

    private Value(int s, boolean valid, boolean truth) {
        strength = s;
        this.valid = valid;
        this.truth = truth;
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
