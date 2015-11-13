package bitbucket;

import java.util.prefs.Preferences;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.prefs.Preferences;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class Config {
    // TODO Where should this stuff be stored? XML of some kind?

    public final static String BITBUCKET_URL = "https://bitbucket.org/api";
    public static final int TOKEN_DURATION_MILLIS = 1000 * 60 * 30;

    public static String APP_KEY = "EkdP4yeMRaMKPBLcBv";
    public static String APP_SECRET = "hRJHdSX4LJyWCsYwyCLetnNUU8Ups4C3";

    public static class User {

        public static String getAccessToken() {
            return ACCESS_TOKEN;
        }
        public static void setAccessToken(String accessToken) {
            ACCESS_TOKEN = accessToken;
        }

        public static String ACCESS_TOKEN = "spAcqc1Nj97NU0k3lbZBdD4ym4KQ78cksclOaxoKT-hwCCeeRH8XD_YBIzRJv3yUZhGrRHYzrHpj2EB4iA==";

        public static String getRefreshToken() {
            return REFRESH_TOKEN;
        }

        public static void setRefreshToken(String refreshToken) {
            REFRESH_TOKEN = refreshToken;
        }

        public static String REFRESH_TOKEN = "J8XkwyCyuMcmrDKsD3";

    }

    /**
     * saves the access and refresh tokens to preferences
     * @param accessToken self-explanatory
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
