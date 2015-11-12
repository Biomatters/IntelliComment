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

    private Comment comment;

    private int y;

    private int lineY;

    private boolean expanded;

    private boolean cursorInLine;

    public RenderableComment(Comment comment, int y, int lineY, boolean expanded, boolean cursorInLine) {
        this.comment = comment;
        this.y = y;
        this.lineY = lineY;
        this.expanded = expanded;
        this.cursorInLine = cursorInLine;
    }

    public void paint(Graphics2D g, int containerWidth, JLabel tempDrawingLabel) {
        if(cursorInLine) {
            g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 2.0f));
        }


        int commentHeight = 50;
        int padding = 10;

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
