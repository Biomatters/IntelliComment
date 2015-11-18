package tree;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import tree.comments.CommentsToolWindowRenderer;

import java.awt.*;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class IntelliBucketToolWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        final ContentManager contentManager = toolWindow.getContentManager();

        CommentsToolWindowRenderer commentsToolWindowRenderer = new CommentsToolWindowRenderer(FileEditorManager.getInstance(project));

        JBScrollPane scrollPane = new JBScrollPane(commentsToolWindowRenderer);
        scrollPane.getVerticalScrollBar().setVisible(false);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getViewport().setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        final Content content = contentManager.getFactory().createContent(scrollPane, "Comments", false);
        contentManager.addContent(content);
    }
}
