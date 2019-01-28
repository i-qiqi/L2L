package iot.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Step {
    private List<VesselIoTData> vesselIoTData;
    private String prePort;
    private String nextPort;
    private long voyagingDuration; // time(ms) taken from pre-port to next Port:
    private long anchoringDuration;
    private long dockingDuration;
}
