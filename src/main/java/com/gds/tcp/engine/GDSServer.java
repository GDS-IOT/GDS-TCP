package com.gds.tcp.engine;

import com.gds.tcp.engine.exception.GDSException;
import com.gds.tcp.engine.netty.GDSServerInitializer;
import com.gds.tcp.engine.scheduler.EventsScheduler;
import com.gds.tcp.engine.utils.GDSUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Timer;

/**
 * @author Sujith Ramanathan
 */
public class GDSServer {

    private static final Logger logger = Logger.getLogger(GDSServer.class);

    private final int port;

    public GDSServer(int port) {
        this.port = port;
    }

    public void initProperties() {
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
        GDSUtils.getInstance().setProperties(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
        logger.debug("Init Properties - Success");
    }

    public void run() {
        initProperties();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new GDSServerInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


//            executeScheduledTasks();
            logger.debug("Server started @ port " + port);
            bootstrap.bind(port).sync().channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (null == args[0]) {
            throw new GDSException("Port number is not defined");
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            port = 0;
        } catch (Exception e) {
            port = 0;
        }
        if (0 == port) {
            throw new GDSException("Port number should be an Integer");
        }
        new GDSServer(port).run();
    }

    private void executeScheduledTasks(){
        Timer timer = new Timer();
        EventsScheduler eventsScheduler = new EventsScheduler();
        timer.schedule(eventsScheduler, 0, (1000 * 300));
        logger.debug("Scheduled tasks triggered");
    }

}