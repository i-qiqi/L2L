# Manager
The `Manager` is a enhanced workflow application with annotion-enable ability, based on [Activiti](https://www.activiti.org/).
### Prerequisites
### How to run
- login the vm `ubuntu@ssp-manager`, then package and run the `manager` project as follows:
```sh
# login the vm with the pwd
[bqzhu@pc ~]$ ssh ubuntu@ssp-manager
# clone the L2L-ICWS prj with the pwd
ubuntu@ssp-manager:~$ git clone git@github.com:i-qiqi/L2L-ICWS.git
# package and run the prj
ubuntu@ssp-manager:~$ cd L2L-ICWS/shipping-company/workflow/manager
ubuntu@ssp-manager:~/L2L-ICWS/shipping-company/IoT-hubs/vessel-IoT-hub$ mvn clean install -DskipTests spring-boot:run
```
- The `manager` app server runs on `ssp-manager:8000`
