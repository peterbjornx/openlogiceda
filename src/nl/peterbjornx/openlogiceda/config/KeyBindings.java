package nl.peterbjornx.openlogiceda.config;/*
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

import java.awt.event.KeyEvent;

/**
 * @author Peter Bosch
 */
public class KeyBindings {

    /**
     * The key code for selecting multiple items
     */
    private static int drawingSelectMultiple = KeyEvent.VK_SHIFT;
    private static int componentModePin = KeyEvent.VK_P;
    private static int componentModeSelect = KeyEvent.VK_ESCAPE;
    private static int componentRotate = KeyEvent.VK_R;
    private static int componentMove = KeyEvent.VK_M;

    public static int getDrawingSelectMultiple() {
        return drawingSelectMultiple;
    }

    public static void setDrawingSelectMultiple(int drawingSelectMultiple) {
        KeyBindings.drawingSelectMultiple = drawingSelectMultiple;
    }

    public static int getComponentModePin() {
        return componentModePin;
    }

    public static int getComponentModeSelect() {
        return componentModeSelect;
    }

    public static int getComponentRotate() {
        return componentRotate;
    }

    public static int getComponentMove() {
        return componentMove;
    }
}
