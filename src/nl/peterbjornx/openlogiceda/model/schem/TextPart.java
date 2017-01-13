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
import nl.peterbjornx.openlogiceda.gui.schem.dialog.TextDialog;
import nl.peterbjornx.openlogiceda.gui.view.TwoDGraphics;
import nl.peterbjornx.openlogiceda.model.draw.DrawingPart;

import java.awt.*;

/**
 * @author Peter Bosch
 */
@XStreamAlias("text")
public class TextPart extends BaseSchematicPart {

    /**
     * The font used to render the label
     */
    @XStreamOmitField
    private Font labelFont;

    /**
     * The colour of the text
     */
    private Color textColour = SchematicColours.getDefaultTextColour();

    /**
     * The name of the text
     */
    private String text = "";

    @XStreamOmitField
    private FontMetrics labelFontMetrics;

    /**
     * The size of the label font
     */
    private int fontSize = 160;

    public TextPart() {
        updateSize();
    }

    private void updateSize() {
        if (labelFont == null || labelFont.getSize() != fontSize)
            labelFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
        if (labelFontMetrics == null ) {
            Canvas c = new Canvas();
            labelFontMetrics = c.getFontMetrics(labelFont);
        }
        switch (orientation) {
            case EAST:
                leftExtent = 0;
                topExtent = labelFontMetrics.getMaxAscent();
                bottomExtent = labelFontMetrics.getMaxDescent();
                rightExtent = labelFontMetrics.stringWidth(text);
                break;
            case WEST:
                leftExtent = labelFontMetrics.stringWidth(text);
                topExtent = labelFontMetrics.getMaxAscent();
                bottomExtent = labelFontMetrics.getMaxDescent();
                rightExtent = 0;
                break;
            case NORTH:
                bottomExtent = 0;
                leftExtent = labelFontMetrics.getMaxAscent();
                rightExtent = labelFontMetrics.getMaxDescent();
                topExtent = labelFontMetrics.stringWidth(text);
                break;
            case SOUTH:
                topExtent = 0;
                leftExtent = labelFontMetrics.getMaxDescent();
                rightExtent = labelFontMetrics.getMaxAscent();
                bottomExtent = labelFontMetrics.stringWidth(text);
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

    /**
     * Open the property window for this part.
     */
    @Override
    public void edit(BaseSchematicView editor) {
        TextDialog.main(editor,this);
    }

    @Override
    public void paintPart(TwoDGraphics g, double zoom) {
        if (labelFont == null || labelFont.getSize() != fontSize) {
            labelFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
            labelFontMetrics = null;
        }
        if (labelFontMetrics == null)
            updateSize();
        g.setStroke(2,false);
        g.setFont(labelFont);
        g.setColor(textColour);
        if (selected)
            g.drawRect(-leftExtent,-topExtent,leftExtent+rightExtent,topExtent+bottomExtent);
        g.rotate(orientation.getAngle());
        if (orientation == Rotation.WEST )
            g.drawStringUpsideDown(text, labelFontMetrics.stringWidth(text), -labelFont.getSize()/4);
        else
            g.drawString(text, 0, labelFont.getSize()/4);
    }

    /**
     * Creates and returns a copy of this object
     */
    @Override
    public DrawingPart copy() {
        TextPart p = new TextPart();
        p.setX(x);
        p.setY(y);
        p.setText(text);
        p.setOrientation(orientation);
        p.updateSize();
        return p;
    }

    /**
     * Gets the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text
     */
    public void setText(String text) {
        this.text = text;
        updateSize();
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        updateSize();
    }

    public void setTextColour(Color textColour) {
        this.textColour = textColour;
    }

    public Color getTextColour() {
        return textColour;
    }

    @Override
    public String toString() {
        return "Text \""+text+"\"";
    }
}
