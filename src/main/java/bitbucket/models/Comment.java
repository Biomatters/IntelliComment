package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A bitbucket comment, properties are copied from the v1 documentation.
 *
 * @see https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-GETalistofapullrequestcommentsRedDEPRECATED
 */
public class Comment {
    public Boolean isEntityAuthor;
    public int pullRequestId;
    public AuthorInfo authorInfo;
    public PrRepo prRepo;
    public String contentRendered;
    public Boolean deleted;
    public int commentId;
    // TODO Parse to date.
    public String lastUpdated;
    public String filenameHash;
    public String filename;
    public String content;
    public int parentId;
    public Boolean convertMarkup;
    public String comparespec;
    public int lineFrom;
    public int lineTo;
    public String destinationRevision;
    public String createdOn;
    // TODO Parse to date.
    public String anchor;
    public Boolean isRepoOwner;
    public Boolean isSpam;
    public String baseRevision;
    private static AtomicReference<ImageIcon> icon;
    public List<Comment> children;

    public Comment() {
        // Empty for json deserialization.
    }

    /**
     * Used for making post requests.
     *
     * @param id                  The pull request id.
     * @param content             The comment message content.
     * @param destinationRevision ???
     * @param lineTo              optional Start line.
     * @param lineFrom            optional End line.
     */
    public Comment(int id, String content, String destinationRevision, int lineTo, int lineFrom) {
        this.pullRequestId = id;
        this.content = content;
        this.destinationRevision = destinationRevision;
        this.lineTo = lineTo;
        this.lineFrom = lineFrom;
    }


    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        if (this.children == null) {
            children = new ArrayList<>();
        }
        this.children = children;
    }


    /**
     * Whether this is a comment with a line number that is not a reply.
     *
     * @return
     */
    public boolean isRoot() {
        return getLineNumber() != 0;
    }

    /**
     * Returns the given line number. Lines start at index 1 and so 0 represents a comment not attached to a line.
     * Examples are comments directly in the root of a pull request or replies to comments.
     *
     * @return
     */
    public int getLineNumber() {
        return lineFrom != 0 ? lineFrom : lineTo;
    }

    public Boolean getConvertMarkup() {
        return convertMarkup;
    }

    @JsonProperty("convert_markup")
    public void setConvertMarkup(Boolean convertMarkup) {
        this.convertMarkup = convertMarkup;
    }

    public String getComparespec() {
        return comparespec;
    }

    @JsonProperty("comparespec")
    public void setComparespec(String comparespec) {
        this.comparespec = comparespec;
    }

    public Boolean getIsRepoOwner() {
        return isRepoOwner;
    }

    @JsonProperty("is_repo_owner")
    public void setIsRepoOwner(Boolean isRepoOwner) {
        this.isRepoOwner = isRepoOwner;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PrRepo getPrRepo() {
        return prRepo;
    }

    @JsonProperty("pr_repo")
    public void setPrRepo(PrRepo prRepo) {
        this.prRepo = prRepo;
    }

    public Boolean getIsEntityAuthor() {
        return isEntityAuthor;
    }

    @JsonProperty("is_entity_author")
    public void setIsEntityAuthor(Boolean isEntityAuthor) {
        this.isEntityAuthor = isEntityAuthor;
    }

    public int getPullRequestId() {
        return pullRequestId;
    }

    @JsonProperty("pull_request_id")
    public void setPullRequestId(int pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public int getCommentId() {
        return commentId;
    }

    @JsonProperty("comment_id")
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getParentId() {
        return parentId;
    }

    @JsonProperty("parent_id")
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("utc_last_updated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getFilenameHash() {
        return filenameHash;
    }

    @JsonProperty("filename_hash")
    public void setFilenameHash(String filenameHash) {
        this.filenameHash = filenameHash;
    }

    public String getBaseRevision() {
        return baseRevision;
    }

    @JsonProperty("base_rev")
    public void setBaseRevision(String baseRevision) {
        this.baseRevision = baseRevision;
    }

    public String getFilename() {
        return filename;
    }

    @JsonProperty("filename")
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    public String getContentRendered() {
        return contentRendered;
    }

    @JsonProperty("content_rendered")
    public void setContentRendered(String contentRendered) {
        this.contentRendered = contentRendered;
    }

    public AuthorInfo getAuthorInfo() {
        return authorInfo;
    }

    @JsonProperty("author_info")
    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    public int getLineFrom() {
        return lineFrom;
    }

    @JsonProperty("line_from")
    public void setLineFrom(int lineFrom) {
        this.lineFrom = lineFrom;
    }

    public int getLineTo() {
        return lineTo;
    }

    @JsonProperty("line_to")
    public void setLineTo(int lineTo) {
        this.lineTo = lineTo;
    }

    public String getDestinationRevision() {
        return destinationRevision;
    }

    @JsonProperty("dest_rev")
    public void setDestinationRevision(String destinationRevision) {
        this.destinationRevision = destinationRevision;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("utc_created_on")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getAnchor() {
        return anchor;
    }

    @JsonProperty("anchor")
    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Boolean getIsSpam() {
        return isSpam;
    }

    @JsonProperty("is_spam")
    public void setIsSpam(Boolean isSpam) {
        this.isSpam = isSpam;
    }


    public ImageIcon getIcon() {
        if (icon == null) {
            icon = new AtomicReference<>();
            Thread iconGetter = new Thread() {
                @Override
                public void run() {
                    try {
                        icon.set(new ImageIcon(new URL(authorInfo.avatar)));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            };
            iconGetter.start();

        }
        return icon.get();
    }
}
