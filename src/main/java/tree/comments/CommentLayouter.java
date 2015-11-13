package tree.comments;

import bitbucket.models.Comment;
import com.intellij.openapi.editor.Editor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Steve on 13/11/2015.
 */
public class CommentLayouter {

    private List<RenderableComment> renderableComments;

    public CommentLayouter(List<Comment> comments) {
        renderableComments = new ArrayList<>();
        for(Comment c : comments) {
            if(c.getLineTo() > 0 || c.getLineFrom() > 0) {
                renderableComments.add(new RenderableComment(c));
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
            int diff = RenderableComment.commentHeight+RenderableComment.padding;
            while(hasOverlap(existingBounds, c.getBounds())) {
                c.setY(desiredY-diff);
                if(hasOverlap(existingBounds, c.getBounds())) {
                    c.setY(desiredY+diff);
                }
                diff += RenderableComment.commentHeight+RenderableComment.padding;
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
