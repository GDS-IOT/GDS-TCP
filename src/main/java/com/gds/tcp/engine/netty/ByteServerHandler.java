package com.gds.tcp.engine.netty;

import com.gds.tcp.engine.service.MessageHandler;
import org.apache.log4j.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Sujith Ramanathan
 */
public class ByteServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(ByteServerHandler.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Handler Added");
        Channel incoming = ctx.channel();
        GDSNettyChannelGroup.getInstance().add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Handler Gonna removed");
        Channel channelToRemove = ctx.channel();
        GDSNettyChannelGroup.getInstance().remove(channelToRemove);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.debug("Exception occurred ", cause);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Message Received");
        if (msg instanceof byte[]) {
            MessageHandler.getInstance().handleNext((byte[]) msg, ctx.channel());
            logger.debug("Message Processed");
        } else
            logger.debug("The received message is not type of byte[]");
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        logger.debug("Message Received");
//        byte []bb = (byte[]) msg;
//        for (byte b : bb) {
//            logger.debug((int) b + " -- ");
//        }
//        byte[] resp = new byte[2];
//
//        resp[1] = 100;
////        ctx.channel().writeAndFlush(new CommandHandler().sendDeviceResponse());
//        logger.debug("Sent response");
//    }

}