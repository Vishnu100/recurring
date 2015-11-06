package com.itranswarp.recurring.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate a 50-char String id that composed by: "timestamp-uuid-hostId".
 * 
 * @author michael
 */
public class IdGenerator {

    static final int HOST_ID = getHostId(getHost()) & 0xfffff;

    static String getHost() {
        String host = System.getenv("COMPUTERNAME");
        if (host == null) {
            host = System.getenv("HOSTNAME");
        }
        if (host == null) {
            try {
                host = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {}
        }
        return host == null ? "" : host;
    }

    static int getHostId(String host) {
        Matcher m = Pattern.compile("^.*?(\\d+)$").matcher(host);
        if (m.matches()) {
            try {
                return Integer.parseInt(m.group(1));
            }
            catch (NumberFormatException e) {}
        }
        return 0;
    }

    /**
     * Generate a 50-char String id that composed by: "timestamp-uuid-hostId":
     * 
     * timestamp: 13-char hex string;
     * uuid: 32-char hex string;
     * hostId: 5-char hex string.
     * 
     * @return 50-char String.
     */
    public static String next() {
        return String.format("%013x%s%05x", System.currentTimeMillis(), UUID.randomUUID().toString().replace("-", ""), HOST_ID);
    }
}
