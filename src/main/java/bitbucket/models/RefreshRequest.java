package bitbucket.models;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class RefreshRequest {
    public String refreshToken;
    public String grantType;

    public RefreshRequest(String refreshToken) {
        this.grantType = "refresh_token";
        this.refreshToken = refreshToken;
    }
}
