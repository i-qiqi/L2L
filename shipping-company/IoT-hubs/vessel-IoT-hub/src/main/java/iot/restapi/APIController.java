package iot.restapi;

import com.amazonaws.services.iot.client.AWSIotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController()
public class APIController {
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    private VesselIoTSimulator vesselIoTSimulator;

    public APIController(VesselIoTSimulator vesselIoTSimulator){
        this.vesselIoTSimulator = vesselIoTSimulator;
    }
    @RequestMapping("/hello")
    public String home() {
        logger.info("test rest api.");
        return "hello , vessel-dev-A";
    }
    @RequestMapping("/start")
    public String start() throws InterruptedException, AWSIotException, IOException {
        logger.info("start vessel simulator...");
        vesselIoTSimulator.start();
        return "success";
    }
}
