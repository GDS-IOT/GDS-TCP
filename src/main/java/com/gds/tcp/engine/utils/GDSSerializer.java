package com.gds.tcp.engine.utils;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class GDSSerializer<T> implements Serializer<T> {

    private Logger LOGGER = Logger.getLogger(GDSSerializer.class);

    public void configure(Map configs, boolean isKey) {
    }

    public byte[] serialize(String topic, T data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("Error occurred while serialization GDSData", e);
            try {
                baos.close();
            } catch (IOException e1) {
                LOGGER.error("Error occurred while closing baos", e1);
            }
        }
        return null;
    }

    public void close() {

    }

}
