package iot.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepPayload {
    private String prePort;
    private String nextPort;
    private long voyagingDuration; // time(ms) taken from pre-port to next Port:
    private long anchoringDuration;
    private long dockingDuration;
}
