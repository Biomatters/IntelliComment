package bitbucket;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class AccessTokenResponse {
    private String accessToken;
    private String scopes;
    private String expires;
    private String refreshToken;
    private String tokenType;

    public AccessTokenResponse() {
        // Empty for json deserialization.
    }

    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getScopes() {
        return scopes;
    }

    @JsonProperty("scopes")
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }


    public String getExpires() {
        return expires;
    }

    @JsonProperty("expires_in")
    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
