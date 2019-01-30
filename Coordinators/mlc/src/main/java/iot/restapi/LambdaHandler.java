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
    private  HandlerService handlerService;

    public LambdaHandler(LambdaService lambdaService , HandlerService handlerService){
        this.lambdaService = lambdaService;
        this.handlerService = handlerService;
    }
    @RequestMapping(value = "/vessel/decision", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> rendzvousDecision(@RequestBody HashMap<String, Object> mp) throws IOException {
        logger.info(mp.toString());

        //TODO : decision code ...

        //send decision result event to manager
        String res =  handlerService.createRendUpdateEvent2manager(mp);
        lambdaService.publishRendUpdateEvent("2221" , res);

        //send decision result event to logistics
        res = handlerService.createRendUpdateEvent2logistics(mp);
        lambdaService.publishRendUpdateEvent("2222" , res);

//        return  ResponseEntity.status(HttpStatus.OK).body(null);
        return new ResponseEntity<String>(res , HttpStatus.OK);
    }

    @RequestMapping(value = "/hello" , method = RequestMethod.GET)
    public String home() {
        logger.info("test rest api.");
        return "hello , mlc";
    }

}
