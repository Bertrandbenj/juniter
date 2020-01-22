package juniter.core.validation;

import juniter.core.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public interface NetValid {

    Logger LOG = LogManager.getLogger(NetValid.class);

    Pattern PUBKEY_PATTERN = Pattern.compile(Constants.Regex.PUBKEY);
    Pattern VALID_IPV4_PATTERN = Pattern.compile(Constants.Regex.IP4, Pattern.CASE_INSENSITIVE);
    Pattern VALID_IPV6_PATTERN = Pattern.compile(Constants.Regex.IP6, Pattern.CASE_INSENSITIVE);
    Pattern VALID_PORT_PATTERN = Pattern.compile(Constants.Regex.PORT, Pattern.CASE_INSENSITIVE);
    Pattern CHECK_IPV6_ADDRESS = Pattern.compile(Constants.Regex.IP6_ADRESS, Pattern.CASE_INSENSITIVE);
    Pattern VALID_DOMAIN_PATTERN = Pattern.compile(Constants.Regex.DOMAIN, Pattern.CASE_INSENSITIVE);
    Pattern VALID_DOMAIN2_PATTERN = Pattern.compile(Constants.Regex.DOMAIN2, Pattern.CASE_INSENSITIVE);

    Pattern VALID_WS2PSTH_PATTERN = Pattern.compile(Constants.Regex.WS2P_SMETHING,
            Pattern.CASE_INSENSITIVE);
    String COMMA = ",";
    String COLON = ":";
    String CLOSE_BRACKET_AND_COLON = "]:";

    private static List<String> getSeperatedList(String data, String delimeter) {
        // return
        // Pattern.compile(delimeter).splitAsStream(data).collect(Collectors.toList());
        return new ArrayList<>(Arrays.asList(data.split(delimeter)));
    }


    static boolean isIPV6Address(String ip) {
        return ip.contains("[") && ip.contains("]");
    }

    static boolean validateDomain(String item) {
        return VALID_DOMAIN_PATTERN.matcher(item).matches();
    }

    static boolean validateDomain2(String item) {
        return VALID_DOMAIN2_PATTERN.matcher(item).matches();
    }

    static boolean validateIP4Address(String ip) {
        return VALID_IPV4_PATTERN.matcher(ip).matches();
    }

    static boolean validateIP6Address(String ip) {
        return VALID_IPV6_PATTERN.matcher(ip).matches();
    }

    static boolean validateIPAddress(String ips) {
        final List<String> ipsList = getSeperatedList(ips, COMMA);
        List<String> ipAndPort = null;
        String ipAddress = null;
        String port = null;
        for (String ip : ipsList) {
            ip = ip.trim();
            if (isIPV6Address(ip)) {
                ipAndPort = getSeperatedList(ip, CLOSE_BRACKET_AND_COLON);
                if (ipAndPort.size() != 2) {
                    LOG.info("Invalid format. Please specify the IP addresses and port numbers again.");
                    return false;
                }
                ipAddress = ipAndPort.get(0).trim().substring(1); // to remove [
                port = ipAndPort.get(1).trim(); // to remove :

                if (!validateIP6Address(ipAddress)) {
                    LOG.info("The specified IVP6 address is invalid. Please specify the IP addresses again.");
                    return false;
                }

            } else {
                ipAndPort = getSeperatedList(ip, COLON);
                if (ipAndPort.size() != 2) {
                    LOG.info("Invalid format. Please specify the IP addresses and port numbers again.");
                    return false;
                }
                ipAddress = ipAndPort.get(0).trim();
                port = ipAndPort.get(1).trim();

                if (!validateIP4Address(ipAddress)) {
                    LOG.info("The specified IPV4 address is invalid. Please specify the IP addresses again.");
                    return false;
                }
            }

            if (!validatePortNumber(port)) {
                LOG.info("The specified port is invalid. Please specify port numbers again.");
                return false;
            }
        }
        return true;
    }

    static boolean validatePortNumber(String port) {
        return VALID_PORT_PATTERN.matcher(port).matches() || "443".equals(port) || "80".equals(port);
    }

    static boolean validatePubkey(String item) {
        return PUBKEY_PATTERN.matcher(item).matches();
    }

    static boolean validateWS2PSTH(String item) {
        return VALID_WS2PSTH_PATTERN.matcher(item).matches();
    }

}
