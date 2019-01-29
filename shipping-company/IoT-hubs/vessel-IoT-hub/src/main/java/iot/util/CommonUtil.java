package iot.util;

import iot.domain.Step;
import iot.lambda.StepPayload;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    public static List<StepPayload> toStepPayloads(List<Step> steps){
        List<StepPayload> stepPayloads = new ArrayList<StepPayload>();
        for(Step step : steps){
            StepPayload stepPayload = new StepPayload(step.getPrePort(), step.getNextPort(), step.getVoyagingDuration() ,
                    step.getAnchoringDuration(), step.getDockingDuration());
            stepPayloads.add(stepPayload);
        }

        return stepPayloads;
    }

}
