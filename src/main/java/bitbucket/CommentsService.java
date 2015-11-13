package bitbucket;

import bitbucket.models.Comment;
import tree.GitStatusInfo;
import tree.IntellijUtilities;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 13/11/2015 11:16 AM
 */
public class CommentsService {

    private static final AtomicReference<List<Comment>> COMMENTS_REF = new AtomicReference<>(null);

    // This will get called when an instance of this class is instantiated via reflection.
    {
        Thread pollingThread = new Thread(() -> {
            while (true) {
                refreshComments();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    public List<Comment> getComments() {
        if (COMMENTS_REF.get() == null) {
            refreshComments();
        }
        return COMMENTS_REF.get();
    }

    /**
     * Gets all comments for the given file name.
     *
     * @param fileName The fully defined file name (ie include path: "/src/main/java/bitbucket/myClass.java" etc)
     * @return
     */
    public List<Comment> getComments(String fileName) {
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

    public List<Comment> getCommentsAndRefresh() {
        refreshComments();
        return getComments();
    }

    private void refreshComments() {

        // This needs to be done in a separate thread as Swing stuff will be coming through here

        GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo();

        List<Comment> comments;
        if (gitStatusInfo != null) {
            CommentManager commentManager = new CommentManager(gitStatusInfo.repoSlug, gitStatusInfo.repoOwner,
                    gitStatusInfo.branch);
            int currentPullRequest = commentManager.getCurrentPullRequest();
            comments = commentManager.get(currentPullRequest);
        } else {
            comments = Collections.emptyList();
        }
        COMMENTS_REF.set(comments);
    }
}
