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
import nl.peterbjornx.openlogiceda.util.TimeUtil;
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
    private Font labelFont = new Font("Sans Serif", Font.BOLD,12);
    private long startTime = 0;
    private long timeScale = 20;
    private java.util.List<Trace> traces = new LinkedList<>();
    private Font tickFont = new Font( "Sans Serif", Font.PLAIN, 8);
    private Color tickColour = Color.white;
    private long majorTickInterval = 500;
    private int scrollY = 0;
    private Color gridColor = Color.gray.darker();
    private int SIGNAL_SPACE = 5;
    private int LABEL_SPACE = 5;

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

    private void drawMajorTick( Graphics g, int x, int y, long time )
    {
        g.setFont(tickFont);
        String ts = TimeUtil.formatTimeShort(time);
        int tw = g.getFontMetrics().stringWidth( ts );
        int th = g.getFontMetrics().getDescent();
        g.setColor(tickColour);
        g.drawString(ts, x - tw / 2, y + signalHeight / 2 + th);
        g.drawLine(x, y + signalHeight / 2 + th + 1, x, y + signalHeight );
    }

    private void drawMinorTick( Graphics g, int x, int y, long time )
    {
        g.setFont(tickFont);
        String ts = Long.toString( time );//TODO: Time formatter
        int tw = g.getFontMetrics().stringWidth( ts );
        int th = g.getFontMetrics().getDescent();
        g.setColor(tickColour);
      //  g.drawString(ts, x - tw / 2, y + signalHeight / 2 + th);
        g.drawLine(x, y + (signalHeight *5)/6, x, y + signalHeight );
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
            int transitX = (int) ((transitTime - startTime) / timeScale);
            int settleX = (int) ((settleTime - startTime) / timeScale);
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
            lastX = Integer.max(0,settleX);
            lastY = Integer.max( 0,settleY);
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
        int traceX = labelWidth + LABEL_SPACE;
        for ( int x = traceX; x < w; x++) {
            long time = (x - traceX) * timeScale + startTime;
            if ( time % majorTickInterval == 0 ) {
                drawMajorTick(g, x, 0, time);
                drawGridLine(g, x, signalHeight, h - signalHeight );
            }
        }
        y += signalHeight + 1;
        Shape oldClip = g.getClip();
        g.setClip(0, y, w - 5, h - y - 5);
        g.translate( 0, -scrollY );
        for ( Trace t : traces ) {
            drawLabel( g, 0,y, t.name, t.colour );
            drawTrace( g, traceX, y, t.history);
            y += signalHeight + SIGNAL_SPACE;
        }
        g.translate( 0, scrollY );
        g.setClip(oldClip);
    }

    private void drawGridLine(Graphics g, int x, int y, int h) {
        g.setColor(gridColor);
        g.drawLine(x,y,x,y+h);
    }

    public void addTrace( Color colour, String name, SignalHistory history ) {
        traces.add( new Trace(colour, name, history));
    }

    public int getScrollYMax() {
        int contentHeight = (signalHeight + SIGNAL_SPACE) * (traces.size() + 1) - getHeight();
        return Integer.max(contentHeight, 0);
    }

    public int getVisibleHeight() {
        return  getHeight() - signalHeight - SIGNAL_SPACE;
    }
    public int getVisibleWidth() {
        return  getWidth() - labelWidth - LABEL_SPACE - 5;
    }

    public int getScrollXMax() {
        long longestTime = 0;
        for ( Trace t : traces )
            longestTime = Long.max(t.history.getDuration(), longestTime);
        int xContent =  (int) (longestTime / timeScale);
        xContent += labelWidth + LABEL_SPACE + 5;
        xContent -= getWidth();
        return Integer.max(xContent, 0);
    }

    public void setScroll( int x, int y ) {
        if ( x < 0 || y < 0 )
            throw new IllegalArgumentException("scroll position can not be smaller than 0");
        startTime = x * timeScale;
        scrollY = y;
    }

    public void setScrollX( int x ) {
        if ( x < 0 )
            throw new IllegalArgumentException("scroll position can not be smaller than 0");
        startTime = x * timeScale;
        repaint();
    }

    public void setScrollY( int y ) {
        if (  y < 0 )
            throw new IllegalArgumentException("scroll position can not be smaller than 0");
        scrollY = y;
        repaint();
    }

    public long getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(long timeScale) {
        this.timeScale = timeScale;
        repaint();
    }

    private static class Trace {
        Color colour;
        String name;
        SignalHistory history;

        Trace(Color colour, String name, SignalHistory history) {
            this.colour = colour;
            this.name = name;
            this.history = history;
        }
    }
}
