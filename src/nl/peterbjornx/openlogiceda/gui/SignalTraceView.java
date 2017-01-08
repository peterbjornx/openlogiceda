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

import nl.peterbjornx.openlogiceda.model.Value;
import nl.peterbjornx.openlogiceda.sim.SignalHistory;
import sun.misc.Signal;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author Peter Bosch
 */
public class SignalTraceView extends JPanel {

    private Color bgColour = Color.black;
    private int labelWidth = 100;
    private int signalHeight = 30;
    private Color labelBgColour = Color.gray;
    private Font labelFont = new Font("Sans Serif",Font.BOLD,12);
    private long startTime = 0;
    private long timeScale = 20;
    private java.util.List<Trace> traces = new LinkedList<>();

    public SignalTraceView() {
    }

    private void drawLabel( Graphics g, int x, int y, String name, Color colour ) {
        g.setFont(labelFont);
        int tw = g.getFontMetrics().stringWidth( name );
        int th = g.getFontMetrics().getDescent();
        g.setColor( labelBgColour );
        g.fillRoundRect( x, y, labelWidth, signalHeight, 5, 5 );
        g.setColor( colour );
        g.fillRoundRect( x, y, 15, signalHeight, 5, 5 );
        g.drawRoundRect( x, y, labelWidth, signalHeight, 5, 5 );
        g.drawString( name, x + 30, y + signalHeight/2 + th );
    }

    private int valueY( Value v ){
        switch ( v ) {
            case WEAK_HIGH:
            case HIGH:
                return signalHeight / 6;
            case WEAK_LOW:
            case LOW:
                return ( signalHeight * 5 ) / 6;
            case UNDEFINED:
            case HIGH_Z:
            case CONFLICT:
                return signalHeight / 2;
            default:
                throw new RuntimeException("invalid enum value");
        }
    }

    private Color valueColour( Value v ) {
        switch ( v ) {
            case CONFLICT:
                return Color.RED;
            case UNDEFINED:
                return Color.GRAY;
            default:
                return Color.GREEN;
        }
    }

    private void drawTrace(Graphics g, int x, int y, SignalHistory history ) {
        java.util.List<SignalHistory.SignalChange> hist = history.getHistory();
        Value v = Value.UNDEFINED;
        int lastX = 0, lastY = valueY(v);
        Color lastColour = valueColour(v);
        int w = getWidth() - x;
        for ( SignalHistory.SignalChange c : hist ) {
            long transitTime = c.getTimestamp() - 3;
            long settleTime = c.getTimestamp();
            v = c.getValue();
            int transitX = (int) (transitTime / timeScale);
            int settleX = (int) (settleTime / timeScale);
            int settleY = valueY(v);
            Color settleColour = valueColour(v);
            if ( c.getTimestamp() >= startTime ) {
               if ( settleX >= w ) {
                   settleX = w;
               }
               g.setColor(lastColour);
               g.drawLine(lastX+x, lastY+y, transitX+x, lastY+y);
               g.setColor(settleColour);
               g.drawLine(transitX+x, lastY+y, settleX+x, settleY+y);
               if ( settleX >= w )
                   return;
            }
            lastX = settleX;
            lastY = settleY;
            lastColour = settleColour;
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        g.setColor(bgColour);
        g.fillRect( 0, 0, w, h );
        int y = 0;
        for ( Trace t : traces ) {
            drawLabel( g, 0,y, t.name, t.colour );
            drawTrace( g, labelWidth + 5, y, t.history);
            y += signalHeight + 5;

        }
    }

    public void addTrace( Color colour, String name, SignalHistory history ) {
        traces.add( new Trace(colour, name, history));
    }

    private static class Trace {
        Color colour;
        String name;
        SignalHistory history;

        public Trace(Color colour, String name, SignalHistory history) {
            this.colour = colour;
            this.name = name;
            this.history = history;
        }
    }
}
