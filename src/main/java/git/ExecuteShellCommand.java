package git;

import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Copied from: http://stackoverflow.com/questions/26830617/java-running-bash-commands
 */
public class ExecuteShellCommand {

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            // TODO Get project directory and feed that in here to replace `~/Documents/Intellibucket/src;`
            p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "cd ~/Documents/Intellibucket/src; " + command});
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

}