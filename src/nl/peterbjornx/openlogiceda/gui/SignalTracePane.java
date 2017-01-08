package nl.peterbjornx.openlogiceda.gui;/*
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

import nl.peterbjornx.openlogiceda.sim.SignalHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Peter Bosch
 */
public class SignalTracePane {
    private JScrollBar verticalScrollBar;
    private JScrollBar horizontalScrollBar;
    private SignalTraceView signalView;
    private JPanel mainPane;
    private JButton zoomInBtn;
    private JButton zoomOutBtn;
    private JButton zoomDefaultBtn;
    private long defaultZoom = 400;

    public SignalTracePane() {
        verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                signalView.setScrollY(verticalScrollBar.getValue());
            }
        });
        horizontalScrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                signalView.setScrollX(horizontalScrollBar.getValue());
            }
        });
        signalView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateHorizontal();
                updateVertical();
            }
        });
        updateHorizontal();
        updateVertical();
        zoomInBtn.addActionListener(e -> zoomIn());
        zoomOutBtn.addActionListener(e -> zoomOut());
        zoomDefaultBtn.addActionListener(e -> zoomSet(defaultZoom));
    }

    private void zoomIn() {
        long timeScale = signalView.getTimeScale();
        signalView.setTimeScale(timeScale / 2);
        zoomChanged();
    }

    private void zoomOut() {
        long timeScale = signalView.getTimeScale();
        signalView.setTimeScale(timeScale * 2);
        zoomChanged();
    }

    private void zoomSet( long zoom ){
        signalView.setTimeScale(zoom);
        zoomChanged();
    }

    private void zoomChanged() {
        long timeScale = signalView.getTimeScale();
        zoomInBtn.setEnabled(timeScale > 1);
        zoomDefaultBtn.setEnabled(timeScale != defaultZoom);
        updateHorizontal();
    }

    public void addTrace(Color colour, String name, SignalHistory history) {
        signalView.addTrace(colour, name, history);
        updateHorizontal();
        updateVertical();
    }

    public void updateHorizontal() {
        horizontalScrollBar.setMaximum(signalView.getScrollXMax()+signalView.getVisibleWidth());
        horizontalScrollBar.setVisibleAmount(signalView.getVisibleWidth());
    }

    public void updateVertical() {
        verticalScrollBar.setMaximum(signalView.getScrollYMax()+signalView.getVisibleHeight());
        verticalScrollBar.setVisibleAmount(signalView.getVisibleHeight());
    }

    public JPanel getMainPane() {
        return mainPane;
    }
}
