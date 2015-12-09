package bitbucket;

import bitbucket.models.Comment;
import tree.GitStatusInfo;
import tree.IntellijUtilities;

import java.util.ArrayList;
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

        // Preload the comments.
        getComments();
    }

    public List<Comment> getComments() {
        if (COMMENTS_REF.get() == null) {
            refreshComments();
        }
        return COMMENTS_REF.get();
    }

    private String projectName = IntellijUtilities.getCurrentProject().getBaseDir().toString();

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

        List<Comment> filtered = comments.stream().filter(new Predicate<Comment>() {
            @Override
            public boolean test(Comment comment) {
                return (comment.getFilename() != null)
                        && comment.getFilename().contains(name);
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
        Thread commentGetterThread = new Thread() {
            @Override
            public void run() {
                GitStatusInfo gitStatusInfo = IntellijUtilities.getGitStatusInfo();
                List<Comment> flat;
                if (gitStatusInfo != null) {
                    CommentManager commentManager = new CommentManager(gitStatusInfo.repoSlug, gitStatusInfo.repoOwner);
                    flat = commentManager.get();
                } else {
                    flat = Collections.emptyList();
                }
                raw = flat;
                List<Comment> hierarchy = buildCommentHierachy(flat.stream().filter(Comment::isRoot).collect(Collectors.toList()));
                COMMENTS_REF.set(hierarchy);
            }
        };
        commentGetterThread.start();


    }

    List<Comment> raw;

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
}
