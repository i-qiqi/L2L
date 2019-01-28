package iot.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import iot.conf.RestTemplateConfig;
import iot.domain.VesselIoTData;
import iot.simulator.VesselSimulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class LambdaService {
    private static final Logger logger = LoggerFactory.getLogger(LambdaService.class);

    private RestTemplate restTemplate;

    private String lambda_addr;

    public LambdaService(RestTemplate restTemplate , @Value("${lambda.address}") String lambda_addr){
        this.restTemplate = restTemplate;
        this.lambda_addr = lambda_addr;
    }

    private  HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

//    @Async
    public String publishIoTData(VesselIoTData data){
        LambdaPayload lambdaPayload = new LambdaPayload();
        Event event = new Event("IOT_DATA_UPDATE" , "0002");
        lambdaPayload.setData(data);
        lambdaPayload.setEvent(event);
        HttpEntity requestEntity = new HttpEntity(data, getHeaders());
        ResponseEntity<LambdaPayload> response = restTemplate.exchange(lambda_addr , HttpMethod.POST , requestEntity , LambdaPayload.class);
        logger.debug(response.getBody().toString());
        return  response.getBody().toString();
    }
}
