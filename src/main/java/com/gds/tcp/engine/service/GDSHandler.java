package com.gds.tcp.engine.service;

import io.netty.channel.Channel;

public interface GDSHandler {

    public void handleNext(byte []data, Channel channel);
}
