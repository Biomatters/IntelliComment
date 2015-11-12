package tree;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 12/11/2015 5:59 PM
 */
public class GitStatusInfo {

    public final String repoSlug;
    public final String userName;
    public final String branch;

    public GitStatusInfo(String repoSlug, String userName, String branch) {
        this.repoSlug = repoSlug;
        this.userName = userName;
        this.branch = branch;
    }
}
