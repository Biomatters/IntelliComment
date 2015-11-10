package git;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by the Biomatters and the Webapps Team for the betterment of mankind.
 */
public class Git {
    ExecuteShellCommand com;


    private final String BASH_ORIGIN_URL = "git config remote.origin.url";

    public Git() {
        com = new ExecuteShellCommand();
    }

    public String getCurrentBranch() {
        return findCurrentBranch(com.executeCommand("git branch"));
    }

    private String findCurrentBranch(String allBranches) {
        String[] branches = allBranches.split("\n");
        for (String branch : branches) {
            if (branch.contains("*")) {
                return branch.replace("*", "").trim();
            }
        }
        throw new RuntimeException("Not in a git repository.");
    }

    public String getRepoSlug() {
        // TODO Get the non capturing group below working.
        return getValueFromOriginUrl("\\w+(?:.git)$").replace(".git", "");
    }

    public String getUserName() {
        return getValueFromOriginUrl("([A-Z])\\w+");
    }

    private String getValueFromOriginUrl(String regex) {
        String repoUrl = com.executeCommand(BASH_ORIGIN_URL);

        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(repoUrl);
        m.find();
        return m.group(0);
    }
}
