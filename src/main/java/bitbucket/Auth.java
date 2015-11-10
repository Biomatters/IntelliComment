package bitbucket;

import bitbucket.models.AccessTokenResponse;
import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class Auth {

    private static String ACCESS_TOKEN_URL = "https://bitbucket.org/site/oauth2/access_token";

    public Auth() {
        // Start server to wait for bitbucket response from the OAuth process.
        authenticateUser();

        // Prompt the user to enter their details.
        promptUserForCredentials();
    }

    private static boolean running = false;

    private void promptUserForCredentials() {
        // Prevent multiple requests. TODO What's the best way to do this in java? - Current implementation doesn't work.
        if (!running) {
            if (Desktop.isDesktopSupported()) {
                try {
                    String urlString = String.format("https://bitbucket.org/site/oauth2/authorize?client_id=%s&response_type=code", Config.APP_KEY);
                    Desktop.getDesktop().browse(new URI(urlString));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Starts a simple server for callbacks during OAuth.
     *
     * @see http://rosettacode.org/wiki/Hello_world/Web_server
     */
    private void authenticateUser() {
        Thread listener = new Thread() {
            public void run() {
                // Open a listener on port 5000 and then continuously listen for authentication.
                // TODO Only use this when needed, but for a demo is probably OK as is.
                String authCode = listenForAndGetOAuthCode();

                getAndSaveAccessToken(authCode);
            }
        };
        listener.start();
    }

    /**
     * Get the OAuth code from the callback URL during an OAuth request.
     *
     * @param path
     * @return
     */
    private String getOAuthCodeFromHttpGet(String path) {
        // TODO Improve the regex used so only captures the code, and so the .replace isn't needed.
        Pattern p = Pattern.compile("(?:code=)(\\w)+");
        Matcher m = p.matcher(path);
        m.find();
        return m.group().replace("code=", "");
    }

    public String listenForAndGetOAuthCode() {
        // TODO Improve this approach, maybe using ExecutorCompletionService?
        // @see http://stackoverflow.com/questions/7939257/wait-until-all-threads-finish-their-work-in-java
        Socket sock;
        String code;
        try {
            // Note that the Port=5000 is set in the bitbucket config, and
            // our client must match that here.
            ServerSocket listener = new ServerSocket(5000);
            sock = listener.accept();
            InputStream is = sock.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String urlPath = br.readLine();
            code = getOAuthCodeFromHttpGet(urlPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        // Always need to close the socket.
        try {
            sock.close();
        } catch (IOException ignored) {
            // Fail silently, this is only clean up.
        }
        running = false;
        return code;
    }

    private void getAndSaveAccessToken(String code) {

        Form form = new Form();
        form.param("grant_type", "authorization_code");
        form.param("code", code);

        WebTarget url = ClientBuilder.newClient().target(ACCESS_TOKEN_URL);
        Invocation.Builder builder = url
                .request(MediaType.APPLICATION_JSON_TYPE);

        // Create a header of the form "Authorization: Basic {client_id:client_secret}".
        byte[] secrets = (Config.APP_KEY + ":" + Config.APP_SECRET).getBytes();
        String base64 = Base64.encodeBase64String(secrets);

        AccessTokenResponse response = builder
                .header("Authorization", "Basic " + base64)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), AccessTokenResponse.class);

        // Save the user access and refresh token for use later.
        Config.User.setAccessToken(response.getAccessToken());
        Config.User.setRefreshToken(response.getRefreshToken());
    }
}
