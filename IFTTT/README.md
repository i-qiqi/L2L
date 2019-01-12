# IFTTT infrastructure
> if this, then that

As an infrasturcture, IFTTT stores its users/channels/recipes all in its database. We use one simple yaml file as its database in this demo.
```
%YAML 1.2
---
# design document is here:
# https://shimo.im/sheet/FQfqj1NMyDIhVmaU/RIDOC
# db file: contain users/channels/recipes
users:
- 
    name: vessel
    token: letmein
channels:
-
    uuid: 0001    
    name: VMC
    host: http://vmc.ssp.org
    port: 3001
    url: /apply
recipes:
-
    description: if apply spare part, then tell VMC
    type: APPLY
    user: vessel
    channel_id: 0001
```
## workflow

If you want to call the API provided by VMC, first you need to check these two things:

- VMC has registered as an IFTTT channel 
- You also have registered as an IFTTT user

Then you just need to call IFTTT by sending request with "event" and "context".
```python
import requests
import json
def send_apply():
    print("enter vessel apply...")
    lambda_url = "http://lambda.ssp.org:3000"
    data = {
        "event": {
            "event_type": "APPLY"
        },
        "context": {
            "spare_name": "ABC",
            "weight": 2000,
            "order": 2000,
        }
    }
    headers = {
        'content-type': 'application/json'
    }
    ret = requests.post(lambda_url, data=json.dumps(data, ensure_ascii=False), headers=headers)
    print("exit vessel apply with...", ret)
    return ret
```
