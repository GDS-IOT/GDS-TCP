package com.gds.tcp.engine.service;

import org.apache.log4j.Logger;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;

public class HeartBeatSender {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatSender.class);

    private final Map<Channel, Long> deviceIdTSMap = new HashMap<Channel, Long>();

    private static HeartBeatSender instance;

    private HeartBeatSender() {
    }

    public static HeartBeatSender getInstance() {
        if (null == instance){
            instance = new HeartBeatSender();
        }
        return instance;
    }

    // TODO Needs to implement logic

    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<Channel, Long> kv : deviceIdTSMap.entrySet()){
                    kv.getKey().writeAndFlush(new CommandHandler().sendDeviceResponse());
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LOGGER.debug("Error occured while sending"+e.getMessage());
                }
            }
        });


    }


}
