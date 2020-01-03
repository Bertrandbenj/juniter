package juniter.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ExecUtils {
    private static final Logger LOG = LogManager.getLogger(ExecUtils.class);

    /**
     * Fail safe command execution
     * TODO : streamline that for god sake, no need to have 2 intermediate disk write
     *
     * @param cmd :
     * @return :
     */
    public static Object run(String cmd) {
        try {
            LOG.info("Executing : " + cmd);
            final Process process = new ProcessBuilder("bash", "-c", cmd).start();

            final ArrayList<String> output = new ArrayList<>();
            final BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                output.add(line);
                LOG.info(line);
            }
            // There should really be a timeout here.
            if (0 != process.waitFor())
                return null;

            return output;

        } catch (final Exception e1) {
            LOG.error("executing "+cmd, e1);
        }
        return null;
    }
}
