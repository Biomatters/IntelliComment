package tree;

import bitbucket.CommentManager;
import com.intellij.ide.impl.ProjectUtil;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.ProjectRootsUtil;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.update.UpdateEnvironment;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import git4idea.GitVcs;
import git4idea.annotate.GitAnnotationProvider;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
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
        if (parent instanceof PsiFileNode) {
            PsiFileNode fileNode = (PsiFileNode) parent;

            GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo(fileNode);

            if (gitStatusInfo != null) {
                CommentManager commentManager = new CommentManager(gitStatusInfo.repoSlug, gitStatusInfo.userName,
                        gitStatusInfo.branch);
                commentManager.get(1);

                ArrayList<AbstractTreeNode> nodes = new ArrayList<>();
                children.stream().filter(child -> child instanceof PsiFileNode).forEach(child -> {
                    BucketNode psiFileNode = new BucketNode(child.getProject(), ((PsiFileNode) child).getValue(), settings);
                    psiFileNode.setHasComment(true);
                    nodes.add(psiFileNode);
                });
                return nodes;
            }
        }

        return children;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
