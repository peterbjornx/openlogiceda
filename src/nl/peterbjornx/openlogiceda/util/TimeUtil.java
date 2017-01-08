package nl.peterbjornx.openlogiceda.util;/*
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
 * @author Peter Bosch
 */
public class TimeUtil {

    private final static String[] SI_TIME = { "ps","ns","us","ms","s","ks","Ms","Gs"};

    public static String formatTimeShort( long time ) {
        int div = 0;
        long dot = 0;
        while ( time > 999 ) {
            dot = time % 1000;
            time /= 1000;
            div++;
        }
        String ds = Long.toString(dot);
        String ms = Long.toString(time);
        String ts = ms;
        if ( ms.length() < 3 && dot != 0)
            ts += "." +ds.substring(0, Integer.max(0, 3 - ms.length()));
        return ts + SI_TIME[div];
    }

}
