package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class PrRepo {

    public String owner;
    public String slug;


    public String getOwner() {
        return owner;
    }
    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

}
