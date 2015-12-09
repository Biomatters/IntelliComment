package bitbucket;

import bitbucket.models.Comment;
import com.intellij.openapi.application.ApplicationManager;
import tree.GitStatusInfo;
import tree.IntellijUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 13/11/2015 11:16 AM
 */
public class CommentsService {

    private static final AtomicReference<List<Comment>> COMMENTS_REF = new AtomicReference<>(null);
    List<Comment> raw;
    private CommentManager commentManager;
    private Updates updates;
    private String projectName = IntellijUtilities.getCurrentProject().getBaseDir().toString();

    {
        // IntelliJ recommends using "executeOnPooledThread", rather than "Thread".
        // @see http://stackoverflow.com/questions/18725340/create-a-background-task-in-intellij-plugin
        ApplicationManager.getApplication().executeOnPooledThread((Runnable) () -> {
            GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo();
            commentManager = new CommentManager(gitStatusInfo.repoSlug, gitStatusInfo.repoOwner, gitStatusInfo.branch);
            // The polling thread will get started when an instance of this class is instantiated via reflection.
            while (true) {
                refreshComments();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<Comment> getComments() {
        return COMMENTS_REF.get();
    }

    /**
     * @param fileName The fully defined file name (ie include path: "/src/main/java/bitbucket/myClass.java" etc)
     * @return all comments for the given file name.
     */
    public List<Comment> getComments(String fileName) {
        List<Comment> comments = getComments();
        if (comments == null) {
            comments = new ArrayList<>();
        }

        final String name = fileName.replace(projectName, "").substring(1);

        return comments.stream().filter(comment -> (comment.getFilename() != null)
                && comment.getFilename().contains(name)).collect(Collectors.toList());

    }

    private void refreshComments() {
        List<Comment> flat = commentManager.get();
        raw = flat;
        List<Comment> hierarchy = buildCommentHierachy(flat.stream().filter(Comment::isRoot).collect(Collectors.toList()));
        COMMENTS_REF.set(hierarchy);
        updates.commentsUpdated(hierarchy);
    }

    /**
     * Builds a list of comments from a flat structure into a hierarchy with children.
     * <p>
     * Ignores comments that are attached to the repo only without line numbers.
     *
     * @param flat Flat list of comments.
     * @return Hierarchical list of comments.
     */
    private List<Comment> buildCommentHierachy(List<Comment> flat) {
        List<Comment> hierarchy = new ArrayList<>();
        flat.stream().forEach(comment -> {
            comment.setChildren(buildCommentHierachy(getChildrenOfComment(comment)));
            hierarchy.add(comment);
        });
        return hierarchy;
    }

    private List<Comment> getChildrenOfComment(Comment parent) {
        return raw.stream().filter(comment -> comment.getParentId() == parent.getCommentId()).collect(Collectors.toList());
    }

    public interface Updates {
        void commentsUpdated(List<Comment> comments);
    }
}
