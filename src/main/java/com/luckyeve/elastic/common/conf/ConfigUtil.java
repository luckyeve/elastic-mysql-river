package com.luckyeve.elastic.common.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by lixy on 2016/11/15.
 */
public class ConfigUtil {

    public static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    private static final String LOOKUP_KEY = "config";

    public static Properties getProperties(String fileNamePattern) {
        // deal fileNamePattern
        String pending = lookupKey(LOOKUP_KEY, null);
        if (pending == null || pending.length() == 0)
            throw new IllegalArgumentException("please config parameter 'config' by adding '-Dconfig=deploy' !");
        String fileName = fileNamePattern.replace("{}", pending);
        // read properties
        Properties properties = new Properties();
        if (fileName != null && fileName.length() > 0) {
            InputStream is = null;
            try {
                is = ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
                properties.load(is);
            } catch (Exception e) {
                logger.error("error loading redis configuration file "+fileName+" !", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("file not exists !");
        }
        return properties;
    }


    public static final String lookupKey(String key, String defaultValue) {
        String value = System.getProperty(key, null);
        if (value == null || value.trim().length() == 0) {
            value = System.getenv(key);
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static Integer getInteger(String value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        if (value.length() == 0) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public static Boolean getBoolean(String value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        if (value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public static InetSocketAddress[] getSocketAddress(String address) {
        String[] serverArray = address.split(",");
        List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
        for (String server : serverArray) {
            String[] arr = server.split(":");
            if (arr.length < 2) continue;
            addresses.add(new InetSocketAddress(arr[0], Integer.parseInt(arr[1])));
        }
        return addresses.toArray(new InetSocketAddress[]{});
    }


}
