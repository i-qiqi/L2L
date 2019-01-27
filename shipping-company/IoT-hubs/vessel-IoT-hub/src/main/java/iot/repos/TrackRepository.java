package iot.repos;

import iot.domain.*;
import iot.util.CsvUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class TrackRepository {
    private static final Logger logger = LoggerFactory.getLogger(TrackRepository.class);
    private Track track;

    public TrackRepository(LocationRepository locationRepository , @Value("${vessel.vid}") String vid) throws IOException {
        String dataPath = this.getClass().getResource("/").getPath() + "data/";
        logger.debug("root path : " + dataPath);
        //Construct tracks
        Track t = new Track();
        List<String> destinations = CsvUtil.readDestinations(dataPath + "DE" + vid + ".csv");
        List<VesselIoTData> trackData = CsvUtil.readTracjectory(dataPath + "VS" + vid + ".csv");
        //split into steps
        int len = trackData.size();
        int k = 0;
        int dSize = destinations.size();
        Location loc = locationRepository.findLocation(destinations.get(k));
        Step step = new Step();
        step.setPrePort("起始点");
        List<VesselIoTData> stepData = new ArrayList<VesselIoTData>();
        step.setVesselIoTData(stepData);
        List<Step> steps = new ArrayList<Step>();
        if (k < dSize) {
            step.setNextPort(loc.getName());
            steps.add(step);
            for (int j = 0; j < len; j++) {
                VesselIoTData vesselIoTData = trackData.get(j);
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
                    loc = locationRepository.findLocation(destinations.get(k));
                    step.setNextPort(loc.getName());
                    steps.add(step);
                }
            }
        }
        t.setVid(vid);
        t.setDestinations(destinations);
        t.setSteps(steps);
        logger.debug("track");

    }
}
