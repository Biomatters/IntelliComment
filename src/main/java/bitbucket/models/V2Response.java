package bitbucket.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class V2Response {
    public int pageLength;
    public List<V2PullRequest> values;

    public int getPageLength() {
        return pageLength;
    }

    @JsonProperty("pagelen")
    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public List<V2PullRequest> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<V2PullRequest> values) {
        this.values = values;
    }


}
