package org.flowable.ui.task.application.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class LambdaHandler {
    private static final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);
    private  LambdaService lambdaService;

    public LambdaHandler(LambdaService lambdaService){
        this.lambdaService = lambdaService;
    }
    @RequestMapping(value = "/vessel/iot-data", method = RequestMethod.POST , produces = "application/json")
    public String delay(@RequestBody HashMap<String, Object> mp) throws JsonProcessingException {

        return "success";
    }

}
