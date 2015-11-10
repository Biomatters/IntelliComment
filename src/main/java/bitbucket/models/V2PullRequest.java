package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class V2PullRequest {
    public String description;
    public String links;
    public String title;
    public Boolean closeSourceBranch;
    public String mergeCommit;
    // TODO Not really a string, hoping to ignore.
    public String destination;
    public String reason;
    public String closedBy;
    public String state;
    // TODO Not really a string, hoping to ignore.
    public String author;
    // TODO Parse to java date.
    public String createdOn;
    public String updatedOn;
    public String type;
    public int id;

    public String getDescription() {
        return description;
    }

    @JsonIgnore
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinks() {
        return links;
    }

    @JsonIgnore
    @JsonProperty("links")
    public void setLinks(String links) {
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    @JsonIgnore
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCloseSourceBranch() {
        return closeSourceBranch;
    }

    @JsonIgnore
    @JsonProperty("close_source_branch")
    public void setCloseSourceBranch(Boolean closeSourceBranch) {
        this.closeSourceBranch = closeSourceBranch;
    }

    public String getMergeCommit() {
        return mergeCommit;
    }

    @JsonIgnore
    @JsonProperty("merge_commit")
    public void setMergeCommit(String mergeCommit) {
        this.mergeCommit = mergeCommit;
    }

    public String getDestination() {
        return destination;
    }

    @JsonIgnore
    @JsonProperty("destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReason() {
        return reason;
    }

    @JsonIgnore
    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClosedBy() {
        return closedBy;
    }

    @JsonIgnore
    @JsonProperty("closed_by")
    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getState() {
        return state;
    }

    @JsonIgnore
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    public String getAuthor() {
        return author;
    }

    @JsonIgnore
    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    @JsonIgnore
    @JsonProperty("created_on")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    @JsonIgnore
    @JsonProperty("updated_on")
    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getType() {
        return type;
    }

    @JsonIgnore
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

}
