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

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper routines to make drawing on a TwoDView easier
 * @author Peter Bosch
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class TwoDGraphics {

    private TwoDView view;
    private Graphics2D g;
    private Font upsideDownFont;
    private HashMap<String, String> textVariables = new HashMap<>();

    TwoDGraphics( TwoDView view, Graphics2D g ) {
        this.view = view;
        this.g = g;
        upsideDownFont = getFont().deriveFont(AffineTransform.getRotateInstance(Math.PI));
    }

    public void setVariable(String name, String val) {
        textVariables.put(name,val);
    }

    private String doVarSub(String format) {
        String out = "", var;
        int pos = -1,len = format.length();
        int start = -1,end;
        boolean preStart = false;
        while ( pos < len - 1 ) {
            char c = format.charAt(++pos);
            if ( start != -1 ) {
                if ( c == '}' ) {
                    end = pos;
                    var = format.substring(start+1,end);
                    if ( textVariables.containsKey(var) )
                        out += textVariables.get(var);
                    start = -1;
                    continue;
                } else
                    continue;
            }
            if ( preStart ) {
                if (c == '{') {
                    start = pos;
                    preStart = false;
                    continue;
                } else {
                    out += "$";
                    preStart = false;
                }
            }
            if ( c == '$')
                preStart = true;
            else
                out += c;

        }
        return out;
    }

    /**
     * Creates a new graohics object
     */
    public TwoDGraphics create() {
        TwoDGraphics gd = new TwoDGraphics( view, (Graphics2D) g.create() );
        Set<Map.Entry<String, String>> a = textVariables.entrySet();
        for ( Map.Entry<String, String> b : a ) {
            gd.setVariable(b.getKey(),b.getValue());
        }
        return gd;
    }

    /**
     * Sets a solid stroke with with defaults for all
     * attributes.
     * The default attributes are a solid line of width 1.0, CAP_SQUARE,
     * JOIN_MITER, a miter limit of 10.0.
     */
    public void setStroke( boolean screen ) {
        setStroke( 1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f, screen);
    }

    /**
     * Sets a solid stroke with the specified
     * line width and with default values for the cap and join
     * styles.
     * @param width the width of the <code>BasicStroke</code>
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public void setStroke( float width, boolean screen ) {
        setStroke( width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f, screen);
    }

    /**
     * Sets a solid stroke with the specified
     * attributes. The <code>miterlimit</code> parameter is
     * unnecessary in cases where the default is allowable or the
     * line joins are not specified as JOIN_MITER.
     * @param width the width of the <code>BasicStroke</code>
     * @param cap the decoration of the ends of a <code>BasicStroke</code>
     * @param join the decoration applied where path segments meet
     * @param screen whether the width is specified in screen coordinates
     * @throws IllegalArgumentException if <code>width</code> is negative
     * @throws IllegalArgumentException if <code>cap</code> is not either
     *         CAP_BUTT, CAP_ROUND or CAP_SQUARE
     * @throws IllegalArgumentException if <code>join</code> is not
     *         either JOIN_ROUND, JOIN_BEVEL, or JOIN_MITER
     */
    public void setStroke( float width,
                           int cap,
                           int join,
                           boolean screen ) {
        setStroke( width, cap, join, 10.0f, null, 0.0f, screen);
    }

    /**
     * Sets a solid stroke with the specified
     * attributes.
     * @param width the width of the <code>BasicStroke</code>
     * @param cap the decoration of the ends of a <code>BasicStroke</code>
     * @param join the decoration applied where path segments meet
     * @param miterlimit the limit to trim the miter join
     * @param screen whether the width is specified in screen coordinates
     * @throws IllegalArgumentException if <code>width</code> is negative
     * @throws IllegalArgumentException if <code>cap</code> is not either
     *         CAP_BUTT, CAP_ROUND or CAP_SQUARE
     * @throws IllegalArgumentException if <code>miterlimit</code> is less
     *         than 1 and <code>join</code> is JOIN_MITER
     * @throws IllegalArgumentException if <code>join</code> is not
     *         either JOIN_ROUND, JOIN_BEVEL, or JOIN_MITER
     */
    public void setStroke( float width,
                           int cap,
                           int join,
                           float miterlimit,
                           boolean screen ) {
        setStroke( width, cap, join, miterlimit, null, 0.0f, screen);
    }

    /**
     * Sets a stroke with the specified
     * attributes.
     * @param width the width of this <code>BasicStroke</code>.  The
     *         width must be greater than or equal to 0.0f.  If width is
     *         set to 0.0f, the stroke is rendered as the thinnest
     *         possible line for the target device and the antialias
     *         hint setting.
     * @param cap the decoration of the ends of a <code>BasicStroke</code>
     * @param join the decoration applied where path segments meet
     * @param miterlimit the limit to trim the miter join.  The miterlimit
     *        must be greater than or equal to 1.0f.
     * @param dash the array representing the dashing pattern
     * @param dash_phase the offset to start the dashing pattern
     * @param screen whether the width is specified in screen coordinates
     * @throws IllegalArgumentException if <code>width</code> is negative
     * @throws IllegalArgumentException if <code>cap</code> is not either
     *         CAP_BUTT, CAP_ROUND or CAP_SQUARE
     * @throws IllegalArgumentException if <code>miterlimit</code> is less
     *         than 1 and <code>join</code> is JOIN_MITER
     * @throws IllegalArgumentException if <code>join</code> is not
     *         either JOIN_ROUND, JOIN_BEVEL, or JOIN_MITER
     * @throws IllegalArgumentException if <code>dash_phase</code>
     *         is negative and <code>dash</code> is not <code>null</code>
     * @throws IllegalArgumentException if the length of
     *         <code>dash</code> is zero
     * @throws IllegalArgumentException if dash lengths are all zero.
     */
    public void setStroke( float width,
                           int cap,
                           int join,
                           float miterlimit,
                           float[] dash,
                           float dash_phase,
                           boolean screen ) {
        if ( screen )
            width /= (float) view.viewportZoom;
        else if ( width * view.viewportZoom < 1.0f)
            width = (float) (1.0f / view.viewportZoom);
        this.g.setStroke(new BasicStroke(width,cap,join,miterlimit,dash,dash_phase));
    }


    /**
     * Draws a 3-D highlighted outline of the specified rectangle.
     * The edges of the rectangle are highlighted so that they
     * appear to be beveled and lit from the upper left corner.
     * <p>
     * The colors used for the highlighting effect are determined
     * based on the current color.
     * The resulting rectangle covers an area that is
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.  This method
     * uses the current <code>Color</code> exclusively and ignores
     * the current <code>Paint</code>.
     * @param x the x coordinate of the rectangle to be drawn.
     * @param y the y coordinate of the rectangle to be drawn.
     * @param width the width of the rectangle to be drawn.
     * @param height the height of the rectangle to be drawn.
     * @param raised a boolean that determines whether the rectangle
     *                      appears to be raised above the surface
     *                      or sunk into the surface.
     * @see         Graphics#fill3DRect
     */
    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        g.draw3DRect(x, y, width, height, raised);
    }

    /**
     * Paints a 3-D highlighted rectangle filled with the current color.
     * The edges of the rectangle are highlighted so that it appears
     * as if the edges were beveled and lit from the upper left corner.
     * The colors used for the highlighting effect and for filling are
     * determined from the current <code>Color</code>.  This method uses
     * the current <code>Color</code> exclusively and ignores the current
     * <code>Paint</code>.
     * @param x the x coordinate of the rectangle to be filled.
     * @param y the y coordinate of the rectangle to be filled.
     * @param       width the width of the rectangle to be filled.
     * @param       height the height of the rectangle to be filled.
     * @param       raised a boolean value that determines whether the
     *                      rectangle appears to be raised above the surface
     *                      or etched into the surface.
     * @see         Graphics#draw3DRect
     */
    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        g.fill3DRect(x, y, width, height, raised);
    }

    /**
     * Renders the text of the specified <code>String</code>, using the
     * current text attribute state in the <code>Graphics2D</code> context.
     * The baseline of the
     * first character is at position (<i>x</i>,&nbsp;<i>y</i>) in
     * the User Space.
     * The rendering attributes applied include the <code>Clip</code>,
     * <code>Transform</code>, <code>Paint</code>, <code>Font</code> and
     * <code>Composite</code> attributes.  For characters in script
     * systems such as Hebrew and Arabic, the glyphs can be rendered from
     * right to left, in which case the coordinate supplied is the
     * location of the leftmost character on the baseline.
     * @param str the string to be rendered
     * @param x the x coordinate of the location where the
     * <code>String</code> should be rendered
     * @param y the y coordinate of the location where the
     * <code>String</code> should be rendered
     * @throws NullPointerException if <code>str</code> is
     *         <code>null</code>
     * @see         Graphics#drawBytes
     * @see         Graphics#drawChars
     * @since JDK1.0
     */
    public void drawString(String str, int x, int y) {
        g.drawString(doVarSub(str), x, y);
    }

    /**
     * Renders the text of the specified <code>String</code>, using the
     * current text attribute state in the <code>Graphics2D</code> context.
     * The baseline of the
     * first character is at position (<i>x</i>,&nbsp;<i>y</i>) in
     * the User Space.
     * The rendering attributes applied include the <code>Clip</code>,
     * <code>Transform</code>, <code>Paint</code>, <code>Font</code> and
     * <code>Composite</code> attributes.  For characters in script
     * systems such as Hebrew and Arabic, the glyphs can be rendered from
     * right to left, in which case the coordinate supplied is the
     * location of the leftmost character on the baseline.
     * @param str the string to be rendered
     * @param x the x coordinate of the location where the
     * <code>String</code> should be rendered
     * @param y the y coordinate of the location where the
     * <code>String</code> should be rendered
     * @throws NullPointerException if <code>str</code> is
     *         <code>null</code>
     * @see         Graphics#drawBytes
     * @see         Graphics#drawChars
     * @since JDK1.0
     */
    public void drawStringUpsideDown(String str, int x, int y) {
        Font o = g.getFont();
        g.setFont(upsideDownFont);
        g.drawString(doVarSub(str), x, y);
        g.setFont(o);
    }

    /**
     * Translates the origin of the <code>Graphics2D</code> context to the
     * point (<i>x</i>,&nbsp;<i>y</i>) in the current coordinate system.
     * Modifies the <code>Graphics2D</code> context so that its new origin
     * corresponds to the point (<i>x</i>,&nbsp;<i>y</i>) in the
     * <code>Graphics2D</code> context's former coordinate system.  All
     * coordinates used in subsequent rendering operations on this graphics
     * context are relative to this new origin.
     * @param  x the specified x coordinate
     * @param  y the specified y coordinate
     * @since JDK1.0
     */
    public void translate(int x, int y) {
        g.translate(x, y);
    }

    /**
     * Concatenates the current
     * <code>Graphics2D</code> <code>Transform</code>
     * with a translation transform.
     * Subsequent rendering is translated by the specified
     * distance relative to the previous position.
     * This is equivalent to calling transform(T), where T is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   1    0    tx  ]
     *          [   0    1    ty  ]
     *          [   0    0    1   ]
     * </pre>
     * @param tx the distance to translate along the x-axis
     * @param ty the distance to translate along the y-axis
     */
    public void translate(double tx, double ty) {
        g.translate(tx, ty);
    }

    /**
     * Concatenates the current <code>Graphics2D</code>
     * <code>Transform</code> with a rotation transform.
     * Subsequent rendering is rotated by the specified radians relative
     * to the previous origin.
     * This is equivalent to calling <code>transform(R)</code>, where R is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   cos(theta)    -sin(theta)    0   ]
     *          [   sin(theta)     cos(theta)    0   ]
     *          [       0              0         1   ]
     * </pre>
     * Rotating with a positive angle theta rotates points on the positive
     * x axis toward the positive y axis.
     * @param theta the angle of rotation in radians
     */
    public void rotate(double theta) {
        g.rotate(theta);
    }

    /**
     * Concatenates the current <code>Graphics2D</code>
     * <code>Transform</code> with a translated rotation
     * transform.  Subsequent rendering is transformed by a transform
     * which is constructed by translating to the specified location,
     * rotating by the specified radians, and translating back by the same
     * amount as the original translation.  This is equivalent to the
     * following sequence of calls:
     * <pre>
     *          translate(x, y);
     *          rotate(theta);
     *          translate(-x, -y);
     * </pre>
     * Rotating with a positive angle theta rotates points on the positive
     * x axis toward the positive y axis.
     * @param theta the angle of rotation in radians
     * @param x the x coordinate of the origin of the rotation
     * @param y the y coordinate of the origin of the rotation
     */
    public void rotate(double theta, double x, double y) {
        g.rotate(theta, x, y);
    }

    /**
     * Gets this graphics context's current color.
     * @return this graphics context's current color.
     * @see       Color
     * @see       Graphics#setColor(Color)
     */
    public Color getColor() {
        return g.getColor();
    }

    /**
     * Sets this graphics context's current color to the specified
     * color. All subsequent graphics operations using this graphics
     * context use this specified color.
     * @param     c   the new rendering color.
     * @see       Color
     * @see       Graphics#getColor
     */
    public void setColor(Color c) {
        g.setColor(c);
    }

    /**
     * Sets the paint mode of this graphics context to overwrite the
     * destination with this graphics context's current color.
     * This sets the logical pixel operation function to the paint or
     * overwrite mode.  All subsequent rendering operations will
     * overwrite the destination with the current color.
     */
    public void setPaintMode() {
        g.setPaintMode();
    }

    /**
     * Sets the paint mode of this graphics context to alternate between
     * this graphics context's current color and the new specified color.
     * This specifies that logical pixel operations are performed in the
     * XOR mode, which alternates pixels between the current color and
     * a specified XOR color.
     * <p>
     * When drawing operations are performed, pixels which are the
     * current color are changed to the specified color, and vice versa.
     * <p>
     * Pixels that are of colors other than those two colors are changed
     * in an unpredictable but reversible manner; if the same figure is
     * drawn twice, then all pixels are restored to their original values.
     * @param     c1 the XOR alternation color
     */
    public void setXORMode(Color c1) {
        g.setXORMode(c1);
    }

    /**
     * Gets the current font.
     * @return this graphics context's current font.
     * @see       Font
     * @see       Graphics#setFont(Font)
     */
    public Font getFont() {
        return g.getFont();
    }

    /**
     * Sets this graphics context's font to the specified font.
     * All subsequent text operations using this graphics context
     * use this font. A null argument is silently ignored.
     * @param  font   the font.
     * @see     Graphics#getFont
     * @see     Graphics#drawString(String, int, int)
     * @see     Graphics#drawBytes(byte[], int, int, int, int)
     * @see     Graphics#drawChars(char[], int, int, int, int)
     */
    public void setFont(Font font) {
        g.setFont(font);
        upsideDownFont = font.deriveFont(AffineTransform.getRotateInstance(Math.PI));
    }

    /**
     * Gets the font metrics of the current font.
     * @return the font metrics of this graphics
     *                    context's current font.
     * @see       Graphics#getFont
     * @see       FontMetrics
     * @see       Graphics#getFontMetrics(Font)
     */
    public FontMetrics getFontMetrics() {
        return g.getFontMetrics();
    }

    /**
     * Gets the font metrics for the specified font.
     * @return the font metrics for the specified font.
     * @param     f the specified font
     * @see       Graphics#getFont
     * @see       FontMetrics
     * @see       Graphics#getFontMetrics()
     */
    public FontMetrics getFontMetrics(Font f) {
        return g.getFontMetrics(f);
    }

    /**
     * Draws a line, using the current color, between the points
     * <code>(x1,&nbsp;y1)</code> and <code>(x2,&nbsp;y2)</code>
     * in this graphics context's coordinate system.
     * @param   x1  the first point's <i>x</i> coordinate.
     * @param   y1  the first point's <i>y</i> coordinate.
     * @param   x2  the second point's <i>x</i> coordinate.
     * @param   y2  the second point's <i>y</i> coordinate.
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     * Fills the specified rectangle.
     * The left and right edges of the rectangle are at
     * <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>.
     * The top and bottom edges are at
     * <code>y</code> and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
     * The resulting rectangle covers an area
     * <code>width</code> pixels wide by
     * <code>height</code> pixels tall.
     * The rectangle is filled using the graphics context's current color.
     * @param         x   the <i>x</i> coordinate
     *                         of the rectangle to be filled.
     * @param         y   the <i>y</i> coordinate
     *                         of the rectangle to be filled.
     * @param         width   the width of the rectangle to be filled.
     * @param         height   the height of the rectangle to be filled.
     * @see           Graphics#clearRect
     * @see           Graphics#drawRect
     */
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    /**
     * Draws the outline of the specified rectangle.
     * The left and right edges of the rectangle are at
     * <code>x</code> and <code>x&nbsp;+&nbsp;width</code>.
     * The top and bottom edges are at
     * <code>y</code> and <code>y&nbsp;+&nbsp;height</code>.
     * The rectangle is drawn using the graphics context's current color.
     * @param         x   the <i>x</i> coordinate
     *                         of the rectangle to be drawn.
     * @param         y   the <i>y</i> coordinate
     *                         of the rectangle to be drawn.
     * @param         width   the width of the rectangle to be drawn.
     * @param         height   the height of the rectangle to be drawn.
     * @see          Graphics#fillRect
     * @see          Graphics#clearRect
     */
    public void drawRect(int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    /**
     * Clears the specified rectangle by filling it with the background
     * color of the current drawing surface. This operation does not
     * use the current paint mode.
     * <p>
     * Beginning with Java&nbsp;1.1, the background color
     * of offscreen images may be system dependent. Applications should
     * use <code>setColor</code> followed by <code>fillRect</code> to
     * ensure that an offscreen image is cleared to a specific color.
     * @param       x the <i>x</i> coordinate of the rectangle to clear.
     * @param       y the <i>y</i> coordinate of the rectangle to clear.
     * @param       width the width of the rectangle to clear.
     * @param       height the height of the rectangle to clear.
     * @see         Graphics#fillRect(int, int, int, int)
     * @see         Graphics#drawRect
     * @see         Graphics#setColor(Color)
     * @see         Graphics#setPaintMode
     * @see         Graphics#setXORMode(Color)
     */
    public void clearRect(int x, int y, int width, int height) {
        g.clearRect(x, y, width, height);
    }

    /**
     * Draws an outlined round-cornered rectangle using this graphics
     * context's current color. The left and right edges of the rectangle
     * are at <code>x</code> and <code>x&nbsp;+&nbsp;width</code>,
     * respectively. The top and bottom edges of the rectangle are at
     * <code>y</code> and <code>y&nbsp;+&nbsp;height</code>.
     * @param      x the <i>x</i> coordinate of the rectangle to be drawn.
     * @param      y the <i>y</i> coordinate of the rectangle to be drawn.
     * @param      width the width of the rectangle to be drawn.
     * @param      height the height of the rectangle to be drawn.
     * @param      arcWidth the horizontal diameter of the arc
     *                    at the four corners.
     * @param      arcHeight the vertical diameter of the arc
     *                    at the four corners.
     * @see        Graphics#fillRoundRect
     */
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * Fills the specified rounded corner rectangle with the current color.
     * The left and right edges of the rectangle
     * are at <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>,
     * respectively. The top and bottom edges of the rectangle are at
     * <code>y</code> and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
     * @param       x the <i>x</i> coordinate of the rectangle to be filled.
     * @param       y the <i>y</i> coordinate of the rectangle to be filled.
     * @param       width the width of the rectangle to be filled.
     * @param       height the height of the rectangle to be filled.
     * @param       arcWidth the horizontal diameter
     *                     of the arc at the four corners.
     * @param       arcHeight the vertical diameter
     *                     of the arc at the four corners.
     * @see         Graphics#drawRoundRect
     */
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * Draws the outline of an oval.
     * The result is a circle or ellipse that fits within the
     * rectangle specified by the <code>x</code>, <code>y</code>,
     * <code>width</code>, and <code>height</code> arguments.
     * <p>
     * The oval covers an area that is
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * and <code>height&nbsp;+&nbsp;1</code> pixels tall.
     * @param       x the <i>x</i> coordinate of the upper left
     *                     corner of the oval to be drawn.
     * @param       y the <i>y</i> coordinate of the upper left
     *                     corner of the oval to be drawn.
     * @param       width the width of the oval to be drawn.
     * @param       height the height of the oval to be drawn.
     * @see         Graphics#fillOval
     */
    public void drawOval(int x, int y, int width, int height) {
        g.drawOval(x, y, width, height);
    }

    /**
     * Fills an oval bounded by the specified rectangle with the
     * current color.
     * @param       x the <i>x</i> coordinate of the upper left corner
     *                     of the oval to be filled.
     * @param       y the <i>y</i> coordinate of the upper left corner
     *                     of the oval to be filled.
     * @param       width the width of the oval to be filled.
     * @param       height the height of the oval to be filled.
     * @see         Graphics#drawOval
     */
    public void fillOval(int x, int y, int width, int height) {
        g.fillOval(x, y, width, height);
    }

    /**
     * Draws the outline of a circular or elliptical arc
     * covering the specified rectangle.
     * <p>
     * The resulting arc begins at <code>startAngle</code> and extends
     * for <code>arcAngle</code> degrees, using the current color.
     * Angles are interpreted such that 0&nbsp;degrees
     * is at the 3&nbsp;o'clock position.
     * A positive value indicates a counter-clockwise rotation
     * while a negative value indicates a clockwise rotation.
     * <p>
     * The center of the arc is the center of the rectangle whose origin
     * is (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the
     * <code>width</code> and <code>height</code> arguments.
     * <p>
     * The resulting arc covers an area
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
     * <p>
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the
     * start and end of the arc segment will be skewed farther along the
     * longer axis of the bounds.
     * @param        x the <i>x</i> coordinate of the
     *                    upper-left corner of the arc to be drawn.
     * @param        y the <i>y</i>  coordinate of the
     *                    upper-left corner of the arc to be drawn.
     * @param        width the width of the arc to be drawn.
     * @param        height the height of the arc to be drawn.
     * @param        startAngle the beginning angle.
     * @param        arcAngle the angular extent of the arc,
     *                    relative to the start angle.
     * @see         Graphics#fillArc
     */
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * Fills a circular or elliptical arc covering the specified rectangle.
     * <p>
     * The resulting arc begins at <code>startAngle</code> and extends
     * for <code>arcAngle</code> degrees.
     * Angles are interpreted such that 0&nbsp;degrees
     * is at the 3&nbsp;o'clock position.
     * A positive value indicates a counter-clockwise rotation
     * while a negative value indicates a clockwise rotation.
     * <p>
     * The center of the arc is the center of the rectangle whose origin
     * is (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the
     * <code>width</code> and <code>height</code> arguments.
     * <p>
     * The resulting arc covers an area
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
     * <p>
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the
     * start and end of the arc segment will be skewed farther along the
     * longer axis of the bounds.
     * @param        x the <i>x</i> coordinate of the
     *                    upper-left corner of the arc to be filled.
     * @param        y the <i>y</i>  coordinate of the
     *                    upper-left corner of the arc to be filled.
     * @param        width the width of the arc to be filled.
     * @param        height the height of the arc to be filled.
     * @param        startAngle the beginning angle.
     * @param        arcAngle the angular extent of the arc,
     *                    relative to the start angle.
     * @see         Graphics#drawArc
     */
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * Draws a sequence of connected lines defined by
     * arrays of <i>x</i> and <i>y</i> coordinates.
     * Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a point.
     * The figure is not closed if the first point
     * differs from the last point.
     * @param       xPoints an array of <i>x</i> points
     * @param       yPoints an array of <i>y</i> points
     * @param       nPoints the total number of points
     * @see         Graphics#drawPolygon(int[], int[], int)
     * @since JDK1.1
     */
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolyline(xPoints, yPoints, nPoints);
    }

    /**
     * Draws a closed polygon defined by
     * arrays of <i>x</i> and <i>y</i> coordinates.
     * Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a point.
     * <p>
     * This method draws the polygon defined by <code>nPoint</code> line
     * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code>
     * line segments are line segments from
     * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
     * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
     * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>.
     * The figure is automatically closed by drawing a line connecting
     * the final point to the first point, if those points are different.
     * @param        xPoints   a an array of <code>x</code> coordinates.
     * @param        yPoints   a an array of <code>y</code> coordinates.
     * @param        nPoints   a the total number of points.
     * @see          Graphics#fillPolygon
     * @see          Graphics#drawPolyline
     */
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolygon(xPoints, yPoints, nPoints);
    }

    /**
     * Fills a closed polygon defined by
     * arrays of <i>x</i> and <i>y</i> coordinates.
     * <p>
     * This method draws the polygon defined by <code>nPoint</code> line
     * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code>
     * line segments are line segments from
     * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
     * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
     * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>.
     * The figure is automatically closed by drawing a line connecting
     * the final point to the first point, if those points are different.
     * <p>
     * The area inside the polygon is defined using an
     * even-odd fill rule, also known as the alternating rule.
     * @param        xPoints   a an array of <code>x</code> coordinates.
     * @param        yPoints   a an array of <code>y</code> coordinates.
     * @param        nPoints   a the total number of points.
     * @see          Graphics#drawPolygon(int[], int[], int)
     */
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g.fillPolygon(xPoints, yPoints, nPoints);
    }
}
