package com.gds.tcp.engine.service;

import org.apache.log4j.Logger;

public class MessageHandler implements GDSHandler {

    private static MessageHandler instance;

    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class);

    private static final StringBuilder message = new StringBuilder();

    private GDSHandler nextHandler;

    private MessageHandler() {
        nextHandler = KafkaHandler.getInstance();
    }

    public static MessageHandler getInstance() {
        if (null == instance) {
            synchronized (MessageHandler.class) {
                if (null == instance)
                    instance = new MessageHandler();
            }
        }
        return instance;
    }

    public void handleNext(Object data) {
        if (data instanceof byte[]) {
            byte []rawData = (byte[]) data;
            printRawData(rawData);

        } else if (data instanceof String) {
            LOGGER.debug("Message received from Kafka ".concat((String)data));
        }
    }

    private void printRawData(byte []data){
        for(byte b : data){
            message.append((int)b).append(", ");
        }
        LOGGER.debug("Message received from device ".concat(message.toString()));
        message.setLength(0);
    }
}
