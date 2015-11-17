package tree.comments;

import bitbucket.models.Comment;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommentLayouter {

    private List<RenderableComment> renderableComments;

    /**
     * @param iconChangeListener Fired when the icon for any comment is loaded;
     */
    public CommentLayouter(List<Comment> comments, PropertyChangeListener iconChangeListener) {
        renderableComments = new ArrayList<>();
        Collections.sort(comments, (o1, o2) -> o1.getLineNumber() - o2.getLineNumber());
        //noinspection Convert2streamapi
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
     * @param editor   used to determine the Y position of a given line
     * @return true if at least one more iteration is needed
     */
    public boolean iterateTowardLine(int line, Editor editor) {
        final AtomicInteger lineY = new AtomicInteger();

        int maxDist = 0;
        try {
            SwingUtilities.invokeAndWait(() -> lineY.set(getYForLineNumber(editor, line)));

            List<Rectangle> existingBounds = new ArrayList<>();

            int selectedCommentIndex = -1;

            //move comments associated with the line
            for (int i = 0; i < renderableComments.size(); i++) {
                RenderableComment comment = renderableComments.get(i);
                if (comment.lineContainedInComment(line)) {
                    int dist = comment.getY() - correctEditorY(comment, lineY.get());
                    maxDist = Math.max(Math.abs(dist), maxDist);
                    comment.setY(comment.getY() - (int) (dist * 0.25));
                    existingBounds.add(comment.getBounds());
                    selectedCommentIndex = i;
                    break;
                }
            }

            updateCommentProperties(line);

            //fit the other comments around them
            for (int i = selectedCommentIndex - 1; i >= 0; i--) {
                RenderableComment comment = renderableComments.get(i);
                comment.setY(getYForComment(editor, comment));
                moveForOverlaps(comment, existingBounds);
            }
            for (int i = selectedCommentIndex + 1; i < renderableComments.size(); i++) {
                RenderableComment comment = renderableComments.get(i);
                comment.setY(getYForComment(editor, comment));
                moveForOverlaps(comment, existingBounds);
            }
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return maxDist > 4;
    }

    public void layoutComments(Editor editor) {
        List<Rectangle> existingBounds = new ArrayList<>();

        int caretLine = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line+1;

        for(RenderableComment c : renderableComments) {
            int lineY = getYForLineNumber(editor, c.getComment().getLineNumber());
            if (editor.hasHeaderComponent() && editor.getHeaderComponent() != null) {
                lineY += editor.getHeaderComponent().getY() + editor.getHeaderComponent().getHeight();
            }


            //find a place for the comment
            c.setLineY(lineY);
            c.setY(correctEditorY(c, lineY));





            moveForOverlaps(c, existingBounds);


            updateCommentProperties(caretLine);
        }
    }

    private int getYForComment(Editor editor, RenderableComment comment) throws InvocationTargetException, InterruptedException {
        final AtomicInteger lineY = new AtomicInteger();
        SwingUtilities.invokeAndWait(() -> lineY.set(getYForLineNumber(editor, comment.getComment().getLineNumber())));
        return lineY.get();
    }

    private int getYForLineNumber(Editor editor, int lineNumber) {
        return editor.logicalPositionToXY(new LogicalPosition(lineNumber, 0)).y - editor.getLineHeight() / 2;
    }

    private int correctEditorY(RenderableComment c, int lineY) {
        //make sure comments don't go off the top...
        if (lineY - c.getBounds().height / 2 < RenderableComment.padding) {
            lineY = lineY + RenderableComment.padding + c.getBounds().height / 2 - lineY;
        }
        return lineY;
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
