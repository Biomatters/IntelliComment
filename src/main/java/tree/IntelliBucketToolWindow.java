package tree;

import bitbucket.CommentManager;
import bitbucket.models.Comment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import git.Git;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class IntelliBucketToolWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        final ContentManager contentManager = toolWindow.getContentManager();

        JBList list = new JBList();
        list.setListData(CommentsRepo.getComments().toArray());

        list.setCellRenderer((list1, value, index, isSelected, cellHasFocus) -> {

            Comment comment = (Comment)value;
            JLabel label = new JLabel();
            label.setText(comment.getContent());

            return label;
        });

        list.setBackground(Color.orange);


        final Content content = contentManager.getFactory().createContent(list, "Comments", false);
        contentManager.addContent(content);
    }
}
