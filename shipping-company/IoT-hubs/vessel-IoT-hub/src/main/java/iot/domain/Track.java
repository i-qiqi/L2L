package iot.domain;

import lombok.Data;
import sun.security.krb5.internal.crypto.Des;

import java.util.List;

@Data
public class Track {
    private String vid;
    private String startTimeStamp;
    private int zoomInVal = 1000;
    private int stepIndex = 0;
    private String status = VesselIoTStatus.START;
    private List<Step>  steps;
}
