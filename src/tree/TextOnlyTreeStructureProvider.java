package tree;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.content.AlertIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class TextOnlyTreeStructureProvider implements TreeStructureProvider {

    static {
        System.out.println("Class loaded " + TextOnlyTreeStructureProvider.class.getName() + " " + TextOnlyTreeStructureProvider.class.getPackage());
    }

    @NotNull
    @Override
    public Collection<AbstractTreeNode> modify(AbstractTreeNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {

        System.out.println("It's working!");

        // TODO Get bitbucket comments here.

        ArrayList<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
        for (AbstractTreeNode child : children) {
            // child.setIcon(new AlertIcon());

            if(child instanceof PsiDirectoryNode){
                // TODO Mark as containing children with comments.
            }

            if (child instanceof PsiFileNode) {
                BucketNode psiFileNode = new BucketNode((PsiFileNode) child, settings);
                psiFileNode.setHasComment(true);
                nodes.add(psiFileNode);
            } else {
                nodes.add(child);
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
