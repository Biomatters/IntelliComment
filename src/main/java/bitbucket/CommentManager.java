package bitbucket;

import bitbucket.models.Comment;
import bitbucket.models.V2Response;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 *
 * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-POSTanewcomment
 */
public class CommentManager {

    // The path for the pull request, parameter 1 is the userName, parameter 2 is the pull request id.
    private final static String PULL_REQUEST_PATH = "/1.0/repositories/%s/%s/pullrequests/%s/comments";
    private final WebTarget rootTarget;
    private final String repoOwner;
    private final String repoSlug;
    private final String branch;
    private int pullRequestId;

    /**
     * @param repoOwner the current user's main.bitbucket username.
     */
    public CommentManager(String repoSlug, String repoOwner, String branch) {
        // TODO Extract rootTarget out when we add DI.
        rootTarget = ClientBuilder.newClient().target(Config.BITBUCKET_URL);
        this.repoOwner = repoOwner;
        this.repoSlug = repoSlug;
        this.branch = branch;

        // TODO The getCurrentPullRequest method isn't working yet.
        this.pullRequestId = 1;//getCurrentPullRequest();
    }

    /**
     * Formats the pull request url by inserting the user's main.bitbucket userName and main.bitbucket pull request id.
     *
     * @param pullRequestId the identifier for the current pull request.
     * @return the complete url.
     */
    public String composePullRequestPath(int pullRequestId) {
        return String.format(PULL_REQUEST_PATH, repoOwner, repoSlug, pullRequestId);
    }

    /**
     * Gets the current pull request if there is one, given the current branch.
     * <p>
     * Queries the open pull requests for the current branch via the bitbucket api.
     * Searches these for a branch which matches that passed in. If matches then returns the id for that PR.
     * TODO If there are multiple for the same branch, will take the most recent open branch.
     *
     * @return
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-423626332.html#pullrequestsResource-GETalistofopenpullrequests
     */
    int getCurrentPullRequest() {
        V2Response response;
        // TODO Refactor the "makeSafeRequest" and use here, to avoid the duplicate code.
        try {
            response = rootTarget
                    .path(String.format("/2.0/repositories/%s/%s/pullrequests/", repoOwner, repoSlug))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                    .get(V2Response.class);
        } catch (NotAuthorizedException ex) {
            // TODO Improve auth to be a bit more obvious rather than leaving in the constructor.
            Auth auth = new Auth();
            response = rootTarget
                    .path(String.format("/2.0/repositories/%s/%s/pullrequests/", repoOwner, repoSlug))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                    .get(V2Response.class);
        }
        // TODO Find PRs made from the current branch and return the most recent of them.

//        response.getValues().stream().filter(repo ->repo.).collect(V2Response.class);

        return response.getValues().get(0).getId();
    }

    /**
     * Gets a list of comments for the given pull request by making a GET request to main.bitbucket's api.
     *
     * @param id The pull request id.
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-GETalistofapullrequestcommentsRedDEPRECATED
     */
    public List<Comment>  get(int id) {
        return makeSafeRequest((Worker) () -> rootTarget
                .path(composePullRequestPath(id))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                .get(new GenericType<List<Comment>>() {
                }));
    }

    /**
     * Creates a new comment.
     * <p>
     * Either lineFrom or lineTo should be specified for comments on the code.
     * Replies don't need lineFrom or lineTo.
     * <p>
     * NB: Have made successful posts without "anchor" - not sure what this is. The bitbucket website posts an anchor.
     *
     * @param id                  The pull request id.
     * @param content             The comment body.
     * @param destinationRevision The revision hash of the destination file.
     * @param lineFrom            optional line number of the original file.
     * @param lineTo              optional line number of the destination file.
     */
//    public void post(int id, String content, String destinationRevision,String lineTo, String lineFrom) {
//        Comment comments = makeSafeRequest((Worker) () -> rootTarget
//                .path(composePullRequestPath(id))
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
//                .post(Comment.class));
//    }

    interface Worker {
        List<Comment> makeRequest();
    }

    public List<Comment> makeSafeRequest(Worker worker) {
        try {
            return worker.makeRequest();
        } catch (NotAuthorizedException ex) {
            // TODO Improve auth to be a bit more obvious rather than leaving in the constructor.
            Auth auth = new Auth();
            return worker.makeRequest();
        }
    }
}
