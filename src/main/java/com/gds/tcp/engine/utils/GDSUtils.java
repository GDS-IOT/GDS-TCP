package com.gds.tcp.engine.utils;

import com.gds.tcp.engine.exception.GDSException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GDSUtils {

    private static final GDSUtils instance = new GDSUtils();

    private static final Logger LOGGER = Logger.getLogger(GDSUtils.class);

    private Properties properties;

    public static GDSUtils getInstance() {
        return instance;
    }

    public String getGDSProperty(String key) {
        String value = (String)this.properties.getProperty(key);
        if (null == value)
            throw new GDSException("Property not found ".concat(key));

        return value;
    }

    public void setProperties(InputStream is) {
        this.properties = new Properties();
        try {
            this.properties.load(is);
        } catch (IOException e) {
            LOGGER.error("Error while setting properties file. ", e);
        }
    }


}
