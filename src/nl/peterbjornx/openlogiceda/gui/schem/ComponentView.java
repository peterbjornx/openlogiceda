package nl.peterbjornx.openlogiceda.gui.schem;/*
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
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.schem.SchematicComponent;

/**
 * @author Peter Bosch
 */
public class ComponentView extends DrawingView {
    public final static int MODE_PIN = 1;
    public final static int MODE_RECT = 2;
    public final static int MODE_LINE = 3;
    public final static int MODE_LABEL = 4;

    /**
     * Creates a new drawing view
     */
    public ComponentView() {
        super(new SchematicComponent("testing"));
    }
}
