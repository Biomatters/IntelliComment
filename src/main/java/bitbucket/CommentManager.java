package bitbucket;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class CommentManager {

    // The path for the pull request, parameter 1 is the userName, parameter 2 is the pull request id.
    private final static String PULL_REQUEST_PATH = "/repositories/%s/%s/pullrequests/%s/comments";
    private final WebTarget rootTarget;
    private final String userName;
    private final String repoSlug;

    /**
     * @param userName the current user's main.bitbucket username.
     */
    public CommentManager(String repoSlug, String userName) {
        // TODO Extract rootTarget out when we add DI.
        rootTarget = ClientBuilder.newClient().target(Config.BITBUCKET_URL);
        this.userName = userName;
        this.repoSlug = repoSlug;
    }

    /**
     * Formats the pull request url by inserting the user's main.bitbucket userName and main.bitbucket pull request id.
     *
     * @param pullRequestId the identifier for the current pull request.
     * @return the complete url.
     */
    public String composePullRequestPath(int pullRequestId) {
        return String.format(PULL_REQUEST_PATH, userName, repoSlug, pullRequestId);
    }

    /**
     * Gets a list of comments for the given pull request by making a GET request to main.bitbucket's api.
     *
     * @param id The pull request id.
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-GETalistofapullrequestcommentsRedDEPRECATED
     */
    public void get(int id) {

        Auth auth = new Auth();

        Invocation.Builder x = rootTarget
                .path(composePullRequestPath(id))
                .request(MediaType.APPLICATION_JSON_TYPE);
//        CommentMeta comments = x.get(CommentMeta.class);

    }
}
