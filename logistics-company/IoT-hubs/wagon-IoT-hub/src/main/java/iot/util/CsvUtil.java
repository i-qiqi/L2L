package iot.util;

import com.csvreader.CsvReader;
import iot.domain.Destination;
import iot.domain.Location;
import iot.domain.VesselIoTData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class CsvUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    /**
     * 读取轮船轨迹数据 required all cells of string Type
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<VesselIoTData> readTracjectory(String filePath) throws IOException {
        List<VesselIoTData> vesselStates= new ArrayList<VesselIoTData>();
        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath);
        if (csvReader.getHeaders() != null){
            logger.debug(csvReader.getHeaders().toString());
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
        String line = null;
        int i = 0;
        while((line=reader.readLine())!=null){
            String[] items= line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
            Double lng = Double.parseDouble(items[0].trim());
            Double lat = Double.parseDouble(items[1].trim());
            String timeStamp = items[2].trim();
            Double velocity = Double.parseDouble(items[3].trim());
            VesselIoTData vesselState = new VesselIoTData(null , lng, lat , velocity , timeStamp);
            vesselStates.add(vesselState);
        }
        return  vesselStates;
    }
    public static List<Destination> readDestinations(String filePath) throws IOException {
        List<Destination> destinations= new ArrayList<Destination>();
        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath);
        if (csvReader.getHeaders() != null){
            logger.debug(csvReader.getHeaders().toString());
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;
        int i = 0;
        while((line=reader.readLine())!=null){
            String[] items= line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
            String dName = items[0].trim();
            Long annchoringDuration = Long.parseLong(items[1].trim());
            Long dockingDuration = Long.parseLong(items[2].trim());
            Destination d = new Destination(dName , annchoringDuration , dockingDuration);
            destinations.add(d);
        }
        return  destinations;
    }


    public static List<Location> readLocations(String filePath) throws IOException {
        List<Location> locations= new ArrayList<Location>();
        // 创建CSV读对象
        CsvReader csvReader = new CsvReader(filePath);
        if (csvReader.getHeaders() != null){
            logger.debug(csvReader.getHeaders().toString());
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;
        int i = 0;
        while((line=reader.readLine())!=null){
            String[] items= line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
            String name = items[0].trim();
            Double lng = Double.parseDouble(items[1].trim());
            Double lat = Double.parseDouble(items[2].trim());
            Location loc = new Location(name , lng, lat);
            locations.add(loc);
        }
        return  locations;
    }

}
