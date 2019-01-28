# Vessel IoT Hub
## Introduction
This system is introduced into L2L as `Vessel Business Entity`. It's a spring boot application and employs the AWS IoT technology to simulate the vessel.
### Prerequisites 
### How to run
- login the vm `ubuntu@ssp-vessel-iot`, then package and run the `vessel-iot-hub` project as follow:
```bash
# login the vm with the pwd
[bqzhu@pc ~]$ ssh ubuntu@ssp-vessel-iot
# clone the L2L-ICWS prj with the pwd
ubuntu@ssp-vessel-iot:~$ git clone git@github.com:i-qiqi/L2L-ICWS.git
# package and run the prj
ubuntu@ssp-vessel-iot:~$ cd L2L-ICWS/shipping-company/IoT-hubs/vessel-IoT-hub
ubuntu@ssp-vessel-iot:~/L2L-ICWS/shipping-company/IoT-hubs/vessel-IoT-hub$ mvn clean install -DskipTests spring-boot:run

```

