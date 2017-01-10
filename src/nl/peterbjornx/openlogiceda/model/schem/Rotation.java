package nl.peterbjornx.openlogiceda.model.schem;/*
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
 * Represents the orientation of a part on a schematic
 * @author Peter Bosch
 */
public enum Rotation {
    NORTH(-Math.PI/2),
    EAST(0),
    SOUTH(-(3*Math.PI)/2),
    WEST(-Math.PI);


    private final double angle;
    private Rotation next;

    Rotation(double v) {
        angle = v;
    }

    public double getAngle() {
        return angle;
    }

    public static Rotation getNext(Rotation r) {
        switch(r){
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
        }
        throw new RuntimeException("impossible");
    }
}
