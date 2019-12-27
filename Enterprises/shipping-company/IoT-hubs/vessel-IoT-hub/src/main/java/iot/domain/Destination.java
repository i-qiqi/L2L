package iot.domain;

import lombok.ToString;

@ToString
public class Destination {
    private String name;
    private long anchoringDuration;
    private long dockingDuration;

    public Destination(String name, long anchoringDuration, long dockingDuration) {
        this.name = name;
        this.anchoringDuration = anchoringDuration;
        this.dockingDuration = dockingDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAnchoringDuration() {
        return anchoringDuration;
    }

    public void setAnchoringDuration(long anchoringDuration) {
        this.anchoringDuration = anchoringDuration;
    }

    public long getDockingDuration() {
        return dockingDuration;
    }

    public void setDockingDuration(long dockingDuration) {
        this.dockingDuration = dockingDuration;
    }
}
