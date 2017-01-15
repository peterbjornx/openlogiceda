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
public class JSIForm {
    private JComboBox<SIMult> multField;
    private JTextField valueField;
    private JPanel topLevel;

    public JSIForm() {

    }

    private void createUIComponents() {
        multField = new JComboBox<SIMult>();
        multField.setModel(new DefaultComboBoxModel<>(SIMult.values()));
    }

    public void setValue(long v) {
        int div = 0;
        long dot = 0;
        while (v > 999) {
            dot = v % 1000;
            v /= 1000;
            div++;
        }
        String ds = Long.toString(v) + "."+Long.toString(dot);
        valueField.setText(ds);
        multField.setSelectedIndex(div);
    }

    public double getValue() {
        return Double.parseDouble(valueField.getText()) * ((SIMult) multField.getSelectedItem()).getFloatMultiplier();
    }

    public void setValue(double val) {
        double t = Math.log10(val);
        int x = (int) Math.floor(t);
        x = (x + 12) / 3;
        multField.setSelectedIndex(x);
        valueField.setText(String.valueOf(val / ((SIMult) multField.getSelectedItem()).getFloatMultiplier()));
    }

    public void setUnit(String unit) {
        SIMult[] va = SIMult.values();
        for (SIMult v : va)
            v.setUnit(unit);
        multField.setModel(new DefaultComboBoxModel<>(va));
    }

    private enum SIMult {
        PICO(1e-12, "p"),
        NANO(1e-9, "n"),
        MICRO(1e-6, "u"),
        MILLI(1e-3, "m"),
        NONE(1d, ""),
        KILO(1e3, "k"),
        MEGA(1e6, "M"),
        GIGA(1e9, "G"),
        TERA(1e12, "T");

        private final double flmult;
        private final String name;
        private String unit = "";

        SIMult(double flmult, String t) {
            this.name = t;
            this.flmult = flmult;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String toString() {
            return name + unit;
        }


        public double getFloatMultiplier() {
            return flmult;
        }

        public long getLongMultiplier() {
            return (long) (1e12 * flmult);
        }
    }


}
