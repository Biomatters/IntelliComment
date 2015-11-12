package tree;

import bitbucket.models.Comment;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class IntelliBucketTreeStructureProvider implements TreeStructureProvider {

    @NotNull
    @Override
    public Collection<AbstractTreeNode> modify(AbstractTreeNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {

        ArrayList<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
        children.stream().filter(child -> child instanceof PsiFileNode).forEach(child -> {

            BucketNode psiFileNode = new BucketNode(child.getProject(), ((PsiFileNode) child).getValue(), settings);
            psiFileNode.setHasComment(true);
            nodes.add(psiFileNode);
        });

        children.stream().filter(child -> child instanceof PsiDirectoryNode).forEach(child -> {
            BucketDirectoryNode bucketDirectoryNode = new BucketDirectoryNode(child.getProject(), ((PsiDirectoryNode) child).getValue(), settings);
            child = bucketDirectoryNode;
        });


        return children;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
