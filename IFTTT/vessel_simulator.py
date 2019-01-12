# -*- coding: utf-8 -*-
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


if __name__ == "__main__":
    send_apply()
