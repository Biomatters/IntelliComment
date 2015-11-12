package tree;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class BucketDirectoryNode extends PsiDirectoryNode {
    public BucketDirectoryNode(Project project, PsiDirectory value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getTitle() {
        return "hello world";
    }


}
