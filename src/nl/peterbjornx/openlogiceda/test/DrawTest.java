package nl.peterbjornx.openlogiceda.test;/*
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

import nl.peterbjornx.openlogiceda.gui.view.DrawingView;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.draw.DrawingIO;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Peter Bosch
 */
public class DrawTest {

    public static void main(String[] args) {
        JFrame tf = new JFrame("Test");
        tf.add(new TestDrawingView(new Drawing(5000, 5000) {
            @Override
            public DrawingIO getIO() {
                return null;
            }
        }));
        //  tf.pack();
        tf.setResizable(true);
        tf.setVisible(true);

    }

    public static class TestDrawingView extends DrawingView {

        public static final int MODE_ADD = 1;

        public TestDrawingView(Drawing drawing) {
            super(drawing);
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if ( e.getKeyChar() == 'e' )
                        setEditMode(MODE_ADD);
                }
            });
        }

        @Override
        protected boolean onMouseClick(int button, int x, int y) {
            if (super.onMouseClick(button, x, y))
                return true;
            switch( editMode ) {
                case MODE_ADD:
                    editMode = MODE_SELECT;
                    addPart(new TestPart(x,y));
                    return true;
            }
            return false;
        }
    }

    public static class TestPart extends DrawingPart {

        public TestPart( int x, int y ){
            this.x = x;
            this.y = y;
            this.leftExtent = 100;
            this.rightExtent = 100;
            this.topExtent = 100;
            this.bottomExtent = 100;
        }

        @Override
        public void paintPart(TwoDGraphics g, double zoom) {
            if ( selected )
                g.setColor(Color.magenta);
            else
                g.setColor(Color.blue);
            g.setStroke(3,true);
            g.drawRect(-leftExtent, -topExtent, rightExtent + leftExtent, bottomExtent +topExtent);
        }

        /**
         * Creates a copy of this part
         */
        @Override
        public DrawingPart copy() {
            return null;
        }
    }
}
