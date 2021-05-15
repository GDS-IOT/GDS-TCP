package com.gds.tcp.engine.constants;

/**
 * @author Sujith Ramanathan
 */
public enum GDSEvents {

    MOTOR_STATUS_EVENT(70, "MOTOR_STATUS_EVENT");

    GDSEvents(int id, String event){
        this.id = id;
        this.event = event;
    }

    private int id;
    private String event;
}
