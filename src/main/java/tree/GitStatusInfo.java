package tree;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 12/11/2015 5:59 PM
 */
public class GitStatusInfo {

    public final String repoSlug;
    public final String repoOwner;
    public final String branch;

    public GitStatusInfo(String repoSlug, String repoOwner, String branch) {
        this.repoSlug = repoSlug;
        this.repoOwner = repoOwner;
        this.branch = branch;
    }
}
