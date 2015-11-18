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
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

/**
 * A window for rendering comments - follows the selected text editor, and tries to match its scroll position.
 */
public class CommentsToolWindowRenderer extends JBPanel {


    private Editor editor;
    private CommentLayouter commentLayouter;

    public CommentsToolWindowRenderer(FileEditorManager editorManager) {
        commentLayouter = new CommentLayouter(editorManager.getSelectedTextEditor());
        setEditor(editorManager.getSelectedTextEditor());
        this.setLayout(commentLayouter);
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

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
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
                invalidate();
            }
        });
    }

    private Thread centeringThread;

    private boolean suspendNormalLayouting = false;

    private void centerForLine(final int line) {
        if (centeringThread != null && centeringThread.isAlive()) {
            centeringThread.interrupt();
        }

        Runnable r = () -> {
            commentLayouter.setSuspendNormalLayouting(true);
            while (commentLayouter.iterateTowardLine(line)) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    commentLayouter.setSuspendNormalLayouting(false);
                    return;
                }
            }
            commentLayouter.setSuspendNormalLayouting(false);
        };
        centeringThread = new Thread(r, "Moving comment to line");
        centeringThread.start();
    }

    private VisibleAreaListener visibleAreaListener = visibleAreaEvent -> {
        JViewport viewport = getViewport(this);
        if (viewport != null) {
            viewport.setViewPosition(new Point(0, editor.getScrollingModel().getVerticalScrollOffset()));
        }
        repaint();
    };

    private JViewport getViewport(Component component) {
        if (component instanceof JViewport) {
            return (JViewport) component;
        }
        if (component.getParent() != null) {
            return getViewport(component.getParent());
        }
        return null;
    }

    private void setEditor(Editor ed) {
        if(editor != null) {
            editor.getScrollingModel().removeVisibleAreaListener(visibleAreaListener);
            editor.getCaretModel().removeCaretListener(caretListener);
        }
        this.editor = ed;
        commentLayouter.replaceEditor(ed);
        this.removeAll();
        for (Comment c : getCommentsForFile(ed)) {
            if (c.getLineNumber() == 0) {
                continue;
            }
            this.add("" + c.getCommentId(), new RenderableComment(c));
        }
        editor.getCaretModel().addCaretListener(caretListener);
        editor.getScrollingModel().addVisibleAreaListener(visibleAreaListener);
//        editor.getGutter().registerTextAnnotation(gutterProvider);
        setPreferredSize(new Dimension(100 /*default (basically min) width*/, editor.getContentComponent().getHeight()));
        this.invalidate();
    }

    private int lastWidth = 0;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
//        g2.translate(0, -editor.getScrollingModel().getVerticalScrollOffset());
//        if(editor != null) {
//            for(RenderableComment c : commentLayouter.getRenderableComments()) {
//                c.paint(g2, getWidth());
//            }
//        }
        super.paintComponent(g);

//        //force refreshing the layout to ensure that labels don't overlap (their height depends on the width of this component)
//        if (getWidth() != lastWidth) {
//            lastWidth = getWidth();
//            refreshLayout();
//        }
    }

    private List<Comment> getCommentsForFile(Editor editor) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (file != null) {
            String fileName = file.toString();
            return ServiceManager.getService(CommentsService.class).getComments(fileName);
        }
        return Collections.emptyList();
    }
}
