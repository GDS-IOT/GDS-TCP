package com.gds.tcp.engine.scheduler;



import com.gds.tcp.engine.netty.GDSNettyChannelGroup;
import com.gds.tcp.engine.utils.McuCommandGenerator;
import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * @author Sujith Ramanathan
 */
public class EventsScheduler extends TimerTask {

    private GDSNettyChannelGroup channelGroup = GDSNettyChannelGroup.getInstance();

    private Logger LOGGER = Logger.getLogger(EventsScheduler.class);

    public EventsScheduler(){
        LOGGER.debug("EventScheduler created");
    }

    @Override
    public void run() {
        channelGroup.sendEventsToAll(McuCommandGenerator.getInstance().getStaticMcuCommand());
    }

}
