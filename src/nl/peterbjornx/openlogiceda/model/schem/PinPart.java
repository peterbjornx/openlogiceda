package nl.peterbjornx.openlogiceda.model.schem;/*
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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import nl.peterbjornx.openlogiceda.config.SchematicColours;
import nl.peterbjornx.openlogiceda.gui.schem.BaseSchematicView;
import nl.peterbjornx.openlogiceda.gui.schem.dialog.PinDialog;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A pin in a component
 * @author Peter Bosch
 */
@XStreamAlias("pin")
public class PinPart extends BaseSchematicPart {

    /**
     * The length of the actual pin line segment
     */
    private static final int PIN_LENGTH = 200;

    /**
     * The length of the actual pin line segment
     */
    private static final int PIN_SPACING = 25;

    /**
     * The font used to render the label
     */
    private static Font labelFont = new Font(Font.MONOSPACED, Font.PLAIN, 50);

    /**
     * The name of the pin
     */
    private String name = "";

    /**
     * The style of the pin
     */
    private PinStyle style = PinStyle.INVERTED_CLOCK;

    @XStreamOmitField
    private FontMetrics labelFontMetrics;
    private static final int BUBBLE_DIAM = 50;

    @XStreamOmitField
    private SchematicNode node = new PinNode(this );

    public PinPart() {
        updateSize();
    }

    private void updateSize() {
        if (labelFontMetrics == null ) {
            Canvas c = new Canvas();
            labelFontMetrics = c.getFontMetrics(labelFont);
        }
        switch (orientation) {
            case EAST:
                rightExtent = PIN_LENGTH;
                topExtent = PIN_SPACING;
                bottomExtent = PIN_SPACING;
                leftExtent = labelFontMetrics.stringWidth(name)+PIN_SPACING;
                break;
            case WEST:
                leftExtent = PIN_LENGTH;
                topExtent = PIN_SPACING;
                bottomExtent = PIN_SPACING;
                rightExtent = labelFontMetrics.stringWidth(name)+PIN_SPACING;
                break;
            case NORTH:
                topExtent = PIN_LENGTH;
                rightExtent = PIN_SPACING;
                leftExtent = PIN_SPACING;
                bottomExtent = labelFontMetrics.stringWidth(name)+PIN_SPACING;
                break;
            case SOUTH:
                bottomExtent = PIN_LENGTH;
                rightExtent = PIN_SPACING;
                leftExtent = PIN_SPACING;
                topExtent = labelFontMetrics.stringWidth(name)+PIN_SPACING;
                break;
        }

    }

    /**
     * Sets the orientation of this part
     */
    @Override
    public void setOrientation(Rotation orientation) {
        super.setOrientation(orientation);
        updateSize();
    }

    public Object readResolve(){
        node = new PinNode(this );
        return this;
    }

    /**
     * Open the property window for this part.
     */
    @Override
    public void edit(BaseSchematicView editor) {
        PinDialog.showDialog(editor,this);
    }

    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        g.setStroke(5,false);
        g.setFont(labelFont);
        g.setColor(SchematicColours.pinColour);
        if (selected)
            g.drawRect(-leftExtent,-topExtent,leftExtent+rightExtent,topExtent+bottomExtent);
        g.rotate(orientation.getAngle());
        if ( style == PinStyle.INVERTED || style == PinStyle.INVERTED_CLOCK) {
            g.drawLine(BUBBLE_DIAM, 0, PIN_LENGTH, 0);
            g.drawOval(0, -BUBBLE_DIAM / 2, BUBBLE_DIAM, BUBBLE_DIAM);
        } else
            g.drawLine(0, 0, PIN_LENGTH, 0);
        if ( style == PinStyle.INVERTED_CLOCK || style == PinStyle.CLOCK )
            g.drawPolyline(new int[]{0,-PIN_SPACING,0},new int[]{-PIN_SPACING,0,PIN_SPACING},3);
        if (orientation == Rotation.WEST )
            g.drawStringUpsideDown(name, -PIN_SPACING, -labelFont.getSize()/4);
        else
            g.drawString(name, -g.getFontMetrics().stringWidth(name)-PIN_SPACING, labelFont.getSize()/4);
    }

    /**
     * Creates and returns a copy of this object
     */
    @Override
    public DrawingPart copy() {
        PinPart p = new PinPart();
        p.setX(x);
        p.setY(y);
        p.setName(name);
        p.orientation = orientation;
        p.style = style;
        p.updateSize();
        return p;
    }

    /**
     * Gets the name of the pin
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the pin
     */
    public void setName(String name) {
        this.name = name;
        updateSize();
    }

    /**
     * Gets the style of the pin
     */
    public PinStyle getStyle() {
        return style;
    }

    /**
     * Sets the style of the pin
     */
    public void setStyle(PinStyle style) {
        this.style = style;
    }

    public enum PinStyle {
        NORMAL,
        INVERTED,
        CLOCK,
        INVERTED_CLOCK
    }

    @Override
    public String toString() {
        return "Pin \""+name+"\"";
    }

    @Override
    public SchematicNode getNodeAt(int x, int y) {
        x -= this.x;
        y -= this.y;
        switch(orientation){
            case EAST:
                if ( x == PIN_LENGTH && y == 0 )
                    return node;
                return null;
            case WEST:
                if ( x == -PIN_LENGTH && y == 0 )
                    return node;
                return null;
            case NORTH:
                if ( x == 0 && y == -PIN_LENGTH )
                    return node;
                return null;
            case SOUTH:
                if ( x == 0 && y == PIN_LENGTH )
                    return node;
                return null;
        }
        return null;
    }

    /**
     * Gets all the nodes on this part
     */
    public java.util.List<SchematicNode> getNodes(){
        List<SchematicNode> e = new LinkedList<>();
        node.setConnectionX(x);
        node.setConnectionY(y);
        switch(orientation){
            case EAST:
                node.setConnectionX(x+PIN_LENGTH);
                break;
            case WEST:
                node.setConnectionX(x-PIN_LENGTH);
                break;
            case NORTH:
                node.setConnectionY(y+PIN_LENGTH);
                break;
            case SOUTH:
                node.setConnectionY(y-PIN_LENGTH);
                break;
        }
        e.add(node);
        return e;
    }


}
