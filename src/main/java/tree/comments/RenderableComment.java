package tree.comments;

import bitbucket.models.Comment;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeListener;

public class RenderableComment {

    static final int commentHeight = 50;
    static final int padding = 10;

    private Comment comment;

    private int y;

    private int lineY;

    private boolean expanded;

    private boolean cursorInLine;

    private JBLabel label;

    public RenderableComment(Comment comment) {
        this.comment = comment;
        try {
            this.label = new JBLabel("<html><b>" + comment.getAuthorInfo().getDisplayName() + ": " + comment.getLineNumber() + "</b><br>" + comment.getContentRendered() + "</html>");
        } catch (NullPointerException ex) {
            this.label = new JBLabel("<html>" + comment.getContentRendered() + "</html>");
        }
        label.setOpaque(false);
        label.setForeground(JBColor.BLACK);
        if (comment.getAuthorInfo() != null && comment.getAuthorInfo().getAvatar() != null) {
            IconLoader.setIcon(comment.getAuthorInfo().getAvatar(), label);
            label.setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        label.addPropertyChangeListener(propertyName, listener);
    }

    public Comment getComment() {
        return comment;
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
        if(comment.getLineFrom() > 0 && comment.getLineTo() > 0) {
            return lineNumber >= comment.getLineFrom() && lineNumber <= comment.getLineTo();
        }
        return lineNumber == comment.getLineNumber();
    }

    /**
     * This should be used for the Y bounds only - X bounds are dependent on container width
     */
    public Rectangle getBounds() {
        return new Rectangle(2 * padding, y - label.getPreferredSize().height / 2 - padding, 3 * padding, label.getPreferredSize().height + 2 * padding);
    }

    public void paint(Graphics2D g, int containerWidth) {
        int height = label.getPreferredSize().height+padding;
        float lineWidth = cursorInLine ? 2.0f : 1.0f;
        Color backgroundColor = cursorInLine ? new JBColor(0xe5f1ff, 0x344b67) : JBColor.white;
        Color borderColor = JBColor.gray;

        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, lineWidth));

        Shape commentBubble = new RoundRectangle2D.Double(2*padding, y-height/2, containerWidth-3*padding, height, 15, 15);
        Polygon commentArrow = new Polygon(new int[] {padding/2, 2*padding+1, 2*padding+1}, new int[] {lineY, y-padding/2, y+padding/2}, 3);


        //draw the bubble...
        g.setColor(backgroundColor);
        g.fill(commentBubble);
        g.setColor(borderColor);
        g.draw(commentBubble);

        //draw the arrow
        g.setColor(backgroundColor);
        g.draw(commentArrow);
        g.fill(commentArrow);
        g.setColor(borderColor);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[1]-1, commentArrow.ypoints[1]);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[2]-1, commentArrow.ypoints[2]);


        //text for the bubble...
        label.setSize(new Dimension(containerWidth-5*padding, commentHeight-2*padding));
        g.translate(3*padding, y-commentHeight/2+padding);
        label.paint(g);
        g.translate(-3*padding, -y+commentHeight/2-padding);
    }
}
