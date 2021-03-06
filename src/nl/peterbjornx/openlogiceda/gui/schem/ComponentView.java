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

import nl.peterbjornx.openlogiceda.config.KeyBindings;
import nl.peterbjornx.openlogiceda.gui.schem.dialog.CompEditDialog;
import nl.peterbjornx.openlogiceda.model.draw.Drawing;
import nl.peterbjornx.openlogiceda.model.schem.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * @author Peter Bosch
 */
public class ComponentView extends BaseSchematicView {
    public final static int MODE_PIN = 1;
    public final static int MODE_RECT = 2;
    public final static int MODE_LINE = 3;
    public final static int MODE_TEXT = 4;

    /**
     * Creates a new drawing view
     */
    public ComponentView() {
        super(new SchematicComponent("testing"));
    }

    @Override
    protected boolean onKeyDown(int kc) {
        if (super.onKeyDown(kc))
            return true;
        if ( kc == KeyBindings.getComponentModeSelect()) {
            setEditMode(MODE_SELECT);
            return true;
        } else if ( kc == KeyBindings.getComponentModePin()) {
            setEditMode(MODE_PIN);
            return true;
        } else if ( kc == KeyBindings.getComponentRotate()) {
            rotate();
            return true;
        } else if ( kc == KeyBindings.getComponentMove()) {
            move();
            return true;
        } else if ( kc == KeyBindings.getComponentCopy()) {
            copy();
            return true;
        } else if ( kc == KeyBindings.getComponentEdit()) {
            edit();
            return true;
        } else if ( kc == KeyBindings.getComponentDelete()) {
            delete();
            return true;
        }
        return false;
    }

    public void newComponent() {
        if (!close())
            return;
        openFile = null;
        try {
            setDrawing(new SchematicComponent("none"));

        } catch (Exception e) {
            //TODO: Show error dialog
            e.printStackTrace();
        }
    }

    public FileNameExtensionFilter getFileNameExtensionFilter() {
        return new FileNameExtensionFilter(
                "Schematic Components", "cmp","xml");
    }

    public boolean isAcceptedFilename(File dir, String name){
        if (name.toLowerCase().endsWith(".cmp"))
            return true;
        else if (name.toLowerCase().endsWith(".xml"))
            return true;
        else
            return false;
    }

    @Override
    protected void paintBackground() {
        super.paintBackground();
        graphics.setVariable("NAME", ((SchematicComponent)getDrawing()).getName());
        graphics.setVariable("REF", "U?");
        graphics.setVariable("SIM", "*sim*");
        graphics.setStroke(5,false);
        graphics.setColor( Color.RED );
        Drawing d = getDrawing();
        int w = d.getWidth();
        int h = d.getHeight();
        graphics.drawLine(0, h/2, w, h/2);
        graphics.drawLine(w/2, 0, w/2, h);
    }

    @Override
    protected boolean onMouseClick(int button, int x, int y) {
        if (super.onMouseClick(button, x, y))
            return true;
        switch (editMode) {
            case MODE_PIN:
                add(new PinPart());
                    return true;
            case MODE_RECT:
                addShape(new RectanglePart());
                return true;
            case MODE_LINE:
                addShape(new LinePart());
                return true;
            case MODE_TEXT:
                add(new TextPart());
                return true;
        }
        return false;
    }

    public void props() {
        CompEditDialog.main((SchematicComponent)getDrawing());
    }
}
