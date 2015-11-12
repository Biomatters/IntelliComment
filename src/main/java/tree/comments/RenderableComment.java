package tree.comments;

import bitbucket.models.Comment;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by Steve on 13/11/2015.
 */
public class RenderableComment {

    static final int commentHeight = 50;
    static final int padding = 10;

    private Comment comment;

    private int y;

    private int lineY;

    private boolean expanded;

    private boolean cursorInLine;
    private int lineFrom;
    private int lineTo;

    public RenderableComment(Comment comment) {
        this.comment = comment;

        lineFrom = Integer.parseInt(comment.getLineFrom());
        lineTo = Integer.parseInt(comment.getLineTo());
    }

    public RenderableComment(Comment comment, int y, int lineY, boolean expanded, boolean cursorInLine) {
        this.comment = comment;
        this.y = y;
        this.lineY = lineY;
        this.expanded = expanded;
        this.cursorInLine = cursorInLine;

        lineFrom = Integer.parseInt(comment.getLineFrom());
        lineTo = Integer.parseInt(comment.getLineTo());
    }

    public int getLineFrom() {
        return lineFrom;
    }

    public int getLineTo() {
        return lineTo;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLineY() {
        return lineY;
    }

    public void setLineY(int lineY) {
        this.lineY = lineY;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isCursorInLine() {
        return cursorInLine;
    }

    public void setCursorInLine(boolean cursorInLine) {
        this.cursorInLine = cursorInLine;
    }

    public boolean lineContainedInComment(int lineNumber) {
        return lineNumber >= lineFrom && lineNumber <= lineTo;
    }

    /**
     * This should be used for the Y bounds only - X bounds are dependent on container width
     */
    public Rectangle getBounds() {
        return new Rectangle(2*padding, y-commentHeight/2, 3*padding, commentHeight);
    }

    public void paint(Graphics2D g, int containerWidth, JLabel tempDrawingLabel) {
        float lineWidth = cursorInLine ? 2.0f : 1.0f;

        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, lineWidth));

        Shape commentBubble = new RoundRectangle2D.Double(2*padding, y-commentHeight/2, containerWidth-3*padding, commentHeight, 15, 15);
        Polygon commentArrow = new Polygon(new int[] {padding/2, 2*padding+1, 2*padding+1}, new int[] {lineY, y-padding/2, y+padding/2}, 3);


        //draw the bubble...
        g.setColor(JBColor.WHITE);
        g.fill(commentBubble);
        g.setColor(JBColor.GRAY);
        g.draw(commentBubble);

        //draw the arrow
        g.setColor(JBColor.WHITE);
        g.draw(commentArrow);
        g.fill(commentArrow);
        g.setColor(JBColor.GRAY);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[1]-1, commentArrow.ypoints[1]);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[2]-1, commentArrow.ypoints[2]);


        //text for the bubble...
        tempDrawingLabel.setText(comment.getContent());
        tempDrawingLabel.setSize(new Dimension(containerWidth-5*padding, commentHeight-2*padding));
        g.translate(3*padding, y-commentHeight/2+padding);
        tempDrawingLabel.paint(g);
        g.translate(-3*padding, -y+commentHeight/2-padding);
    }
}
