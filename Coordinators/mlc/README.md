# Manager-Logistics Coordinator
### Prerequisites
### How to run
- login the vm `ubuntu@ssp-mlc`, then package and run the `mlc` project as follow:
```bash
# login the vm with the pwd
[bqzhu@pc ~]$ ssh ubuntu@ssp-mlc
# clone the L2L-ICWS prj with the pwd
ubuntu@ssp-mlc:~$ git clone git@github.com:i-qiqi/L2L-ICWS.git
# package and run the prj
ubuntu@ssp-mlc:~$ cd L2L-ICWS/Coordinators
ubuntu@ssp-mlc:~/L2L-ICWS/Coordinators/mlc$ mvn clean install -DskipTests spring-boot:run
```
- The `mlc` app server runs on `ssp-mlc:8000`
