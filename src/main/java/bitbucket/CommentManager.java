package bitbucket;

import bitbucket.models.Comment;
import bitbucket.models.V2PullRequest;
import bitbucket.models.V2Response;
import tree.GitStatusInfo;
import tree.IntellijUtilities;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    // Important to set this to -1, don't change without understanding the consequences (see getPullRequestId).
    private int pullRequestId=-1;

    /**
     * @param repoOwner the current user's main.bitbucket username.
     */
    public CommentManager(String repoSlug, String repoOwner, String branch) {
        // TODO Extract rootTarget out when we add DI.
        rootTarget = ClientBuilder.newClient().target(Config.BITBUCKET_URL);
        this.repoOwner = repoOwner;
        this.repoSlug = repoSlug;
        this.branch = branch;

        this.pullRequestId = getPullRequestId();
    }

    /**
     * Formats the pull request url by inserting the user's main.bitbucket userName and main.bitbucket pull request id.
     *
     * @return the complete url.
     */
    public String composePullRequestPath() {
        return String.format(PULL_REQUEST_PATH, repoOwner, repoSlug, getPullRequestId());
    }

    /**
     * Gets the current pull request if there is one, given the current branch.
     * <p>
     * Queries the open pull requests for the current branch via the bitbucket api.
     * Searches these for a branch which matches that passed in. If matches then returns the id for that PR.
     * TODO If there are multiple for the same branch, will take the most recent open branch.
     *
     * @return the current pull request's id, or -1 if there is no pull request for this branch
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-423626332.html#pullrequestsResource-GETalistofopenpullrequests
     */
    int getPullRequestId() {
        if (pullRequestId == -1) {
            V2Response response = makeSafeRequest(() -> rootTarget
                    .path(String.format("/2.0/repositories/%s/%s/pullrequests/", repoOwner, repoSlug))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                    .get(V2Response.class));

            GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo();
            if (gitStatusInfo != null) {
                for (V2PullRequest pullRequest : response.getValues()) {
                    if (pullRequest.getSource().getBranch().getName().equals(gitStatusInfo.branch)) {
                        return pullRequest.getId();
                    }
                }
            }
            return -1;
        } else {
            return pullRequestId;
        }

    }

    /**
     * Gets a list of comments for the given pull request by making a GET request to main.bitbucket's api.
     *
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-GETalistofapullrequestcommentsRedDEPRECATED
     */
    public List<Comment> get() {
        return makeSafeRequest(() -> rootTarget
                .path(composePullRequestPath())
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
     * @param pullRequestId The pull request id.
     * @param newComment    The comment to post. Only a few fields are needed, see the second constructor.
     */
    public Comment post(int pullRequestId, Comment newComment) {
        return makeSafeRequest(() -> rootTarget
                .path(pullRequestId + "")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                .post(Entity.json(newComment), Comment.class));
    }

    interface Worker<E> {
        E makeRequest();
    }

    public <T> T makeSafeRequest(Worker<T> worker) {
        try {
            return worker.makeRequest();
        } catch (NotAuthorizedException ex) {
            // TODO Improve auth to be a bit more obvious rather than leaving in the constructor.
            Auth auth = new Auth();
            return worker.makeRequest();
        }
    }
}
