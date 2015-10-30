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

    public BucketNode(PsiFileNode psiFileNode,ViewSettings viewSettings) {
        super(psiFileNode.getProject(), psiFileNode.getValue(), viewSettings);
    }

    public void setHasComment(Boolean hasComment) {
        String icon = hasComment ? "[C]" : "";
        modifiedTitle = String.format("%s%s", super.getTitle(), icon);
    }

    @Override
    public String getTitle() {
        return modifiedTitle != null ? modifiedTitle : super.getTitle();
    }
}
