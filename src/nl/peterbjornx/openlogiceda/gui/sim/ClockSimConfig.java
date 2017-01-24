package nl.peterbjornx.openlogiceda.gui.sim;/*
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

import nl.peterbjornx.openlogiceda.gui.common.JSIField;
import nl.peterbjornx.openlogiceda.gui.common.JSIForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Peter Bosch
 */
public class ClockSimConfig extends SimConfig {
    private JRadioButton frequencyRadioButton;
    private JRadioButton periodRadioButton;
    private JSIForm frequencyJSIForm;
    private JSIForm highDurationJSIForm;
    private JSIForm lowDurationJSIForm;
    private JTextField dutyCycleTextField;
    private JPanel mainPane;
    private long periodH = 500, periodL = 500;
    private String name = "a";

    public ClockSimConfig() {
        frequencyRadioButton.addActionListener(e -> {
            if (frequencyRadioButton.isSelected()) {
                setFromPeriod();
            }else
                setFromFrequency();
        });
        periodRadioButton.addActionListener(e -> {
            if (periodRadioButton.isSelected())
                setFromFrequency();
            else
                setFromPeriod();
        });
    }

    @Override
    public void setValues(String config) {
        if ( config == null )
            config = "a 500 500";
        String[] toks = config.split(" ");
        try {
            periodH = Long.parseLong(toks[1]);
            if (toks.length > 2)
                periodL = Long.parseLong(toks[2]);
            else
                periodL = periodH;
        } catch (Exception e) {

        }
        setToFrequency();
        setToPeriod();
    }

    private void setToFrequency() {
        frequencyJSIForm.setValue(1e12 / ((double) (periodH + periodL)));
        dutyCycleTextField.setText(String.valueOf(100.0 * ((double) periodH) / ((double) (periodH + periodL))));
    }

    private void setToPeriod() {
        highDurationJSIForm.setValue(periodH);
        lowDurationJSIForm.setValue(periodL);
    }

    private void setFromFrequency() {
        double freq = frequencyJSIForm.getValue();
        double duty = Double.valueOf(dutyCycleTextField.getText()) / 100.0;
        double per = 1e12 / freq;
        double perH = per * duty;
        double perL = per * (1.0 - duty);
        periodH = (long) perH;
        periodL = (long) perL;
        setToPeriod();
    }

    private void setFromPeriod() {
        periodH = (long) (1e12*highDurationJSIForm.getValue());
        periodL = (long) (1e12*lowDurationJSIForm.getValue());
        setToFrequency();
    }

    @Override
    public String getValues() {
        if (periodRadioButton.isSelected())
            setFromPeriod();
        else
            setFromFrequency();
        return name + " " + periodH + " " + periodL;
    }

    @Override
    public JPanel getMainPane() {
        return mainPane;
    }

}
