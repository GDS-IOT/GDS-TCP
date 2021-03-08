package com.gds.tcp.engine.netty;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Sujith Ramanathan
 */
public class GDSNettyChannelGroup {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final GDSNettyChannelGroup instance = new GDSNettyChannelGroup();

    private static final Map<String, Channel> channelMap = new HashMap<>();

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

    public void mapRFAndChannel(String rfId, Channel channel){
        channelMap.put(rfId, channel);
    }

    public Channel getChannelByRfId(String rfId){
        return channelMap.get(rfId);
    }
}
