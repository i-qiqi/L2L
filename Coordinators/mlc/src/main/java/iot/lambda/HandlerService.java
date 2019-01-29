package iot.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public  String createRendUpdateEvent2manager(HashMap<String, Object> mp) throws IOException {
        JsonNode jsonRoot = objectMapper.readTree(objectMapper.writeValueAsString(mp)).get("context").get("inboundVariables");
        String vid = jsonRoot.get("vid").asText();
        int zoomInVal = jsonRoot.get("zoomInVal").asInt();
        String startTime = jsonRoot.get("startTime").asText();
        ObjectNode payload = objectMapper.createObjectNode();
        Map<String,Object> contextMp = new HashMap<String,Object>();
        contextMp.put("appName" ,"mlc");
        contextMp.put("vid" ,vid);
        contextMp.put("processInstanceId", "ba238744-we70-12e8-ef4w-215f69sec253");
        contextMp.put("processDefinitionId" , "VesselProcess:1:d4053a34-3r23-23e4-9324-1s234c14232w");
        contextMp.put("activityElementId" , "w3321r28-5jd0-1s58-s834-218d3h3n42o9" );
        contextMp.put("rendzvous_port" ,"安庆");
        contextMp.put("warehousing_cost" ,5278.24);
        contextMp.put("postage_cost" ,2315.56);
        contextMp.put("timeStamp" ,8215.56);
        payload.putPOJO("context" , objectMapper.writeValueAsString(contextMp));
        contextMp.put("timeStamp" ,DateUtil.translate2simuDateStr(startTime , new Date().getTime() , zoomInVal));
        return payload.toString();
    }

    public  String createRendUpdateEvent2logistics(HashMap<String, Object> mp) throws IOException {
        JsonNode jsonRoot = objectMapper.readTree(objectMapper.writeValueAsString(mp)).get("context").get("inboundVariables");
        int zoomInVal = jsonRoot.get("zoomInVal").asInt();
        String startTime = jsonRoot.get("startTime").asText();
        ObjectNode payload = objectMapper.createObjectNode();
        Map<String,Object> contextMp = new HashMap<String,Object>();
        contextMp.put("appName" ,"mlc");
        contextMp.put("wid" ,"W51252378");
        contextMp.put("processInstanceId", "sd923864-89ef-a283-edw2-s8d5n3e2345f");
        contextMp.put("processDefinitionId" , "WagonProcess:2:sk7293d5-4b62-347s-8d24-sj5x23ld726s");
        contextMp.put("activityElementId" , null );
        contextMp.put("rendzvous_port" ,"安庆");
        contextMp.put("warehousing_cost" ,1478.24);
        contextMp.put("postage_cost" ,5215.56);
        contextMp.put("timeStamp" ,8215.56);
        payload.putPOJO("context" , objectMapper.writeValueAsString(contextMp));
        contextMp.put("timeStamp" ,DateUtil.translate2simuDateStr(startTime , new Date().getTime() , zoomInVal));
        return payload.toString();
    }
}
