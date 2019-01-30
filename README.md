[TOC]
# L2L Framework

The experiment aims to demonstrate the L2L framework proposed in our paper how to achieve on-the-fly collaboration of legacy business process systems in an open environment in a nonintrusive, lightweight and flexible manner. The architecure of L2L is as follows: :point_down:
<center>
<img src="images/L2L-ICWS-Ifttt-events.jpg">
<strong></strong>
</center>

Initially , our business scenario originated from  Ship Spare Parts (SSP) problem in China shipping company. There are four participants, namely `Shipping Company` ,`Supplier Company` and `Logistics Company`. Their enterprise legacy systems are isolated and heterogeneous, like the cubic nodes shown in the architecture diagram.The current enterprise legacy system is mainly composed of `EISs`, `Traditional Workflow` and other `white components` inside nodes. The `Traditional workflow` is dedicated to handle the intra-enterprise business , and can not be directly integrate with external enterprise systems and internal emerging components, like IoT infrastructure. On the premise of maintaining highly autonomy of each system, L2L introduces some new components and concepts to bridge different enterprise systems so as to facilitate information comunication and cross-enterprise business decision-making. Inside the cubic nodes, such as  `Shipping Company`,  the grey components are newly introduced, which is in charge of internal or external collaboration.
- ***IoT Hub*** : `IoT Hub` manages an IoT device as a single `Business Entity` and allows `Human Interaction`.
- ***Serverless Funtions*** : We implements **`IFTTT`**,**`Collaboration Service`** by `Serverless Function Framwork`
- ***Context Sharing*** : Enterprises can selectively expose internal information to the outside world via `Context Sharing`
- ***Event Gateway*** :
- ***Annotation-enable Workflow*** : We extend the traditional workflow to facilitate business process collaboration across diverse enterprises, and patch annotations on  the process definition to flexibly  adapt to emerging business scenarios , not only SSP problem , but also cold chain transportation, IoT scenarios. To see more [here](annotation.md).
- ***Inra-enterprise Coordinator*** : eg. **`vmc`** component
- ***Inter-enterprise Coordinator*** : such as **`Manager-Logistics Coordinator(MLC)`** , they can perceive the event from **`IFTTT`** and deal with them by `decision-making`.
- ***Policy Repository*** ：
## Quick Start

### Prerequisite

If you want to quickly setup all the components and try out,  The prerequisite is that **5 virtual machines (ubuntu 18.04,8cores+16G)** need to be prepared, and [JDK8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), [Maven](http://maven.apache.org/) and other softwares need to be installed on each node.
- add these hosts to /etc/hosts.

	 host | ip
	-- | --
	ssp-manager | xxx
	ssp-vessel-iot | xxx
	ssp-logistics | xxx
	ssp-mlc | xxx
	ssp-lambda | xxx  |  

### Detailed Guide

#### Setup the L2L cluster environment

| Sr. No. | Step details           |                    Link                    |
|:------- |:---------------------- |:------------------------------------------:|
| 1       | Setup `vessel-iot-hub` |        [Link](/shipping-company/IoT-hubs/vessel-IoT-hub/README.md)         |
| 2       | Setup `manager`        | [Link](/shipping-company/workflow/manager/README.md) |
| 3       | Setup `mlc`        |       [Link](/static_assets/test.md)       |
| 4       | Setup `logistics`        |       [Link](/static_assets/test.md)       |
|5|Setup `aws-lambda`|[Link](/static_assets/test.md)|

#### Examples for Integration Testing With IFTTT
