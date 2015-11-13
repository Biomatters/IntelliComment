package tree;

import bitbucket.CommentManager;
import bitbucket.models.Comment;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class CommentsRepo {

    private static List<Comment> comments;

    /**
     * Gets all comments for the entire project (PR).
     *
     * @return The list of comments.
     */
    public static List<Comment> getComments() {
        if (comments == null || comments.size() == 0) {
            GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo();

            if (gitStatusInfo != null) {
                CommentManager commentManager = new CommentManager(gitStatusInfo.repoSlug, gitStatusInfo.repoOwner,
                        gitStatusInfo.branch);
                comments = commentManager.get(174);
            } else {
                comments = Collections.emptyList();
            }
        }
        return comments;
    }

    /**
     * Gets all comments for the given file name.
     *
     * @param fileName The fully defined file name (ie include path: "/src/main/java/bitbucket/myClass.java" etc)
     * @return
     */
    public static List<Comment> getComments(String fileName) {
        List<Comment> comments = getComments();


        final String name = fileName.replace(IntellijUtilities.getCurrentProject().getBaseDir().toString(), "").substring(1);

        List<Comment> filtered = comments.stream().filter(new Predicate<Comment>() {
            @Override
            public boolean test(Comment comment) {

                return comment.getFilename() != null && comment.getFilename().contains(name);
            }
        }).collect(Collectors.toList());

        return filtered;

    }
}
