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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Peter Bosch
 */
public class SignalHistory {

    private LinkedList<SignalChange> list = new LinkedList<>();


    public SignalHistory(){}

    public void addChange(long timestamp, Value value ){
        list.add(new SignalChange(timestamp, value));
    }

    public List<SignalChange> getHistory() {
        return list;
    }

    public class SignalChange {
        private Value value;
        private long timestamp;

        public SignalChange(long timestamp, Value value)
        {
            this.timestamp = timestamp;
            this.value = value;
        }

        public Value getValue() {
            return value;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

}
