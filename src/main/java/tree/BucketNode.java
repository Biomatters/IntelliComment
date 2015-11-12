package tree;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class BucketNode extends PsiFileNode {

    private String modifiedTitle;

    public BucketNode(Project project, PsiFile value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    public void setHasComment(Boolean hasComment) {
        //TODO Make "C" a number.
        String icon = hasComment ? "[C]" : "";
        modifiedTitle = String.format("%s%s", getTitle(), icon);
    }

    @Override
    public String getTitle() {
        return modifiedTitle != null ? modifiedTitle : super.getTitle();
    }

    @Override
    public String getName() {
        return "test";
    }
}
