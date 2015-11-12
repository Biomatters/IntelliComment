package tree.comments;

import bitbucket.models.Comment;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.TextAnnotationGutterProvider;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A window for rendering comments - follows the selected text editor, and tries to match its scroll position.
 */
public class CommentsToolWindowRenderer extends JComponent {


    private Editor editor;

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


        tempDrawingLabel.setOpaque(false);
        tempDrawingLabel.setForeground(JBColor.BLACK);
    }

    private CaretListener caretListener = new CaretListener() {
        @Override
        public void caretPositionChanged(CaretEvent caretEvent) {
            repaint();
        }

        @Override
        public void caretAdded(CaretEvent caretEvent) {

        }

        @Override
        public void caretRemoved(CaretEvent caretEvent) {

        }
    };

    @SuppressWarnings("unused")
    private TextAnnotationGutterProvider gutterProvider = new TextAnnotationGutterProvider() {
        @Nullable
        @Override
        public String getLineText(int i, Editor editor) {
            for(Comment c : getCommentsForFile(editor)) {
                if(Integer.parseInt(c.getLineFrom()) == i+1) {
                    return "COMMENT HERE";
                }
            }
            return null;
        }

        @Nullable
        @Override
        public String getToolTip(int i, Editor editor) {
            for(Comment c : getCommentsForFile(editor)) {
                if(Integer.parseInt(c.getLineFrom()) == i+1) {
                    return c.getContent();
                }
            }
            return null;
        }

        @Override
        public EditorFontType getStyle(int i, Editor editor) {
            return EditorFontType.BOLD;
        }

        @Nullable
        @Override
        public ColorKey getColor(int i, Editor editor) {
            return ColorKey.createColorKey("whoknows", JBColor.BLACK);
        }

        @Nullable
        @Override
        public Color getBgColor(int i, Editor editor) {
            for(Comment c : getCommentsForFile(editor)) {
                if(Integer.parseInt(c.getLineFrom()) == i+1) {
                    return JBColor.PINK;
                }
            }
            return null;
        }

        @Override
        public List<AnAction> getPopupActions(int i, Editor editor) {
            return null;
        }

        @Override
        public void gutterClosed() {

        }
    };

    private VisibleAreaListener visibleAreaListener = visibleAreaEvent -> repaint();//scrollRectToVisible(visibleAreaEvent.getNewRectangle());

    private void setEditor(Editor ed) {
        if(editor != null) {
            editor.getScrollingModel().removeVisibleAreaListener(visibleAreaListener);
            editor.getCaretModel().removeCaretListener(caretListener);
        }
        this.editor = ed;
        editor.getCaretModel().addCaretListener(caretListener);
        editor.getScrollingModel().addVisibleAreaListener(visibleAreaListener);
//        editor.getGutter().registerTextAnnotation(gutterProvider);
        setPreferredSize(new Dimension(100 /*default (basically min) width*/, 100/*editor.getContentComponent().getHeight()*/));
        this.invalidate();
    }

    private JLabel tempDrawingLabel = new JLabel();

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(editor != null) {
            for(RenderableComment c : layoutComments(getCommentsForFile(editor))) {
                c.paint(g2, getWidth(), tempDrawingLabel);
            }
        }
        super.paintComponent(g);
    }

    private List<RenderableComment> layoutComments(List<Comment> comments) {
        List<RenderableComment> commentsLaidOut = new ArrayList<>();
        if(editor != null) {
            int caretLine = editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line+1;
            for (Comment c : comments) {
                int lineY = Integer.parseInt(c.getLineFrom())*editor.getLineHeight() - editor.getLineHeight()/2;
                int scrollY = editor.getScrollingModel().getVerticalScrollOffset();
                int y = lineY-scrollY;
                RenderableComment renderableComment = new RenderableComment(c, y, y, false, false);
                renderableComment.setCursorInLine(renderableComment.lineContainedInComment(caretLine));
                commentsLaidOut.add(renderableComment);
            }
        }
        return commentsLaidOut;
    }


    private List<Comment> getCommentsForFile(Editor editor) {
        ArrayList<Comment> commentList = new ArrayList<>();


        Comment comment1 = new Comment();
        comment1.setContent(editor.getDocument().getText(new TextRange(1, 100)));
        comment1.setLineFrom("20");
        comment1.setLineTo("20");

        commentList.add(comment1);


        return commentList;
    }
}
