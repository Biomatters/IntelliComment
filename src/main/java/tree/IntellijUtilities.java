package tree;

import com.intellij.ide.DataManager;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcs.log.VcsUser;
import com.intellij.vcsUtil.VcsUtil;
import git4idea.GitUserRegistry;
import git4idea.GitVcs;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 12/11/2015 3:55 PM
 */
public class IntellijUtilities {

    public static Project getCurrentProject() {
        return CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
    }

    /**
     * @return attempts to return all the necessary pieces of information regarding the current git status of a file -
     * the repo it belongs to, the current branch being displayed, and the username for the repo. Returns null if
     * any of these pieces of information couldn't be obtained.
     */
    public static
    @Nullable
    GitStatusInfo getGitStatusInfo() {

        String user = getCurrentUserName();
        String repositorySlug = null;
        String branch = null;

        Project project = getCurrentProject();
        GitRepository gitRepository = IntellijUtilities.getGitRepositoryForFile(project.getBaseDir());
        if (gitRepository != null) {
            // Determine the branch
            branch = IntellijUtilities.getCurrentGitBranchName(gitRepository);

            // Determine the repo slug
            repositorySlug = determineBitBucketSlug(gitRepository.getInfo().getRemotes());
        }

        // intellij reckons branch can never be null. It clearly can be, though...
        //noinspection ConstantConditions
        if (user == null || repositorySlug == null || branch == null) {
            return null;
        }

        return new GitStatusInfo(user, repositorySlug, branch);
    }

    private static final Pattern BITBUCKET_REPO_PATTERN = Pattern.compile(".*://[^@]*@bitbucket\\..*/([^/]*)\\.git");

    /**
     * Determines the bitbucket repo slug; that is, the project name. Ensures that the url of the remote is a bitbucket url.
     * @param remotes the remotes listed for a git repository.
     * @return the repo slug, if determinable, else null
     */
    public static @Nullable String determineBitBucketSlug(Collection<GitRemote> remotes) {
        Iterator<GitRemote> remotesIter = remotes.iterator();
        if (!remotesIter.hasNext()) {
            return null;
        }
        GitRemote firstRemote = remotesIter.next();
        String firstUrl = firstRemote.getFirstUrl();
        if (firstUrl != null) {
            Matcher matcher = BITBUCKET_REPO_PATTERN.matcher(firstUrl);
            if (matcher.matches()) {
                //noinspection UnnecessaryLocalVariable
                String group = matcher.group(1);
                return group;
            }
        }
        return null;
    }

    /**
     *
     * @return the current user name, if discoverable, else null
     */
    public static @Nullable String getCurrentUserName() {
        // Determine the current user
        GitUserRegistry userRegistry = ServiceManager.getService(getCurrentProject(), GitUserRegistry.class);
        Project project = getCurrentProject();
        VcsUser vcsUser = userRegistry.getOrReadUser(project.getBaseDir());
        if (vcsUser != null) {
            return vcsUser.getName();
        }
        return null;
    }

    /**
     * Returns the git repository for file backing a particular file node Returns null if this isn't applicable.
     *
     * @param virtualFile the file
     * @return the branch name, else null
     */
    public static
    @Nullable
    GitRepository getGitRepositoryForFile(VirtualFile virtualFile) {
        if (virtualFile != null) {
            Project currentProject = IntellijUtilities.getCurrentProject();
            if (currentProject != null) {
                AbstractVcs vcs = VcsUtil.getVcsFor(currentProject, virtualFile);
                if (vcs instanceof GitVcs) {
                    return GitBranchUtil.getCurrentRepository(currentProject);
                }
            }
        }
        return null;
    }

    /**
     * Returns the GitVcs that backs a file, if any, else null
     *
     * @param fileNode the file
     * @return the branch name, else null
     */
    public static
    @Nullable
    GitVcs getGitVcs(PsiFileNode fileNode) {
        VirtualFile virtualFile = fileNode.getVirtualFile();
        if (virtualFile != null) {
            Project currentProject = IntellijUtilities.getCurrentProject();
            if (currentProject != null) {
                AbstractVcs vcs = VcsUtil.getVcsFor(currentProject, virtualFile);
                if (vcs instanceof GitVcs) {
                    return (GitVcs) vcs;
                }
            }
        }
        return null;
    }

    /**
     * Returns the current branch for the given repository, else null
     *
     * @param gitRepository the repository
     * @return see above
     */
    public static
    @Nullable
    String getCurrentGitBranchName(GitRepository gitRepository) {
        if (gitRepository != null) {
            return GitBranchUtil.getDisplayableBranchText(gitRepository);
        }
        return null;
    }
}
