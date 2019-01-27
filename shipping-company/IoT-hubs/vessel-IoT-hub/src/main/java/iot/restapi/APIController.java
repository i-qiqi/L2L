package iot.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class APIController {
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);
    @RequestMapping("/hello")
    String home() {
        logger.info("test rest api.");
        return "hello , vessel-dev-A";
    }
}
