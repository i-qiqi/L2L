package iot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VesselSchedule {
    private String prePort;
    private String nextPort;
    private String EstimateAnchorBeginTime;
    private String EstimateDockingBeginTime;
    private String EstimateDockingEndTime;
}
