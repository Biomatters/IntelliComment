package tree.comments;

import bitbucket.models.Comment;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeListener;

public class RenderableComment extends JComponent {

    static final int commentHeight = 50;
    static final int padding = 10;

    private Comment comment;

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
        this.setLayout(new GridLayout(1, 1));
        if (comment.getAuthorInfo() != null && comment.getAuthorInfo().getAvatar() != null) {
            IconLoader.setIcon(comment.getAuthorInfo().getAvatar(), label);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setVerticalTextPosition(SwingConstants.TOP);
        }
        this.add(label);
        this.setBorder(new EmptyBorder(padding, padding, padding, 4 * padding));
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        label.addPropertyChangeListener(propertyName, listener);
    }

    public Comment getComment() {
        return comment;
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
        if(comment.getLineFrom() > 0 && comment.getLineTo() > 0) {
            return lineNumber >= comment.getLineFrom() && lineNumber <= comment.getLineTo();
        }
        return lineNumber == comment.getLineNumber();
    }


    public void paintComponent(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;
        int height = getSize().height;
        float lineWidth = cursorInLine ? 2.0f : 1.0f;
        Color backgroundColor = cursorInLine ? new JBColor(0xe5f1ff, 0x344b67) : JBColor.white;
        Color borderColor = JBColor.gray;

        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, lineWidth));

        Shape commentBubble = new RoundRectangle2D.Double(0, padding / 2, getBounds().width - 3 * padding, height - padding, 15, 15);
        Polygon commentArrow = new Polygon(new int[]{-3 * padding / 2, 1, 1}, new int[]{lineY - getBounds().y, getHeight() / 2 - padding / 2, getHeight() / 2 + padding / 2}, 3);



        //draw the bubble...
        g.setColor(backgroundColor);
        g.fill(commentBubble);
        g.setColor(borderColor);
        g.draw(commentBubble);


        //draw the arrow
        Shape oldClip = g.getClip();
        g.setClip(null);
        g.setColor(backgroundColor);
        g.draw(commentArrow);
        g.fill(commentArrow);
        g.setColor(borderColor);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[1]-1, commentArrow.ypoints[1]);
        g.drawLine(commentArrow.xpoints[0], commentArrow.ypoints[0], commentArrow.xpoints[2]-1, commentArrow.ypoints[2]);
        g.setClip(oldClip);

        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        Shape oldClip = g.getClip();
        g.clipRect(0, padding / 2, getBounds().width - 3 * padding, getSize().height - padding);
        super.paintChildren(g);
        g.setClip(oldClip);
    }
}
