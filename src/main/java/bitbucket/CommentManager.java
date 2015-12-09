package bitbucket;

import bitbucket.models.Comment;
import bitbucket.models.V2PullRequest;
import bitbucket.models.V2Response;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 *
 * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-POSTanewcomment
 */
public class CommentManager {

    // The path for the pull request, parameter 1 is the userName, parameter 2 is the pull request id.
    private final static String PULL_REQUEST_PATH = "/1.0/repositories/%s/%s/pullrequests/%s/comments";
    private final WebTarget rootTarget;
    private String branch;
    private String repoOwner;
    private String repoSlug;
    private int pullRequestId;

    public CommentManager(String repoSlug, String repoOwner,String branch) {
        rootTarget = ClientBuilder.newClient().target(Config.BITBUCKET_URL);
        this.repoOwner = repoOwner;
        this.repoSlug = repoSlug;
        this.branch = branch;

        getPullRequestId();
    }

    /**
     * Sets the git branch and updates the pull request id.
     * TODO Call this method when IntelliJ's branch chooser fires (ie when the user changes branches).
     *
     * @param branch the current git branch.
     */
    public void setBranch(String branch) {
        this.branch = branch;
        // Now we need to see if there is any open pull requests for this branch.
        getPullRequestId();
    }

    /**
     * Formats the pull request url by inserting the user's main.bitbucket userName and main.bitbucket pull request id.
     *
     * @return the complete url.
     */
    public String composePullRequestPath() {
        return String.format(PULL_REQUEST_PATH, repoOwner, repoSlug, pullRequestId);
    }

    /**
     * Gets the current pull request if there is one, given the current branch.
     * <p>
     * Queries the open pull requests for the current branch via the bitbucket api.
     * Searches these for a branch which matches that passed in. If matches then returns the id for that PR.
     *
     * @return the current pull request's id, or -1 if there is no pull request for this branch
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-423626332.html#pullrequestsResource-GETalistofopenpullrequests
     */
    private void getPullRequestId() {
        V2Response response = rootTarget
                .path(String.format("/2.0/repositories/%s/%s/pullrequests/", repoOwner, repoSlug))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", String.format("Bearer %s", Config.User.getAccessToken()))
                .get(V2Response.class);

        Optional<V2PullRequest> mostRecent = getMostRecentPullRequestForTheCurrentBranch(response);
        mostRecent.ifPresent(pr -> pullRequestId = pr.getId());
    }

    public Optional<V2PullRequest> getMostRecentPullRequestForTheCurrentBranch(V2Response response) {
        // Get all pull requests for this branch.
        return response.getValues()
                .stream()
                .filter(pullRequest -> pullRequest.getSource().getBranch().getName().equals(branch))
                .sorted((p1, p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()))
                .findFirst();
    }


    /**
     * Gets a list of comments for the given pull request by making a GET request to main.bitbucket's api.
     *
     * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-GETalistofapullrequestcommentsRedDEPRECATED
     */
    public List<Comment> get() {
        if (pullRequestId != -1) {
            return makeSafeRequest(() -> rootTarget
                    .path(composePullRequestPath())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                    .get(new GenericType<List<Comment>>() {
                    }));
        } else {
            return Collections.emptyList();
        }
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
        return rootTarget
                .path(pullRequestId + "")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", String.format("Bearer %s", Config.User.ACCESS_TOKEN))
                .post(Entity.json(newComment), Comment.class);
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

    interface Worker<E> {
        E makeRequest();
    }
}
