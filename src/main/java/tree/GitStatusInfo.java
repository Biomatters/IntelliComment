package tree;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 12/11/2015 5:59 PM
 */
public class GitStatusInfo {

    public final String userName;
    public final String repoSlug;
    public final String branch;

    public GitStatusInfo(String userName, String repoSlug, String branch) {
        this.userName = userName;
        this.repoSlug = repoSlug;
        this.branch = branch;
    }
}
