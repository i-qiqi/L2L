package iot.domain;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class VesselIoTData implements Serializable {
    private String vid;
    private double longitude;
    private double latitude;
    private double speed;
    private String timeStamp; // Date String

    public VesselIoTData(String vid, double longitude, double latitude, double speed, String timeStamp) {
        this.vid = vid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.timeStamp = timeStamp;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public  VesselIoTData deepCopy(){
        return new VesselIoTData(this.vid , this.longitude , this.latitude , this.speed , this.timeStamp);
    }
}
