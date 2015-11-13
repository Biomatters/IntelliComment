package tree.comments;

import bitbucket.models.Comment;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import tree.CommentsRepo;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A window for rendering comments - follows the selected text editor, and tries to match its scroll position.
 */
public class CommentsToolWindowRenderer extends JComponent {


    private Editor editor;
    private CommentLayouter commentLayouter;

    public CommentsToolWindowRenderer(FileEditorManager editorManager) {
        setEditor(editorManager.getSelectedTextEditor());
        //noinspection deprecation
        editorManager.addFileEditorManagerListener(new FileEditorManagerListener() {
            @Override
            public void fileOpened(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {
                setEditor(fileEditorManager.getSelectedTextEditor());
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {
                editor = null;
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent fileEditorManagerEvent) {
                setEditor(fileEditorManagerEvent.getManager().getSelectedTextEditor());
            }
        });
    }

    PropertyChangeListener iconChangeListener = evt -> refreshLayout();

    private CaretListener caretListener = new CaretListener() {
        @Override
        public void caretPositionChanged(CaretEvent caretEvent) {
            if (commentLayouter.commentExistsForLine(caretEvent.getNewPosition().line + 1)) {
                centerForLine(caretEvent.getNewPosition().line + 1);
            }
            commentLayouter.updateCommentProperties(caretEvent.getNewPosition().line + 1);
            repaint();
        }

        @Override
        public void caretAdded(CaretEvent caretEvent) {

        }

        @Override
        public void caretRemoved(CaretEvent caretEvent) {

        }
    };

    private void refreshLayout() {
        SwingUtilities.invokeLater(() -> {
            if (commentLayouter != null) {
                commentLayouter.layoutComments(editor);
                repaint();
            }
        });
    }

    private Thread centeringThread;

    private void centerForLine(int line) {
        if (centeringThread != null && centeringThread.isAlive()) {
            centeringThread.interrupt();
        }

        final int lineHeight = editor.getLineHeight();
        final int scrollOffset = editor.getScrollingModel().getVerticalScrollOffset();

        Runnable r = () -> {
            while (commentLayouter.iterateTowardLine(line, lineHeight, scrollOffset)) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        };
        centeringThread = new Thread(r, "Moving comment to line");
        centeringThread.start();
    }

    private VisibleAreaListener visibleAreaListener = visibleAreaEvent -> refreshLayout();

    private void setEditor(Editor ed) {
        if(editor != null) {
            editor.getScrollingModel().removeVisibleAreaListener(visibleAreaListener);
            editor.getCaretModel().removeCaretListener(caretListener);
        }
        this.editor = ed;
        editor.getCaretModel().addCaretListener(caretListener);
        editor.getScrollingModel().addVisibleAreaListener(visibleAreaListener);
        commentLayouter = new CommentLayouter(getCommentsForFile(editor), iconChangeListener);
        commentLayouter.layoutComments(editor);
//        editor.getGutter().registerTextAnnotation(gutterProvider);
        setPreferredSize(new Dimension(100 /*default (basically min) width*/, 100/*editor.getContentComponent().getHeight()*/));
        this.invalidate();
    }

    private int lastWidth = 0;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(editor != null) {
            for(RenderableComment c : commentLayouter.getRenderableComments()) {
                c.paint(g2, getWidth());
            }
        }
        super.paintComponent(g);

        //force refreshing the layout to ensure that labels don't overlap (their height depends on the width of this component)
        if (getWidth() != lastWidth) {
            lastWidth = getWidth();
            refreshLayout();
        }
    }

//    private List<RenderableComment> layoutComments(List<Comment> comments) {
//        List<RenderableComment> commentsLaidOut = new ArrayList<>();
//        if(editor != null) {
//            int caretLine = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line+1;
//            for (Comment c : comments) {
//                int lineY = Integer.parseInt(c.getLineFrom())*editor.getLineHeight() - editor.getLineHeight()/2;
//                int scrollY = editor.getScrollingModel().getVerticalScrollOffset();
//                int y = lineY-scrollY;
//                RenderableComment renderableComment = new RenderableComment(c, y, y, false, false);
//                renderableComment.setCursorInLine(renderableComment.lineContainedInComment(caretLine));
//                commentsLaidOut.add(renderableComment);
//            }
//        }
//        return commentsLaidOut;
//    }


    private List<Comment> getCommentsForFile(Editor editor) {
        ArrayList<Comment> commentList = new ArrayList<>();


        Comment comment1 = new Comment();
        comment1.setContent(editor.getDocument().getText(new TextRange(1, 100)));
        comment1.setLineFrom(20);
//        comment1.setLineTo(20);

        Comment comment2 = new Comment();
        comment2.setContent("A second comment");
        comment2.setLineFrom(21);
//        comment2.setLineTo(21);

        Comment comment3 = new Comment();
        comment3.setContent("A third comment");
        comment3.setLineFrom(22);
//        comment3.setLineTo(22);

        commentList.add(comment1);
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);


        List<Comment> comments = null;
        int count = 0;
        while(count < 5) {
            try {
                comments = CommentsRepo.getComments();
                break;
            }
            catch(Throwable ignore) {}
            count++;
        }
        return comments != null ? comments : commentList;
    }
}
