package iot.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iot.lambda.HandlerService;
import iot.lambda.LambdaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class LambdaHandler {
    private static final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);
    private LambdaService lambdaService;
    private HandlerService handlerService;
    public LambdaHandler(LambdaService lambdaService , HandlerService handlerService){
        this.lambdaService = lambdaService;
        this.handlerService = handlerService;
    }
    @RequestMapping(value = "/vessel/iot-data", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> IoTDataUpdate(@RequestBody HashMap<String, Object> mp) throws JsonProcessingException {
        logger.info(mp.toString());
        ObjectMapper objectMapper = new ObjectMapper();

//        return  ResponseEntity.status(HttpStatus.OK).body(null);
        return new ResponseEntity<String>(objectMapper.writeValueAsString(mp) , HttpStatus.OK);
    }

    @RequestMapping(value = "/vessel/delay", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> delay(@RequestBody HashMap<String, Object> mp) throws IOException {
        logger.info(mp.toString());
        ObjectMapper objectMapper = new ObjectMapper();
//        String payload = handlerService.tranformDelayEvent(mp);
//        logger.debug("payload of delay event to mlc : " + payload);
//        lambdaService.publishDelayEvent(payload);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/rendzvous-port", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> updateRend(@RequestBody HashMap<String, Object> mp) throws IOException {
        logger.info(mp.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(mp);
        logger.debug("event--RENDZVOUS_PORT_UPDATE : " + res);
        return new ResponseEntity<String>(res , HttpStatus.OK);
    }

    @RequestMapping(value = "/hello" , method = RequestMethod.GET)
    public String home() {
        logger.info("test rest api.");
        return "hello , manager!";
    }

}
