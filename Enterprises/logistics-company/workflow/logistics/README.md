# Logistics
The Logistics is a enhanced workflow application with annotion-enable ability, based on Activiti.
### Prerequisites
### How to run
- login the vm `ubuntu@ssp-logistics`, then package and run the `logistics` project as follow:
```bash
# login the vm with the pwd
[bqzhu@pc ~]$ ssh ubuntu@ssp-logistics
# clone the L2L-ICWS prj with the pwd
ubuntu@ssp-logistics:~$ git clone git@github.com:i-qiqi/L2L-ICWS.git
# package and run the prj
ubuntu@ssp-logistics:~$ cd L2L-ICWS/logistics-company/workflow/logistics
ubuntu@ssp-logistics:~/L2L-ICWS/logistics-company/workflow/logistics$ mvn clean install -DskipTests spring-boot:run

```
- The `logistics` app server runs on `ssp-logistics:8000`
