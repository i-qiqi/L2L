package iot.simulator;

import com.amazonaws.services.iot.client.AWSIotException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import iot.domain.*;
import iot.lambda.LambdaPayload;
import iot.lambda.LambdaService;
import iot.repos.LocationRepository;
import iot.util.CsvUtil;
import iot.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class VesselSimulatorService {
    private static final Logger logger = LoggerFactory.getLogger(VesselSimulatorService.class);

    private  static final long DELAY_TIME_DEFAULT = 6; //delay duration at every port is 6h by default;
    private ObjectMapper objectMapper;

    private LocationRepository locationRepository;

    private VesselIoTSimulator vesselIoTSimulator;

    private  LambdaService lambdaService;

    public VesselSimulatorService(ObjectMapper objectMapper , LocationRepository locationRepository , LambdaService lambdaService){
        this.objectMapper = objectMapper;
        this.locationRepository = locationRepository;
        this.lambdaService = lambdaService;
    }

    public void setVesselIoTSimulator(VesselIoTSimulator vesselIoTSimulator) {
        this.vesselIoTSimulator = vesselIoTSimulator;
    }

    public Track readRawData(String vid) throws IOException {
        String dataPath = this.getClass().getResource("/").getPath() + "data/";
        logger.info("root path : " + dataPath);
        //Construct tracks
        Track t = new Track();
        List<Destination> destinations = CsvUtil.readDestinations(dataPath + "DE" + vid + ".csv");
        List<VesselIoTData> trackData = CsvUtil.readTracjectory(dataPath + "VS" + vid + ".csv");
        //split into steps
        int len = trackData.size();
        int k = 0;
        int dSize = destinations.size();
        Location loc = locationRepository.findLocation(destinations.get(k).getName());
        Step step = new Step();
        step.setPrePort("起始点");
        List<VesselIoTData> stepData = new ArrayList<VesselIoTData>();
        step.setVesselIoTData(stepData);
        List<Step> steps = new ArrayList<Step>();
        if (k < dSize) {
            step.setNextPort(loc.getName());
            step.setAnchoringDuration(destinations.get(k).getAnchoringDuration());
            step.setDockingDuration(destinations.get(k).getDockingDuration());
            steps.add(step);
            for (int j = 0; j < len; j++) {
                VesselIoTData vesselIoTData = trackData.get(j);
                vesselIoTData.setVid(vid);
                steps.get(k).getVesselIoTData().add(vesselIoTData);
                if (vesselIoTData.getLatitude() == loc.getLatitude() && vesselIoTData.getLongitude() == loc.getLongitude()) {
                    if (k >= dSize - 1) {
                        break;
                    }

                    //new step
                    step = new Step();
                    step.setPrePort(loc.getName());
                    stepData = new ArrayList<VesselIoTData>();
                    step.setVesselIoTData(stepData);

                    k++;
                    loc = locationRepository.findLocation(destinations.get(k).getName());
                    step.setNextPort(loc.getName());
                    step.setAnchoringDuration(destinations.get(k).getAnchoringDuration());
                    step.setDockingDuration(destinations.get(k).getDockingDuration());
                    steps.add(step);
                }
            }
        }
        t.setVid(vid);
        t.setSteps(steps);
        logger.info("track is loaded completely : "+ t.toString());
        for(int i = 0 ; i < steps.size() ; i++){
            //compute duration in step;
            List<VesselIoTData> stepIoTData = steps.get(i).getVesselIoTData();
            long duration = DateUtil.TimeMinus(stepIoTData.get(stepIoTData.size()-1).getTimeStamp() , stepIoTData.get(0).getTimeStamp());
            steps.get(i).setVoyagingDuration(duration);
        }

        return t;
    }

    public void initIoTData() throws InterruptedException, IOException, AWSIotException {

        Track track = vesselIoTSimulator.getTrack();
        List<Step> steps = track.getSteps();
        long startMs = new Date().getTime();
        track.setStartTimeStamp(DateUtil.ms2dateStr(startMs));
        track.setStepIndex(0);
    }

    public void reportStep() throws InterruptedException, AWSIotException, JsonProcessingException {
        Track track = vesselIoTSimulator.getTrack();
        int stepIdx = track.getStepIndex();
        Step curStep = track.getSteps().get(stepIdx);
        List<VesselIoTData> stepIoTData = curStep.getVesselIoTData();
        int size = stepIoTData.size();
        int i = 0;
        long x = 0;
        long y = 0;
        track.setStatus(VesselIoTStatus.VOYAGING);
        //TODO: to notify xxx of vessel status : VOYAGING
//        lambdaService.publishIoTStatus(track.getVid(), VesselIoTStatus.VOYAGING ,
//                DateUtil.translate2simuDateStr(track.getStartTimeStamp() , new Date().getTime() , track.getZoomInVal()));

        while (i < size) {
            x = System.currentTimeMillis();
            VesselIoTData curVesselState = stepIoTData.get(i).deepCopy();// deep copy to avoid modifying the vesselStates map
            long sleepMs = 0;
            //TODO:calculate elapse time between current state and next state
            if (i < size - 1) {
                long curStateMs = DateUtil.str2date(stepIoTData.get(i).getTimeStamp()).getTime();
                long nextStateMs = DateUtil.str2date(stepIoTData.get(i + 1).getTimeStamp()).getTime();
                sleepMs = nextStateMs - curStateMs;
            }
            //TODO: modify the date in vessel state.
            long curMs = new Date().getTime();
            long startMs = DateUtil.str2date(track.getStartTimeStamp()).getTime();
            String newStateTime = DateUtil.ms2dateStr(startMs + (curMs - startMs) * track.getZoomInVal());
            if(stepIdx == 0 && i == 0){
                curVesselState.setTimeStamp(track.getStartTimeStamp());
            }else{
                curVesselState.setTimeStamp(newStateTime);
            }
            logger.info("step < "+stepIdx+" : "+ size + " : " +i+ " > | "+ "vessel-iot-data : "+curVesselState );
            //TODO: send vessel iot data to lambda function -- iot-consumer-function
//            lambdaService.publishIoTData(curVesselState);

            i++;
            y = x;
            Thread.sleep(sleepMs / track.getZoomInVal());
            y = System.currentTimeMillis();
            logger.info((y-x)+"ms");
        }


        if (stepIdx <= track.getSteps().size() - 1) {
            //TODO: Determine if the status is  anchoring or docking
            ObjectNode payloadObjectNode = objectMapper.createObjectNode();
            if (curStep.getAnchoringDuration() == 0) {
                logger.info("Transiting into Docking status straightly");
                track.setStatus(VesselIoTStatus.DOCKING);
                //TODO: update vessel vessel-iot status "DOCKING"  to lambda function -- iot-consumer-function
//                lambdaService.publishIoTStatus(track.getVid(), VesselIoTStatus.DOCKING ,
//                        DateUtil.translate2simuDateStr(track.getStartTimeStamp() , new Date().getTime() , track.getZoomInVal()));

            } else {
                logger.info("Transiting into Anchoring status");
                payloadObjectNode.put("msgType", VesselIoTStatus.ANCHORING);
                track.setStatus(VesselIoTStatus.ANCHORING);
                //TODO: update vessel vessel-iot status "ANCHORING" to lambda function -- iot-consumer-function
//                lambdaService.publishIoTStatus(track.getVid(), VesselIoTStatus.ANCHORING ,
//                        DateUtil.translate2simuDateStr(track.getStartTimeStamp() , new Date().getTime() , track.getZoomInVal()));
            }
        }
    }


    public void simuAD() throws JsonProcessingException {
        //TODO: Timing simulation of anchoring and docking status of the ship
        Track track = vesselIoTSimulator.getTrack();
        int stepIdx = track.getStepIndex();
        Step curStep = track.getSteps().get(stepIdx);
        long zoomVal = track.getZoomInVal();
        long startMs = DateUtil.str2date(track.getStartTimeStamp()).getTime();
        long enterASimuMs = DateUtil.translate2simuMs(startMs ,new Date().getTime(),  zoomVal); // record the the time entering into anchoring / docking
        long enterDSimuMs = enterASimuMs;
        while (true) {
            long curMs =DateUtil.translate2simuMs (startMs , new Date().getTime() , zoomVal);
            long nextMs = curMs + 1000 * zoomVal;
            if (track.getStatus().equals(VesselIoTStatus.ANCHORING)) {
                //update end time of anchoring , maybe the anchoring duration is updated
                long newReachMs = enterASimuMs + curStep.getAnchoringDuration(); // hour to ms
                logger.info("Current time : " + DateUtil.ms2dateStr(curMs) + " Next time : " + DateUtil.ms2dateStr(nextMs) + "new reach time : " + DateUtil.ms2dateStr(newReachMs));

                if (newReachMs > curMs && newReachMs <= nextMs) {
                    //TODO: update vessel vessel-iot status "ANCHORING" to lambda function -- iot-consumer-function
//                    lambdaService.publishIoTStatus(track.getVid(), VesselIoTStatus.DOCKING ,
//                            DateUtil.translate2simuDateStr(track.getStartTimeStamp() , new Date().getTime() , track.getZoomInVal()));
                    logger.info("anchrong end ...");
                    enterDSimuMs = DateUtil.translate2simuMs(startMs ,new Date().getTime(),  zoomVal); // record the the time entering into anchoring / docking
                    track.setStatus(VesselIoTStatus.DOCKING);
                    logger.info("enter docking ...");

                }
            } else if (track.getStatus().equals(VesselIoTStatus.DOCKING)) {
                long newDepartureMs = enterDSimuMs + curStep.getDockingDuration(); // hour to ms
                logger.info("Current time : " + DateUtil.ms2dateStr(curMs) + " Next time : " + DateUtil.ms2dateStr(nextMs) + " New arrival time : " + DateUtil.ms2dateStr(newDepartureMs));
                if (newDepartureMs > curMs && newDepartureMs <= nextMs) {
                    //TODO: update vessel vessel-iot status "DOCKING"  to lambda function -- iot-consumer-function
                    logger.info("docking end ...");
                    break;
                }
            }
            int nextStepIndex = stepIdx + 1;
            track.setStepIndex(nextStepIndex);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Async
    public void start() throws InterruptedException, AWSIotException, IOException {

        Track track = vesselIoTSimulator.getTrack();
        int stepIdx = track.getStepIndex();
        List<Step> steps = track.getSteps();
        int size = steps.size();
        while (stepIdx < size) {
            reportStep();
            long x = System.currentTimeMillis();
            simuAD();
            long y = System.currentTimeMillis();
            logger.info((y-x)/1000+"s");
            track.setStatus(VesselIoTStatus.END);
            //TODO: update vessel vessel-iot status "ANCHORING" to lambda function -- iot-consumer-function
//            lambdaService.publishIoTStatus(track.getVid(), VesselIoTStatus.END ,
//                    DateUtil.translate2simuDateStr(track.getStartTimeStamp() , new Date().getTime() , track.getZoomInVal()));
        }
    }


    /**
     * change status
     *
     * @param changeStatusTopic
     * @param msgType
     * @param device
     */
    private void changeStatus(String changeStatusTopic, String msgType) {
        ObjectNode payloadObjectNode = objectMapper.createObjectNode();
        payloadObjectNode.put("msgType", msgType);
    }



    /**
     * update vessel Shadow
     *
     * @param rootNode
     */
    public void delay(String vid , JsonNode rootNode) throws IOException {
        JsonNode destinationsNode = rootNode.get("destinations");
        logger.info("--delay--"+destinationsNode.toString());
        if (destinationsNode!= null) {
            List<Destination> destinations = new ArrayList<Destination>();
            for(int i = 0 ; i < destinationsNode.size() ; i++){
                Destination d = objectMapper.readValue(destinationsNode.get(i).toString() , Destination.class);
                destinations.add(d);
            }
        }
    }


}
