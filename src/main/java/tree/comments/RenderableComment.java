package tree.comments;

import bitbucket.models.Comment;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class RenderableComment {

    static final int commentHeight = 50;
    static final int padding = 10;
    static final int subCommentsHeight = 50;

    private Comment comment;

    private int y;

    private int lineY;

    private boolean expanded;

    private boolean cursorInLine;

    private JBLabel label;

    private ArrayList<JBLabel> childCommentLabels;

    public RenderableComment(Comment comment) {
        this.comment = comment;
        this.childCommentLabels = new ArrayList<JBLabel>();
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
            label.setVerticalTextPosition(SwingConstants.TOP);
        }
        for (Comment child : comment.getChildren()) {
            try {
                childCommentLabels.add(new JBLabel("<html><b>" + child.getAuthorInfo().getDisplayName() + "</b><br>" + child.getContentRendered() + "</html>"));
            } catch (NullPointerException ex) {
                childCommentLabels.add(new JBLabel("<html>" + child.getContentRendered() + "</html>"));
            }
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

    @SuppressWarnings("unused")
    public int getLineY() {
        return lineY;
    }

    public void setLineY(int lineY) {
        this.lineY = lineY;
    }

    @SuppressWarnings("unused")
    public boolean isExpanded() {
        return expanded;
    }

    @SuppressWarnings("unused")
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @SuppressWarnings("unused")
    public boolean isCursorInLine() {
        return cursorInLine;
    }

    public void setCursorInLine(boolean cursorInLine) {
        this.cursorInLine = cursorInLine;
    }

    public boolean lineContainedInComment(int lineNumber) {
        if (comment.getLineFrom() > 0 && comment.getLineTo() > 0) {
            return lineNumber >= comment.getLineFrom() && lineNumber <= comment.getLineTo();
        }
        return lineNumber == comment.getLineNumber();
    }

    /**
     * This should be used for the Y bounds only - X bounds are dependent on container width
     */
    public Rectangle getBounds() {
        return new Rectangle(2 * padding, y - label.getHeight() / 2, 3 * padding, label.getPreferredSize().height + padding);
    }

    public void paint(Graphics2D g, int containerWidth) {
        int labelHeight = label.getPreferredSize().height;
        int height = labelHeight + padding + subCommentsHeight;
        float lineWidth = cursorInLine ? 2.0f : 1.0f;
        Color backgroundColor = cursorInLine ? new JBColor(0xe5f1ff, 0x344b67) : JBColor.white;
        Color borderColor = JBColor.gray;

        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, lineWidth));

        Shape commentBubble = new RoundRectangle2D.Double(2 * padding, y - height / 2, containerWidth - 3 * padding, height, 15, 15);
        Polygon commentArrow = new Polygon(new int[]{padding / 2, 2 * padding + 1, 2 * padding + 1}, new int[]{lineY, y - padding / 2, y + padding / 2}, 3);

        // Draw the bubble.
        g.setColor(backgroundColor);
        g.fill(commentBubble);
        g.setColor(borderColor);
        g.draw(commentBubble);

        // Draw the arrow.
        g.setColor(backgroundColor);
        g.draw(commentArrow);
        g.fill(commentArrow);
        g.setColor(JBColor.GRAY);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[1] - 1, commentArrow.ypoints[1]);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[2] - 1, commentArrow.ypoints[2]);

        int childOffset = 0;
        int childHeight = 50;
        for (JBLabel child : childCommentLabels) {
            int childLabelHeight = child.getPreferredSize().height;
            Shape childBubble = new RoundRectangle2D.Double(2 * padding, y + height / 2 + childOffset, containerWidth - 3 * padding, childLabelHeight + padding, 15, 15);
            childOffset += childLabelHeight;
            // Draw the bubble.
            g.setColor(backgroundColor);
            g.fill(childBubble);
            g.setColor(borderColor);
            g.draw(childBubble);

            // Draw the text label.
            child.setSize(new Dimension(containerWidth - 5 * padding, childHeight - 2 * padding));
            int labelCentreX = 3 * padding;
            int labelCentreY = y + height / 2 + childOffset / 2 - padding;
            g.translate(labelCentreX, labelCentreY);
            child.paint(g);
            g.translate(-labelCentreX, -labelCentreY);
        }


        // Hack to scale user icons to reasonable size.
        if (label.getIcon() != null && label.getIcon() instanceof ImageIcon) {
            ImageIcon imageIcon = ((ImageIcon) label.getIcon());
            if (imageIcon.getImage().getWidth(null) > 32) {
                label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
            }
        }

        // Text for the bubble.
        label.setSize(new Dimension(containerWidth - 5 * padding, labelHeight - 2 * padding));
        int labelCentreX = 3 * padding;
        int labelCentreY = y - height / 2 + 2 * padding;
        g.translate(labelCentreX, labelCentreY);
        label.paint(g);
        g.translate(-labelCentreX, -labelCentreY);
    }
}
