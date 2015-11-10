package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public String lineFrom;
    public String lineTo;
    public String destinationRevision;
    public String createdOn;
    // TODO Parse to date.
    public String anchor;
    public Boolean isRepoOwner;
    public Boolean isSpam;
    public String baseRevision;

    public Comment() {
        // Empty for json deserialization.
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

    public String getLineFrom() {
        return lineFrom;
    }

    @JsonProperty("line_from")
    public void setLineFrom(String lineFrom) {
        this.lineFrom = lineFrom;
    }

    public String getLineTo() {
        return lineTo;
    }

    @JsonProperty("line_to")
    public void setLineTo(String lineTo) {
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
}
