package iot.domain;

import lombok.Data;
import sun.security.krb5.internal.crypto.Des;

import java.util.List;

@Data
public class Track {
    private String vid;
    private List<Destination> destinations;
    private List<Step>  steps;
}
