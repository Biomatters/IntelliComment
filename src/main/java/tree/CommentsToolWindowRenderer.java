package tree;

import bitbucket.models.Comment;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.TextAnnotationGutterProvider;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorFontType;
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

    private VisibleAreaListener visibleAreaListener = visibleAreaEvent -> scrollRectToVisible(visibleAreaEvent.getNewRectangle());

    private void setEditor(Editor ed) {
        if(editor != null) {
            editor.getScrollingModel().removeVisibleAreaListener(visibleAreaListener);
        }
        this.editor = ed;
        editor.getScrollingModel().addVisibleAreaListener(visibleAreaListener);
//        editor.getGutter().registerTextAnnotation(gutterProvider);
        setPreferredSize(new Dimension(100 /*default (basically min) width*/, editor.getContentComponent().getHeight()));
        this.invalidate();


    }

    private JLabel tempDrawingLabel = new JLabel();

    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(editor != null) {
            for(Comment c : getCommentsForFile(editor)) {
                int y = Integer.parseInt(c.getLineFrom())*editor.getLineHeight() - editor.getLineHeight()/2;
                int commentHeight = 50;
                int padding = 10;

                //draw the bubble...
                g.setColor(JBColor.WHITE);
                g.fillRoundRect(padding, y-commentHeight/2, getWidth()-2*padding, commentHeight, 15, 15);
                g.setColor(JBColor.GRAY);
                g.drawRoundRect(padding, y-commentHeight/2, getWidth()-2*padding, commentHeight, 15, 15);
                g.setColor(JBColor.WHITE);
                Polygon p = new Polygon(new int[] {padding/2, padding, padding}, new int[] {y, y-padding/2, y+padding/2}, 3);
                g.drawPolygon(p);
                g.fillPolygon(p);
                g.setColor(JBColor.GRAY);
                g.drawLine(padding/2, y, padding, y-padding/2);
                g.drawLine(padding/2, y, padding, y+padding/2);


                //text for the bubble...
                tempDrawingLabel.setText(c.getContent());
                tempDrawingLabel.setSize(new Dimension(getWidth()-4*padding, commentHeight-2*padding));
                g.translate(2*padding, y-commentHeight/2+padding);
                tempDrawingLabel.paint(g);
                g.translate(-2*padding, -y+commentHeight/2-padding);
            }
        }
        super.paintComponent(g);
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
