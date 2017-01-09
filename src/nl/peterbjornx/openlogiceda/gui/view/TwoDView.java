package nl.peterbjornx.openlogiceda.gui.view;/*
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
import java.awt.event.*;

/**
 * The base for any 2D view.
 * Handles a moveable viewport and zoom.
 * @author Peter Bosch
 */
public abstract class TwoDView extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * The mouse button bound to drag.
     */
    private static final int DRAG_BUTTON = MouseEvent.BUTTON2;

    /**
     * The colour of the background
     */
    private Color backgroundColour = Color.WHITE;

    /**
     * The width of the view
     */
    protected int viewWidth = 5000;

    /**
     * The height of the view
     */
    protected int viewHeight = 5000;

    /**
     * The viewport center X
     */
    private double viewportX = 0.0d;

    /**
     * The viewport center Y
     */
    private double viewportY = 0.0d;

    /**
     * The viewport zoom
     */
    private double viewportZoom = 1.0d;

    /**
     * Are we dragging the viewport?
     */
    private boolean viewportDrag = false;

    /**
     * The graphics object used to draw the contents
     */
    protected Graphics2D graphics;

    /**
     * The origin of mouse drag
     */
    private int dragStartY, dragStartX;

    /**
     * Creates a new 2D view
     */
    public TwoDView() {
        setDoubleBuffered( true );
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setViewSize(5000,5000);
    }

    /**
     * Resizes the view
     * @param width The new width of the view
     * @param height The new height of the view
     */
    public void setViewSize(int width, int height){
        viewWidth = width;
        viewHeight = height;
        viewportX = viewWidth/2;
        viewportY = viewHeight/2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics = ((Graphics2D) g.create());
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.translate(getWidth()/2, getHeight()/2);
        graphics.scale(viewportZoom, viewportZoom);
        graphics.translate(-viewportX, -viewportY);

        graphics.setColor( backgroundColour );
        graphics.fillRect( 0, 0, viewWidth, viewHeight);
        paintView();
    }

    /**
     * Called when the user presses a mouse button.
     * @param button The button that was pressed
     * @param x The x coordinate of the cursor in view coordinates
     * @param y The y coordinate of the cursor in view coordinates
     */
    protected void onMouseDown(int button, int x, int y) {}

    /**
     * Called when the user releases a mouse button.
     * @param button The button that was releases
     * @param x The x coordinate of the cursor in view coordinates
     * @param y The y coordinate of the cursor in view coordinates
     */
    protected void onMouseUp(int button, int x, int y) {}

    /**
     * Called when the user clicks a mouse button.
     * @param button The button that was click
     * @param x The x coordinate of the cursor in view coordinates
     * @param y The y coordinate of the cursor in view coordinates
     */
    protected void onMouseClick(int button, int x, int y) {}

    /**
     * Called when the user drags the mouse
     * @param dx The difference of the x coordinate since the last event
     * @param dy The difference of the y coordinate since the last event
     */
    protected void onMouseDrag(int dx, int dy) {}

    /**
     * Called when the user moves the mouse
     * @param x The x coordinate of the cursor in view coordinates
     * @param y The y coordinate of the cursor in view coordinates
     */
    protected void onMouseMove(int x, int y) {}

    private int screenToViewX( int x ) {
        int vpX = x - getWidth() / 2;
        return (int)((vpX / viewportZoom) + viewportX);
    }

    private int screenToViewY( int y ) {
        int vpY = y - getHeight() / 2;
        return (int)((vpY / viewportZoom) + viewportY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ( e.getButton() != DRAG_BUTTON )
            onMouseClick( e.getButton(), screenToViewX(e.getX()), screenToViewY(e.getY()) );
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
        if ( e.getButton() == DRAG_BUTTON )
            viewportDrag = true;
        else
            onMouseDown( e.getButton(), screenToViewX(e.getX()), screenToViewY(e.getY()) );
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if ( e.getButton() == DRAG_BUTTON )
            viewportDrag = false;
        else
            onMouseUp( e.getButton(), screenToViewX(e.getX()), screenToViewY(e.getY()) );

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX, deltaY;
        deltaX = e.getX() - dragStartX;
        deltaY = e.getY() - dragStartY;
        if ( viewportDrag ) {
            viewportX -= (int)(deltaX/viewportZoom);
            viewportY -= (int)(deltaY/viewportZoom);
            repaint();
        } else
            onMouseDrag( (int)(deltaX/viewportZoom), (int)(deltaY/viewportZoom) );
        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        onMouseMove( screenToViewX(e.getX()), screenToViewY(e.getY()) );

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double vpz = 1.0d/viewportZoom;
        vpz += e.getPreciseWheelRotation();
        if ( vpz <= 1 )
            vpz = 1;
        viewportZoom = 1.0d/vpz;
        repaint();
    }

    /**
     * Paint the view
     */
    protected abstract void paintView();

    /**
     * Gets the background colour
     */
    public Color getBackgroundColour() {
        return backgroundColour;
    }

    /**
     * Sets the background colour
     */
    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    /**
     * Gets the width of the view
     */
    public int getViewWidth() {
        return viewWidth;
    }

    /**
     * Gets the height of the view
     */
    public int getViewHeight() {
        return viewHeight;
    }

    /**
     * Sets the viewport X coordinate
     */
    public double getViewportX() {
        return viewportX;
    }

    /**
     * Gets the viewport X coordinate
     */
    public void setViewportX(double viewportX) {
        this.viewportX = viewportX;
    }

    /**
     * Gets the viewport Y coordinate
     */
    public double getViewportY() {
        return viewportY;
    }

    /**
     * Sets the viewport Y coordinate
     */
    public void setViewportY(double viewportY) {
        this.viewportY = viewportY;
    }

    /**
     * Gets the viewport zoom
     */
    public double getViewportZoom() {
        return viewportZoom;
    }

    /**
     * Sets the viewport zoom
     */
    public void setViewportZoom(double viewportZoom) {
        this.viewportZoom = viewportZoom;
    }
}
