package nl.peterbjornx.openlogiceda.gui.common;/*
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

import javax.swing.*;
import java.awt.*;

/**
 * @author Peter Bosch
 */
public class JSIField extends JPanel {

    private JTextField text;
    private JComboBox<SIMult> mult;
    private String unit;

    public JSIField() {
        mult = new JComboBox<>();
        SIMult[] va = SIMult.values();
        for (SIMult v : va)
            v.setUnit(unit);
        mult.setModel(new DefaultComboBoxModel<>(va));
        text = new JTextField();
        text.setText("1");
        text.setMinimumSize(new Dimension(100, (int) text.getPreferredSize().getHeight()));
        add(text);
        add(mult);
        doLayout();
    }

    private enum SIMult {
        PICO( 1e-12, "p" ),
        NANO( 1e-9, "n"),
        MICRO( 1e-6, "u" ),
        MILLI( 1e-3, "m" ),
        NONE( 1d, "" ),
        KILO( 1e3, "k" ),
        MEGA( 1e6, "M" ),
        GIGA( 1e9, "G" ),
        TERA( 1e12, "T" );

        private final double flmult;
        private final String name;
        private String unit="";

        SIMult(double flmult, String t) {
            this.name = t;
            this.flmult = flmult;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String toString() {
            return name;
        }

        public double getFloatMultiplier() {
            return flmult;
        }

        public long getLongMultiplier() {
            return (long)(1e12*flmult);
        }
    }

}
