package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 13/11/2015 12:45 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class V2Branch {

    public String name;

    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }
}
