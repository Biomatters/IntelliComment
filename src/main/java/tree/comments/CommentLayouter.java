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

    public void layoutComments(Editor editor) {
        List<Rectangle> existingBounds = new ArrayList<>();

        int caretLine = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line+1;

        for(RenderableComment c : renderableComments) {
            int lineY = c.getComment().getLineNumber()*editor.getLineHeight() - editor.getLineHeight()/2;
            int scrollY = editor.getScrollingModel().getVerticalScrollOffset();
            int desiredY = lineY-scrollY;

            //find a place for the comment
            c.setLineY(desiredY);
            c.setY(desiredY);


            //make sure comments don't go off the top...
            if (lineY - c.getBounds().height / 2 < RenderableComment.padding) {
                c.setY(desiredY + RenderableComment.padding + c.getBounds().height / 2 - lineY);
            }


            int diff = RenderableComment.padding;
            while(hasOverlap(existingBounds, c.getBounds())) {
                c.setY(desiredY-diff);
                if(hasOverlap(existingBounds, c.getBounds())) {
                    c.setY(desiredY+diff);
                }
                diff++;
            }
            existingBounds.add(c.getBounds());


            //set comment properties based on editor state
            c.setCursorInLine(c.lineContainedInComment(caretLine));
        }
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
