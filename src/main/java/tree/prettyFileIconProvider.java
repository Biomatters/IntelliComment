package tree;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.util.IconLoader;

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

        //Icon getIcon(@NotNull VirtualFile file, @Iconable.IconFlags int flags, @Nullable Project project);
        int help = 0;
        String filePath = file.getPresentableName();
        String fileName = file.getNameWithoutExtension();
        Icon myIcon = IconLoader.getIcon("/icons/comment.png");

        if (myIcon.getIconHeight() != 0){
            return myIcon;
        }

        return null;
        //public static final Icon MavenLogo = load("/images/mavenLogo.png"); // 16x16

        //ImageIcon icon = createImageIcon("images/middle.gif","a pretty but meaningless splat");
    }
}