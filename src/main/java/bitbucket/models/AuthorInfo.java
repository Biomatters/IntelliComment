package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class AuthorInfo {
    public String username;
    public String first_name;
    public String last_name;
    public Boolean isTeam;
    public String avatar;
    public String resourceUri;

    public Boolean getIsStaff() {
        return isStaff;
    }

    @JsonProperty("is_staff")
    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

    public Boolean isStaff;

    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String displayName;

    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    @JsonProperty("first_name")
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    @JsonProperty("last_name")
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Boolean getIsTeam() {
        return isTeam;
    }

    @JsonProperty("is_team")
    public void setIsTeam(Boolean isTeam) {
        this.isTeam = isTeam;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    @JsonProperty("resource_uri")
    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

}
