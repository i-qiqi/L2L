## Coordinator template  Sample
- 在配置中心，协调器本地配置文件application.yaml:
```yaml
coordinator:
    uuid: null
```
- 代表协调器实例的协调器模板
- 采用DynamoDB存储协调器
```yaml
application:
    name: Logistics Vessel coordinator
    id: l2l:coordinator:lvc:cbd8b7d0-8689-11e9-af93-e94a4331c552
    type: coordinator
    bussiness_key: lvc
    description : "Coordinator between vessel and logistics company"
    services:
      - xxFunction:
        trigger_events:
          - 
             topic_in: A_in
             type: SNS
             filter: none
          - 
            topic_default_in: topic_default_in
            type: SNS
            filter: none
            topic_default_out: topic_default_out
```
## Coordinator API
### 协调器注册
- [API－Backend](https://github.com/awslabs/serverless-application-model/blob/master/examples/2016-10-31/api_backend/template.yaml)

当用
- 带参: 初始的服务集合不为空
- 不带参: 初始服务集合为空
```nodejs
＃ register_coord.js
＃ POST /coord/register/
```
### 协调器服务发布与更新
- POST: 创建服务-->新的服务引入，会使新的参与者有机会参与到该协调器负责的协作中来。
- PUT: 更新服务-->
- DELETE：删除服务-->会导致与该协调器所负责的所有参与者的协作关系被解除
- 
### 自动建立协作
- **二分图/二部图**: 一种特殊的网络流。
- 如果某个图为二分图，那么它至少有两个顶点，且其所有回路的长度均为偶数，任何无回路的的图均是二分图。
