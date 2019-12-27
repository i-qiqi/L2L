package iot.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.VesselIoTSpringBootApplication;
import iot.domain.Track;
import iot.domain.VesselIoTData;
import iot.util.CommonUtil;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class LambdaService {
    private Logger logger = LoggerFactory.getLogger(VesselIoTSpringBootApplication.class);

    private RestTemplate restTemplate;
    private String lambda_addr;
    private ObjectMapper objectMapper;

    public LambdaService(RestTemplate restTemplate , ObjectMapper objectMapper,  @Value("${lambda.address}") String lambda_addr){
        this.restTemplate = restTemplate;
        this.lambda_addr = lambda_addr;
        this.objectMapper = objectMapper;
    }

    private  HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

//    @Async
    public String publishIoTData(VesselIoTData data) throws JsonProcessingException {
        Event event = new Event("IOT_DATA_UPDATE" , "0002");
        ObjectNode payload = objectMapper.createObjectNode();
        payload.putPOJO("event" , objectMapper.writeValueAsString(event));
        payload.putPOJO("context" , objectMapper.writeValueAsString(data));
        HttpEntity requestEntity = new HttpEntity(payload.toString(), getHeaders());
        logger.info(payload.toString());
        ResponseEntity<String> response = restTemplate.exchange(lambda_addr , HttpMethod.POST , requestEntity , String.class);
        logger.info(response.getBody().toString());
        return  response.getBody().toString();
    }

    //    @Async
    public String publishIoTStatus(String vid , String vStatus , String timeStamp) throws JsonProcessingException {
        Event event = new Event("VESSEL_STATUS_UPDATE" , "0002");
        ObjectNode payload = objectMapper.createObjectNode();
        payload.putPOJO("event" , objectMapper.writeValueAsString(event));
        Map<String,String> mp = new HashMap<String,String>();
        mp.put("vid" , vid);
        mp.put("status" , vStatus);
        mp.put("timeStamp" , timeStamp);
        payload.putPOJO("context" , objectMapper.writeValueAsString(mp));
        HttpEntity requestEntity = new HttpEntity(payload.toString(), getHeaders());
        logger.info(payload.toString());
        ResponseEntity<String> response = restTemplate.exchange(lambda_addr , HttpMethod.POST , requestEntity , String.class);
        logger.info(response.getBody().toString());
        return  response.getBody().toString();
    }
    @Async
    public String publishIoTDelayEvent(Track track , long dxMs , long dyMs , String timeStamp) throws JsonProcessingException {
        Event event = new Event("VESSEL_DELAY" , "1113");
        ObjectNode payload = objectMapper.createObjectNode();
        payload.putPOJO("event" , objectMapper.writeValueAsString(event));
        Map<String,Object> mp = new HashMap<String,Object>();
        mp.put("vid" , track.getVid());
        mp.put("reason" , "HUMAN_NOTIFICATION");
        mp.put("dx" , dxMs);
        mp.put("dy" , dyMs);
        mp.put("zoomInVal" , track.getZoomInVal());
        mp.put("startTime" , track.getStartTimeStamp());
        mp.put("status" , track.getStatus());
        mp.put("stepIndex" , track.getStepIndex());
        mp.put("steps" , CommonUtil.toStepPayloads(track.getSteps()));
        mp.put("unit_type" , "MILLs");
        mp.put("timeStamp" , timeStamp);
        payload.putPOJO("context" , objectMapper.writeValueAsString(mp));
        HttpEntity requestEntity = new HttpEntity(payload.toString(), getHeaders());
        logger.info(payload.toString());
        ResponseEntity<String> response = restTemplate.exchange(lambda_addr , HttpMethod.POST , requestEntity , String.class);
        logger.info(response.getBody().toString());
        return  response.getBody().toString();
    }
}
