package bitbucket;

import bitbucket.models.Comment;
import bitbucket.models.RefreshRequest;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class Config {
    public final static String BITBUCKET_URL = "https://bitbucket.org/api";
    public static final int TOKEN_DURATION_MILLIS = 1000 * 60 * 30;

    private static String APP_KEY = "EkdP4yeMRaMKPBLcBv";
    private static String APP_SECRET = "hRJHdSX4LJyWCsYwyCLetnNUU8Ups4C3";

    /**
     * saves the access and refresh tokens to preferences
     *
     * @param accessToken  self-explanatory
     * @param refreshToken self-explanatory
     */
    public static void saveUserTokensToPreferences(String accessToken, String refreshToken) {
        // expire them in half an hour.
        Date expiry = new Date(new Date().getTime() + TOKEN_DURATION_MILLIS);
        Preferences preferences = getPreferences();
        preferences.put("ACCESS_TOKEN", accessToken);
        preferences.put("REFRESH_TOKEN", refreshToken);
        preferences.put("ACCESS_TOKEN_GENERATED", expiry.getTime() + "");
        preferences.put("REFRESH_TOKEN_GENERATED", expiry.getTime() + "");
    }

    /**
     * @return loads (and verifies) the contents of the access and refresh tokens from Preferences, or returns null
     * if there aren't currently a valid set of tokens stored there.
     */
    public static
    @Nullable
    AccessAndRefreshToken loadUserTokensFromPreferences() {
        AccessAndRefreshToken tokens = loadUserTokensFromPreferencesUnvalidated();
        if (tokens.accessToken == null ||
                tokens.refreshToken == null ||
                tokens.accessTokenExpiry == null ||
                tokens.refreshTokenExpiry == null) {
            setAllTokenFieldsEmpty();
            return null;
        }
        if (tokens.accessTokenExpiry.compareTo(new Date()) <= 0 ||
                tokens.refreshTokenExpiry.compareTo(new Date()) <= 0) {
            setAllTokenFieldsEmpty();
            return null;
        }
        return tokens;
    }

    private static void setAllTokenFieldsEmpty() {
        Preferences preferences = getPreferences();
        preferences.put("ACCESS_TOKEN", "");
        preferences.put("REFRESH_TOKEN", "");
        preferences.put("ACCESS_TOKEN_GENERATED", "");
        preferences.put("REFRESH_TOKEN_GENERATED", "");
    }

    private static AccessAndRefreshToken loadUserTokensFromPreferencesUnvalidated() {
        // todo tidy this up
        Preferences preferences = getPreferences();
        String accessToken = preferences.get("ACCESS_TOKEN", "");
        String refreshToken = preferences.get("REFRESH_TOKEN", "");
        String accessTokenGenerated = preferences.get("ACCESS_TOKEN_GENERATED", "");
        String refreshTokenGenerated = preferences.get("REFRESH_TOKEN_GENERATED", "");

        Date accessTokenGeneratedDate = null;
        if (accessTokenGenerated != null) {
            try {
                accessTokenGeneratedDate = new Date(Long.valueOf(accessTokenGenerated));
            } catch (NumberFormatException e) {
                // it'll stay null
            }
        }

        Date refreshTokenGeneratedDate = null;
        if (accessTokenGenerated != null) {
            try {
                refreshTokenGeneratedDate = new Date(Long.valueOf(refreshTokenGenerated));
            } catch (NumberFormatException e) {
                // it'll stay null
            }
        }

        return new AccessAndRefreshToken(
                accessToken.isEmpty() ? null : accessToken,
                refreshToken.isEmpty() ? null : refreshToken,
                accessTokenGeneratedDate,
                refreshTokenGeneratedDate
        );
    }

    private static Preferences getPreferences() {
        return Preferences.userNodeForPackage(Config.class);
    }

    public static String getAppKey() {
        return APP_KEY;
    }

    public static void setAppKey(String appKey) {
        APP_KEY = appKey;
    }

    public static String getAppSecret() {
        return APP_SECRET;
    }

    public static void setAppSecret(String appSecret) {
        APP_SECRET = appSecret;
    }

    public static class User {

        private static String ACCESS_TOKEN;
        private static String REFRESH_TOKEN;
        private static Date accessTokenExpiry;
        private static Date refreshTokenExpiry;

        public static String getAccessToken() {
            if (ACCESS_TOKEN == null) {
                // Then we need to load the tokens.
                AccessAndRefreshToken accessAndRefreshToken = loadUserTokensFromPreferences();
                if (accessAndRefreshToken != null) {
                    ACCESS_TOKEN = accessAndRefreshToken.accessToken;
                    REFRESH_TOKEN = accessAndRefreshToken.refreshToken;
                    accessTokenExpiry = accessAndRefreshToken.accessTokenExpiry;
                    refreshTokenExpiry = accessAndRefreshToken.refreshTokenExpiry;
                }
            }

            if (ACCESS_TOKEN == null) {
                // Then we need to get a new access token.
                Auth auth = new Auth();
            }

            // If expiring in the next 30 minutes, then we need to get a new one.
            int thirtyMinutes = 30 * 60 * 1000;
            if (Math.abs(accessTokenExpiry.getTime()-new Date().getTime()) <= thirtyMinutes
                    && refreshTokenExpiry.compareTo(new Date()) <= 0) {
                (new Thread() {
                    @Override
                    public void run() {
                        // Create a header of the form "Authorization: Basic {client_id:client_secret}".
                        byte[] secrets = (APP_KEY + ":" + APP_SECRET).getBytes();
                        String base64 = Base64.encodeBase64String(secrets);

                        // Request a new access token using our current refresh token.
                        RefreshRequest refreshRequest = new RefreshRequest(REFRESH_TOKEN);
                        WebTarget target = ClientBuilder.newClient().target(Config.BITBUCKET_URL);
                        target
                                .path("site/oauth2/access_token")
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .header("Authorization", "Basic " + base64)
                                .post(Entity.json(refreshRequest), Comment.class);
                    }
                }).run();
            }

            return ACCESS_TOKEN;
        }

        public static void setAccessToken(String accessToken) {
            ACCESS_TOKEN = accessToken;
        }

        public static void setRefreshToken(String refreshToken) {
            REFRESH_TOKEN = refreshToken;
        }
    }

    public static class AccessAndRefreshToken {
        public final String accessToken;
        public final String refreshToken;
        public final Date accessTokenExpiry;
        public final Date refreshTokenExpiry;

        public AccessAndRefreshToken(String accessToken, String refreshToken, Date accessTokenExpiry, Date refreshTokenExpiry) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.accessTokenExpiry = accessTokenExpiry;
            this.refreshTokenExpiry = refreshTokenExpiry;
        }
    }
}
