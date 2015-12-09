package tree.comments;

import bitbucket.CommentsService;
import bitbucket.models.Comment;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;
import tree.IntellijUtilities;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

/**
 * A window for rendering comments - follows the selected text editor, and tries to match its scroll position.
 */
public class CommentsToolWindowRenderer extends JComponent implements CommentsService.Updates {


    private Editor editor;
    private CommentLayouter commentLayouter;
    PropertyChangeListener iconChangeListener = evt -> refreshLayout();
    private ToolWindow toolWindow;
    private Thread centeringThread;
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
    private VisibleAreaListener visibleAreaListener = visibleAreaEvent -> repaint();
    private int lastWidth = 0;

    public CommentsToolWindowRenderer(ToolWindow toolWindow, FileEditorManager editorManager) {
        this.toolWindow = toolWindow;
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

    private void refreshLayout() {
        SwingUtilities.invokeLater(() -> {
            if (commentLayouter != null) {
                commentLayouter.layoutComments(editor);
                repaint();
            }
        });
    }

    private void centerForLine(int line) {
        if (centeringThread != null && centeringThread.isAlive()) {
            centeringThread.interrupt();
        }

        Runnable r = () -> {
            while (commentLayouter.iterateTowardLine(line, editor)) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        };
        centeringThread = new Thread(r, "Moving comment to line");
        centeringThread.start();
    }

    private void setEditor(Editor ed) {
        if (editor != null) {
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

    private int getYOffset() {
        // This is a bit hacky but ensures that we take any static header components into account (as
        // editor.getHeaderComponent() seems to return null for html files even though a header is present)
        if (!toolWindow.getAnchor().isHorizontal()) {
            Point editorLocation = new Point(0, 0);
            SwingUtilities.convertPointToScreen(editorLocation, editor.getContentComponent());
            Point commentsLocation = new Point(0, 0);
            SwingUtilities.convertPointToScreen(commentsLocation, this);
            return commentsLocation.y - editorLocation.y;
        }
        //the case for if the tool box is docked underneath the editor window
        return editor.getScrollingModel().getVerticalScrollOffset();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(0, -getYOffset());
        if (editor != null) {
            for (RenderableComment c : commentLayouter.getRenderableComments()) {
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

    private List<Comment> getCommentsForFile(Editor editor) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (file != null && IntellijUtilities.isInProject(file)) {
            String fileName = file.toString();
            CommentsService x = ServiceManager.getService(CommentsService.class);
            return x.getComments(fileName);
        }
        return Collections.emptyList();
    }

    @Override
    public void commentsUpdated(List<Comment> comments) {
        repaint();
    }
}
