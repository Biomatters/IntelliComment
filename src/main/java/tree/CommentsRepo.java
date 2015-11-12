package tree;

import bitbucket.CommentManager;
import bitbucket.models.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class CommentsRepo {

    private static List<Comment> comments;

    public static List<Comment> getComments() {
        if (comments == null) {
            GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo();

            if (gitStatusInfo != null) {
                CommentManager commentManager = new CommentManager(gitStatusInfo.repoSlug, gitStatusInfo.userName,
                        gitStatusInfo.branch);
                commentManager.get(1);
                comments = commentManager.get(1);
            } else {
                comments = Collections.emptyList();
            }

        }
        return comments;
    }
}
