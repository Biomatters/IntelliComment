package tree;

import bitbucket.CommentManager;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import git.Git;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class IntelliBucketTreeStructureProvider implements TreeStructureProvider {

    @NotNull
    @Override
    public Collection<AbstractTreeNode> modify(AbstractTreeNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {

        Git git = new Git();
        String branch = git.getCurrentBranch();
        String userName = git.getUserName();
        String repoSlug = git.getRepoSlug();

        CommentManager commentManager = new CommentManager(repoSlug, userName,branch);
        commentManager.get(1);

        ArrayList<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
        children.stream().filter(child -> child instanceof PsiFileNode).forEach(child -> {
            BucketNode psiFileNode = new BucketNode(child.getProject(), ((PsiFileNode) child).getValue(), settings);
            psiFileNode.setHasComment(true);
            nodes.add(psiFileNode);
        });
        return nodes;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
