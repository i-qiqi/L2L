package iot.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.domain.VesselSchedule;
import iot.util.CommonUtil;
import iot.util.DateUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class HandlerService {
    private ObjectMapper objectMapper;

    public HandlerService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public List<VesselSchedule> computeVesselSchedule(HashMap<String, Object> mp) throws IOException {
        JsonNode jsonRoot = objectMapper.readTree(objectMapper.writeValueAsString(mp)).get("context");
        JsonNode steps = jsonRoot.get("steps");
        String startTime = jsonRoot.get("startTime").asText();
        List<VesselSchedule> res = new ArrayList<VesselSchedule>();
        if(steps.isArray()){
            for(JsonNode step : steps){
                VesselSchedule vesselSchedule = new VesselSchedule();
                long voyagingDuration = step.get("voyagingDuration").asLong();
                long anchoringDuration = step.get("anchoringDuration").asLong();
                long dockingDuration = step.get("dockingDuration").asLong();

                vesselSchedule.setEstimateAnchorBeginTime(DateUtil.strAddMs(startTime , voyagingDuration));
                vesselSchedule.setEstimateDockingBeginTime(DateUtil.strAddMs(startTime , voyagingDuration+dockingDuration));
                vesselSchedule.setEstimateDockingEndTime(DateUtil.strAddMs(startTime , voyagingDuration+anchoringDuration+dockingDuration));
                startTime = vesselSchedule.getEstimateDockingEndTime();
                vesselSchedule.setPrePort(step.get("prePort").asText());
                vesselSchedule.setNextPort(step.get("nextPort").asText());
                res.add(vesselSchedule);
            }
        }

        return res;
    }

    public  String tranformDelayEvent(HashMap<String, Object> mp) throws IOException {
        Event event = new Event("SHIP_PROCESS_DELAY" , "1114");
        ObjectNode payload = objectMapper.createObjectNode();
        payload.putPOJO("event" , objectMapper.writeValueAsString(event));
        JsonNode jsonRoot = objectMapper.readTree(objectMapper.writeValueAsString(mp)).get("context");
        String vid = jsonRoot.get("vid").asText();
        int zoomInVal = jsonRoot.get("zoomInVal").asInt();
        String startTime = jsonRoot.get("startTime").asText();
        int stepIndex = jsonRoot.get("stepIndex").asInt();
        String timeStamp = jsonRoot.get("timeStamp").asText();
        List<VesselSchedule> vesselSchedules = computeVesselSchedule(mp);
        Map<String,Object> ivars = new HashMap<String,Object>();
        ivars.put("vid" , vid);
        ivars.put("zoomInVal" , zoomInVal);
        ivars.put("startTime" , startTime);
        ivars.put("stepIndex" , stepIndex);
        ivars.put("vesselSchedule" , vesselSchedules);

        Map<String,Object> contextMp = new HashMap<String,Object>();
        contextMp.put("inboundVariables" , ivars);
        contextMp.put("outBoundVariables" , Arrays.asList("rendzvous_port" , "timeStamp" , "warehousing_cost" , "postage_cost"));
        contextMp.put("appName" ,"Shipping Company");
        contextMp.put("serviceFullName" ,"coordinate the redezvous port");
        contextMp.put("appVersion" , "v1.0.1");
        contextMp.put("serviceName" , "coodinate-rend-port");
        contextMp.put("serviceVersion" , "v2.0.1");
        contextMp.put("processInstanceId", "ba238744-we70-12e8-ef4w-215f69sec253");
        contextMp.put("processDefinitionId" , "VesselProcess:1:d4053a34-3r23-23e4-9324-1s234c14232w");
        contextMp.put("activityElementId" , "w3321r28-5jd0-1s58-s834-218d3h3n42o9" );
        payload.putPOJO("context" , objectMapper.writeValueAsString(contextMp));

        return payload.toString();
    }
}
