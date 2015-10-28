package tree;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ui.content.AlertIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class TextOnlyTreeStructureProvider implements TreeStructureProvider {

    @NotNull
    @Override
    public Collection<AbstractTreeNode> modify(AbstractTreeNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {

        // TODO Get bitbucket comments here.

        ArrayList<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
        for (AbstractTreeNode child : children) {
//            child.setIcon(new AlertIcon());
            if (child instanceof PsiFileNode) {
                BucketNode psiFileNode = new BucketNode(child.getProject(), ((PsiFileNode) child).getValue(), settings);
                psiFileNode.setHasComment(true);
                nodes.add(psiFileNode);
            }

        }
        return nodes;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
