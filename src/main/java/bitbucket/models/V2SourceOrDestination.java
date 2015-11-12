package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * TODO: Write some javadoc
 * <p>
 * Created on 13/11/2015 12:43 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class V2SourceOrDestination {
    public V2Branch branch;

    public V2Branch getBranch() {
        return branch;
    }

    @JsonProperty
    public void setBranch(V2Branch branch) {
        this.branch = branch;
    }
}
