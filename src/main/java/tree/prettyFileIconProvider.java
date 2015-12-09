package tree;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * TODO: Write some javadoc
 *
 * @author <Megan Kennington>
 *         Created on 12/11/2015 8:08 PM
 */
public class prettyFileIconProvider implements FileIconProvider {
    @Nullable
    @Override
    public Icon getIcon(VirtualFile file, int flags, Project project) {

        //public static final Icon MavenLogo = load("/images/mavenLogo.png"); // 16x16
        String fileName = file.getPresentableName();
        String filePath = file.getPath();
        final Icon[] myIcon = new Icon[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            myIcon[0] = IconLoader.getIcon("/icons/comment.png");
        });
//        List<Comment> comments = CommentsService.getComments(filePath);
//        if (comments.size() == 0) {
//            return myIcon;
//        }
        if (myIcon[0].getIconHeight() != 0) {
            return myIcon[0];
        }
        return null;
    }
}