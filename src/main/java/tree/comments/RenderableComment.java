package tree.comments;

import bitbucket.models.Comment;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.MalformedURLException;
import java.net.URL;

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

    private JLabel label;

    public RenderableComment(Comment comment) {
        this.comment = comment;
        this.label = new JLabel("<html><b>"+comment.getAuthorInfo().getDisplayName()+"</b><br>"+comment.getContent()+"</html>");
        label.setOpaque(false);
        label.setForeground(JBColor.BLACK);
        if(comment.getAuthorInfo().getAvatar() != null) {
            try {
                ImageIcon icon = new ImageIcon(new URL(comment.getAuthorInfo().getAvatar()));
                label.setIcon(icon);
                label.setHorizontalAlignment(SwingConstants.LEFT);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
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
        return new Rectangle(2*padding, y-label.getHeight()/2, 3*padding, label.getPreferredSize().height+padding);
    }

    public void paint(Graphics2D g, int containerWidth) {
        int height = label.getPreferredSize().height+padding;
        float lineWidth = cursorInLine ? 2.0f : 1.0f;

        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, lineWidth));

        Shape commentBubble = new RoundRectangle2D.Double(2*padding, y-height/2, containerWidth-3*padding, height, 15, 15);
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


        //hack to scale user icons to reasonable size
        if(label.getIcon() != null && label.getIcon() instanceof ImageIcon) {
            ImageIcon imageIcon = ((ImageIcon)label.getIcon());
            if(imageIcon.getImage().getWidth(null) > 32) {
                label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
            }
        }

        //text for the bubble...
        label.setSize(new Dimension(containerWidth-5*padding, commentHeight-2*padding));
        g.translate(3*padding, y-commentHeight/2+padding);
        label.paint(g);
        g.translate(-3*padding, -y+commentHeight/2-padding);
    }
}