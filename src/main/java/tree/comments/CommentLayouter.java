package tree.comments;

import bitbucket.models.Comment;
import com.intellij.openapi.editor.Editor;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentLayouter {

    private List<RenderableComment> renderableComments;

    /**
     * @param iconChangeListener Fired when the icon for any comment is loaded;
     */
    public CommentLayouter(List<Comment> comments, PropertyChangeListener iconChangeListener) {
        renderableComments = new ArrayList<>();
        Collections.sort(comments, (o1, o2) -> o1.getLineNumber() - o2.getLineNumber());
        for(Comment c : comments) {
            if(c.getLineTo() > 0 || c.getLineFrom() > 0) {
                RenderableComment e = new RenderableComment(c);
                e.addPropertyChangeListener("icon", iconChangeListener);
                renderableComments.add(e);
            }
        }
    }

    public boolean commentExistsForLine(int line) {
        for (RenderableComment comment : renderableComments) {
            if (comment.getComment().getLineNumber() == line) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gradually moves comments associated with the given line towards their natural position.
     *
     * @param line         The line number
     * @param lineHeight   height of each line in the editor
     * @return true if at least one more iteration is needed
     */
    public boolean iterateTowardLine(int line, int lineHeight/*, int scrollOffset*/) {
        int lineY = line * lineHeight - lineHeight / 2;

        int maxDist = 0;

        List<Rectangle> existingBounds = new ArrayList<>();

        //move comments associated with the line
        for (RenderableComment comment : renderableComments) {
            if (comment.lineContainedInComment(line)) {
                int dist = comment.getY() - lineY;
                maxDist = Math.max(Math.abs(dist), maxDist);
                comment.setY(comment.getY() - (int) (dist * 0.25));
                existingBounds.add(comment.getBounds());
            }
        }

        updateCommentProperties(line);

        //fit the other comments around them
        for (RenderableComment comment : renderableComments) {
            if (comment.getComment().getLineNumber() != line) {
                comment.setY(comment.getComment().getLineNumber() * lineHeight - lineHeight / 2);
                moveForOverlaps(comment, existingBounds);
            }
        }
        return maxDist > 4;
    }

    public void layoutComments(Editor editor) {
        List<Rectangle> existingBounds = new ArrayList<>();

        int caretLine = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line+1;

        for(RenderableComment c : renderableComments) {
            int lineY = c.getComment().getLineNumber()*editor.getLineHeight() - editor.getLineHeight()/2;

            //find a place for the comment
            c.setLineY(lineY);
            c.setY(lineY);


            //make sure comments don't go off the top...
            if (lineY - c.getBounds().height / 2 < RenderableComment.padding) {
                c.setY(lineY + RenderableComment.padding + c.getBounds().height / 2 - lineY);
            }


            moveForOverlaps(c, existingBounds);


            updateCommentProperties(caretLine);
        }
    }

    public void updateCommentProperties(int caretLine) {
        for (RenderableComment c : renderableComments) {
            //set comment properties based on editor state
            c.setCursorInLine(c.lineContainedInComment(caretLine));
        }
    }

    /**
     * Moves the given comment such that it doesn't overlap any existing comments
     */
    private void moveForOverlaps(RenderableComment c, List<Rectangle> existingBounds) {
        int desiredY = c.getY();
        int diff = RenderableComment.padding;
        while (hasOverlap(existingBounds, c.getBounds())) {
            c.setY(desiredY - diff);
            if (hasOverlap(existingBounds, c.getBounds())) {
                c.setY(desiredY + diff);
            }
            diff++;
        }
        existingBounds.add(c.getBounds());
    }

    private boolean hasOverlap(List<Rectangle> existingBounds, Rectangle candidateBounds) {
        for(Rectangle bounds : existingBounds) {
            if(bounds.intersects(candidateBounds)) {
                return true;
            }
        }

        return false;
    }


    public List<RenderableComment> getRenderableComments() {
        return Collections.unmodifiableList(renderableComments);
    }
}
