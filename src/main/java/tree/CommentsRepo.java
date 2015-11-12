package tree;

import bitbucket.CommentManager;
import bitbucket.models.Comment;
import git.Git;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class CommentsRepo {

    private static List<Comment> comments;

    public static List<Comment> getComments() {
        if (comments == null) {
            Git git = new Git();
            String branch = git.getCurrentBranch();
            String userName = git.getUserName();
            String repoSlug = git.getRepoSlug();

            CommentManager commentManager = new CommentManager(repoSlug, userName, branch);
            comments = commentManager.get(1);
        }
        return comments;
    }
}
