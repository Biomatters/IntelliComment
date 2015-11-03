package bitbucket;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class Config {

    public final static String BITBUCKET_URL = "https://bitbucket.org/api/1.0";

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
}
