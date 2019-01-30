package iot.restapi;

import com.amazonaws.services.iot.client.AWSIotException;
import com.fasterxml.jackson.core.JsonProcessingException;
import iot.domain.Step;
import iot.domain.Track;
import iot.domain.VesselIoTStatus;
import iot.lambda.LambdaService;
import iot.simulator.VesselIoTSimulator;
import iot.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class APIController {
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    private VesselIoTSimulator vesselIoTSimulator;

    private LambdaService lambdaService;


    public APIController(VesselIoTSimulator vesselIoTSimulator , LambdaService lambdaService){
        this.vesselIoTSimulator = vesselIoTSimulator;
        this.lambdaService = lambdaService;
    }
    @RequestMapping("/hello")
    public String home() {
        logger.info("test rest api.");
        return "hello , vessel-iot-hub";
    }
    @RequestMapping(value = "/start")
    public String start() throws InterruptedException, AWSIotException, IOException {
        logger.info("start vessel simulator...");
        vesselIoTSimulator.start();
        return "success";
    }

    @RequestMapping(value = "/delay", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<String> delay(@RequestBody HashMap<String, Object> mp) throws JsonProcessingException {
        int dx = Integer.parseInt(mp.get("dx").toString());
        int dy = Integer.parseInt(mp.get("dy").toString());
        Track track = vesselIoTSimulator.getTrack();
        String vStatus = track.getStatus();
        logger.info("Notification from Human : dx = "+dx +" , dy = "+dy+" when vessel-iot status : "+vStatus);

        switch (vStatus){
            case VesselIoTStatus.VOYAGING :
                delayOnVoyaging(dx , dy);
                break;
            case VesselIoTStatus.ANCHORING :
                break;
            case VesselIoTStatus.DOCKING :
                break;
            case VesselIoTStatus.END :

            default:  return new ResponseEntity<String>("{\"Tips\":\"vessel-iot status is "+vStatus+"\"}" , HttpStatus.OK);
        }
        // delay/postpone event to coordinator
        return new ResponseEntity<String>("{\"status\":\"ok\"}" , HttpStatus.OK);
    }

    public void delayOnVoyaging(int dx , int dy) throws JsonProcessingException { //dx , dy--hour
        Track track = vesselIoTSimulator.getTrack();
        long dxMs = dx * 60 * 60 * 1000;
        long dyMs = dy * 60 * 60 * 1000;
        int curStepIdx = track.getStepIndex();
        List<Step> steps = track.getSteps();
        int zoomInVal = track.getZoomInVal();
        String res = null;
        Step curStep = steps.get(curStepIdx);
        if ((dxMs + curStep.getAnchoringDuration()) >= 0 && (dxMs + curStep.getDockingDuration()) >= 0) {
            curStep.setAnchoringDuration(dxMs + curStep.getAnchoringDuration());
            curStep.setDockingDuration(dxMs + curStep.getDockingDuration());

            String timeStamp =  DateUtil.translate2simuDateStr(track.getStartTimeStamp(), new Date().getTime(), zoomInVal);
            lambdaService.publishIoTDelayEvent(track , dxMs , dyMs ,timeStamp);
        }
    }

}
