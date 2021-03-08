package com.gds.tcp.engine.service;

import io.netty.channel.Channel;
import org.apache.log4j.Logger;

public class MessageHandler implements GDSHandler {

    private static MessageHandler instance;

    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class);

    private static final StringBuilder message = new StringBuilder();

    private GDSHandler kafkaHandler;
    private GDSHandler rfHandler;

    private MessageHandler() {
        kafkaHandler = KafkaHandler.getInstance();
        rfHandler = RFDeviceCommandHandlerImpl.getInstance();
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

    public void handleNext(byte[] data, Channel channel) {
        printRawData(data);
        kafkaHandler.handleNext(data, channel);
    }

    private void sendToRespectiveHandler(byte[] data, Channel channel) {
        if (data[0] != -1) {
            LOGGER.debug("Event Received from Device");
            kafkaHandler.handleNext(data, channel);
        } else {
            LOGGER.debug("Event Received from API Component");
            rfHandler.handleNext(data, channel);
        }
    }

    private void printRawData(byte[] data) {
        for (byte b : data) {
            message.append((int) b).append(", ");
        }
        LOGGER.debug("Message received from device ".concat(message.toString()));
        message.setLength(0);
    }
}
