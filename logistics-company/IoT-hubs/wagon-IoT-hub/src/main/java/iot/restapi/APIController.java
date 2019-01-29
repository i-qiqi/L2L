package iot.restapi;

import com.amazonaws.services.iot.client.AWSIotException;
import iot.simulator.VesselIoTSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class APIController {
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    private VesselIoTSimulator vesselIoTSimulator;

    public APIController(VesselIoTSimulator vesselIoTSimulator){
        this.vesselIoTSimulator = vesselIoTSimulator;
    }
    @RequestMapping("/hello")
    public String home() {
        logger.info("test rest api.");
        return "hello , vessel-iot-A";
    }
    @RequestMapping(value = "/start")
    public String start() throws InterruptedException, AWSIotException, IOException {
        logger.info("start vessel simulator...");
        vesselIoTSimulator.start();
        return "success";
    }

//    @RequestMapping(value = "/delay", method = RequestMethod.POST , produces = "application/json")
//    public ResponseEntity<String> delay(@RequestBody HashMap<String, Object> mp) throws JsonProcessingException {
//        int dx = Integer.parseInt(mp.get("dx").toString());
//        int dy = Integer.parseInt(mp.get("dy").toString());
//        Track track = vesselIoTSimulator.getTrack();
//        String vStatus = track.getStatus();
//        logger.info("Notification from Human : dx = "+dx +" , dy = "+" when vessel-iot status : "+vStatus);
//
//        switch (vStatus){
//            case VesselIoTStatus.VOYAGING :
//
//                break;
//            case VesselIoTStatus.ANCHORING :
//                break;
//            case VesselIoTStatus.DOCKING :
//                break;
//            case VesselIoTStatus.END :
//
//            default:  return new ResponseEntity<String>("{\"Tips\":\"vessel-iot status is "+vStatus+".\"" , HttpStatus.OK);
//        }
//
//        //TODO:modify vesselShadow and sync shadow to device
//        String vid = pvars.get("vid").toString();
//        VesselShadow vesselShadow = shadowRepository.findById(vid);
//        if (vesselShadow == null) {
//            return new ResponseEntity<String>("{\"Tips\":\"There is no corresponding process instance for this vessel identifier!\"" , HttpStatus.OK);
//        }
//        List<Destination> destinations = vesselShadow.getDestinations();
//        logger.info(destinations.toString());
//        //TODO : find current port
//        int curPortIndex = vesselShadow.getStepIndex();
//        //TODO : find current time
//        long zoomVal = commonRepository.getZoomInVal();
//        long simuMs = DateUtil.str2date(vesselShadow.getStartTime()).getTime();
//        long curMs = (new Date().getTime()-simuMs)*zoomVal+simuMs;
//        //TODO : when status is "Voyaging" or "Anchoring"
//        if(vesselShadow.getStatus().equals("Voyaging") || vesselShadow.getStatus().equals("Anchoring")) {
//
//            //TODO : when status is "Docking"
//        } else if(vesselShadow.getStatus().equals("Docking")) {
//            logger.debug("when status is \"Docking\"");
//            //TODO: can not set dy
//            if(dx != 0){
//                dx = 0;
//            }
//            long gapMs = 0;
//            for(int i = 0 ; i < destinations.size();i++){
//                String newEstiAnchoringTime = null;
//                String newEstiArrivalTime = null;
//                String newEstiDepartureTime = null;
//                Destination d = destinations.get(i);
//                if(i == curPortIndex ){
//                    newEstiAnchoringTime = d.getEstiAnchorTime();
//                    newEstiArrivalTime = d.getEstiArrivalTime();
//                    newEstiDepartureTime = DateUtil.date2str(DateUtil.
//                            transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + dy * 60 * 60 * 1000));
//                    gapMs = dy * 60 * 60 * 1000;
//                    //TODO: determine if the departure time is later than cur Ms
//                    if(DateUtil.str2date(newEstiDepartureTime).getTime() < curMs) {
//                        logger.debug("New estimate departure time is illegal : required to be later than current time.");
//                        return new ResponseEntity<String>("{\"Tips\":\"New estimate departure time is illegal : required to be later than current time.\"" , HttpStatus.OK);
//                    }
//
//                }
//
//                if(i > curPortIndex) {
//                    newEstiAnchoringTime = DateUtil.date2str(DateUtil
//                            .transForDate(DateUtil.str2date(d.getEstiAnchorTime()).getTime() + gapMs));
//                    newEstiArrivalTime = DateUtil.date2str(DateUtil
//                            .transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + gapMs));
//                    newEstiDepartureTime = DateUtil.date2str(DateUtil
//                            .transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
//                }
//
//                //TODO: update destination of shadow
//                d.setEstiAnchorTime(newEstiAnchoringTime);
//                d.setEstiArrivalTime(newEstiArrivalTime);
//                d.setEstiDepartureTime(newEstiDepartureTime);
//            }
//            //TODO : send destinations to vessel device.
//            logger.info("send destinations to vessel device.");
//            restClient.postDestinations(destinations , vid);
//
//            //TODO: notify logistic of "Planning"
//            HashMap<String , Object> msgBody = new HashMap<String , Object>();
//            msgBody.put("eventType" , "DELAY");
//            msgBody.put("phase" , "Docking");
//            msgBody.put("dy" , dy);
//            restClient.notifyMsg(pid , "Planning" , msgBody);
//            String msg = "";
//            if(dy != 0){
//                if(dy > 0){
//                    msg = "The ship will leave the port port after a delay of "+ dy +" hours";
//                }else{
//                    msg = "The ship will leave the port " + (-dy) + " hours in advance";
//                }
//                stompClient.sendDelayMsg("admin" , "/topic/vessel/delay" , pid , msg);
//                logger.debug(msg);
//            }
//        } else{
//            logger.debug("The current situation is not considered!(the current status of the vessel is ignored)");
//            return new ResponseEntity<String>("{\"Tips\":\"The current situation is not considered!(the current status of the vessel is ignored\")", HttpStatus.OK);
//        }
//
//        // delay/postpone event to coordinator
//        return new ResponseEntity<String>("{\"Tips\":\"ok\"}" , HttpStatus.OK);
//    }
//
//    public void delayOnVoyaging(int dx , int dy){ //dx , dy--hour
//        Track track = vesselIoTSimulator.getTrack();
//        int curStepIdx = track.getStepIndex();
//        List<Step> steps = track.getSteps();
//        int stepSize = steps.size();
//        long gapMs = -1;
//        long startMs =
//        int curMs = DateUtil.translate2simuMs(, new Date().getTime() , track.getZoomInVal() )
//        for(int i = curStepIdx ; i < stepSize ; i++){
//
//                if()
//        }
//        for (int i = 0; i < destinations.size(); i++) {
//            Destination d = destinations.get(i);
//            String newEstiAnchoringTime = null;
//            String newEstiArrivalTime = null;
//            String newEstiDepartureTime = null;
//
//            if (i == curPortIndex) {
//                newEstiAnchoringTime = d.getEstiAnchorTime();//EstiAnchoringTime 不变
//                newEstiArrivalTime = DateUtil.date2str(DateUtil.
//                        transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + dx * 60 * 60 * 1000));
//
//                //TODO:EstiArrivalTime + delay > currentTime --> New EstiArrivalTime must be later than the current time.
//                if(DateUtil.str2date(newEstiArrivalTime).getTime() < curMs) {
//                    logger.debug("New estimate arrival time is illegal ,  required to be later than current time");
//                    return new ResponseEntity<String>("{\"Tips\":\"New estimate arrival time is illegal ,  required to be later than current time.\"" , HttpStatus.OK);
//                }
//                gapMs = (dx+dy) * 60 * 60 * 1000;
//                newEstiDepartureTime = DateUtil.date2str(DateUtil.
//                        transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
//                //TODO:NewEstiDepartureTime > NewEstiArrivalTime --> NewEstiDepartureTime must be later than the NewEstiArrivalTime.
//                if(DateUtil.TimeMinus(newEstiDepartureTime , newEstiArrivalTime) < 0) {
//                    logger.debug("New estimate departure time is illegal ,  required to be later than estimate arrival time");
//                    return new ResponseEntity<String>("{\"Tips\":\"New estimate departure time is illegal ,  required to be later than estimate arrival time.\"" , HttpStatus.OK);
//                }
//            }
//
//            if (i > curPortIndex) {
//                newEstiAnchoringTime = DateUtil.date2str(DateUtil
//                        .transForDate(DateUtil.str2date(d.getEstiAnchorTime()).getTime() + gapMs));
//                newEstiArrivalTime = DateUtil.date2str(DateUtil
//                        .transForDate(DateUtil.str2date(d.getEstiArrivalTime()).getTime() + gapMs));
//                newEstiDepartureTime = DateUtil.date2str(DateUtil
//                        .transForDate(DateUtil.str2date(d.getEstiDepartureTime()).getTime() + gapMs));
//            }
//            //TODO: update destination of shadow
//            d.setEstiAnchorTime(newEstiAnchoringTime);
//            d.setEstiArrivalTime(newEstiArrivalTime);
//            d.setEstiDepartureTime(newEstiDepartureTime);
//        }
//
//        //TODO : send destinations to vessel device.
//        logger.debug("send destinations to vessel device.");
//        restClient.postDestinations(destinations , vid);
//
//        //TODO: notify logistic of "Planning"
//        HashMap<String , Object> msgBody = new HashMap<String , Object>();
//        msgBody.put("eventType" , "DELAY");
//        msgBody.put("phase" , vesselShadow.getStatus());
//        msgBody.put("dx" , dx);
//        msgBody.put("dy" , dy);
//        restClient.notifyMsg(pid , "Planning" , msgBody);
//
//        String anchoringMsg = "";
//        String dockingMsg = "";
//        String msg ="The ship will";
//        if(dx != 0){
//            if(dx > 0){
//                anchoringMsg = " arrive the port port after a delay of "+ dy +" hours";
//            }else{
//                anchoringMsg = " arrive the port " + (-dy) + " hours in advance";
//            }
//
//            msg += anchoringMsg;
//        }
//        if(dy != 0){
//            if(!msg.equals("The ship will")){
//                msg+= " and";
//            }
//            if(dy > 0){
//                dockingMsg = " leave the port port after a delay of "+ dy +" hours";
//            }else{
//                dockingMsg = " leave the port " + (-dy) + " hours in advance";
//            }
//            msg+=dockingMsg;
//        }
//
//        if(!msg.equals("The ship will")){
//            stompClient.sendDelayMsg("admin" , "/topic/vessel/delay" , pid , msg);
//            logger.debug(msg);
//        }
//    }
}
