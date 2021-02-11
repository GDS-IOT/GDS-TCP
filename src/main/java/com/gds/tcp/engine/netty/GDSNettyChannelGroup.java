package com.gds.tcp.engine.netty;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Iterator;

/**
 * @author Sujith Ramanathan
 */
public class GDSNettyChannelGroup {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final GDSNettyChannelGroup instance = new GDSNettyChannelGroup();

    public static GDSNettyChannelGroup getInstance(){
        return instance;
    }

    private GDSNettyChannelGroup(){}

    public void add(Channel channel) {
        channels.add(channel);
    }

    public void remove(Channel channel) {
        channels.remove(channel);
    }

    public void sendEventsToAll(byte[] data) {
        Iterator<Channel> channelItr = channels.iterator();
        Channel cnl = null;
        while(channelItr.hasNext()){
            cnl = channelItr.next();
            cnl.writeAndFlush(data);
        }
    }
}
