package iot.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.domain.VesselIoTData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class LambdaService {
    private Logger logger = LoggerFactory.getLogger(LambdaService.class);

    private RestTemplate restTemplate;
    private String lambda_addr;
    private ObjectMapper objectMapper;

    public LambdaService(RestTemplate restTemplate , ObjectMapper objectMapper, @Value("${lambda.address}") String lambda_addr){
        this.restTemplate = restTemplate;
        this.lambda_addr = lambda_addr;
        this.objectMapper = objectMapper;
    }

    private HttpHeaders getHeaders(){
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
    public void publishDelayEvent(String payload) throws JsonProcessingException {
        HttpEntity requestEntity = new HttpEntity(payload, getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(lambda_addr , HttpMethod.POST , requestEntity , String.class);
        logger.info(response.getBody().toString());
    }
}
