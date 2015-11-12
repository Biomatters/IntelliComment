package tree;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class IntelliBucketToolWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        final ContentManager contentManager = toolWindow.getContentManager();

        CommentsToolWindowRenderer commentsToolWindowRenderer = new CommentsToolWindowRenderer(FileEditorManager.getInstance(project));

        final Content content = contentManager.getFactory().createContent(new JBScrollPane(commentsToolWindowRenderer), "Comments", false);
        contentManager.addContent(content);
    }
}
