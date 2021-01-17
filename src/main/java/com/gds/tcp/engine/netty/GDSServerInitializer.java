package com.gds.tcp.engine.netty;

import com.gds.tcp.engine.netty.ByteServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;

/**
 *
 * @author Sujith Ramanathan
 *
 */
public class GDSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel)throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

//		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//
//		pipeline.addLast("decoder", new StringDecoder());
//		pipeline.addLast("encoder", new StringEncoder());
//		pipeline.addLast("handler", new ChatServerHandler());

        pipeline.addLast("decoder", new ByteArrayDecoder());
        pipeline.addLast("encoder", new ByteArrayEncoder());
        pipeline.addLast("handler", new ByteServerHandler());
    }

}