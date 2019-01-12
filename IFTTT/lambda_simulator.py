# -*- coding: utf-8 -*-
"""
Created on Sat Jan 12 13:47:07 2019

@author: clhu
"""
from flask import Flask
from flask import request
import requests
import yaml
import json

app = Flask(__name__)


def handler(event, context):
    print(event, context)
    rules = yaml.load(open('rule.yaml', encoding='utf8'))
    event_type = event.get('event_type')
    channel_id = 0
    for recipe in rules.get('recipes'):
        if recipe.get('type') == event_type:
            channel_id = recipe.get("channel_id")
            break
    if not channel_id:
        return "error event type"

    host, port, url = None, None, None
    find = False
    for channel in rules.get("channels"):
        if channel.get("uuid") == channel_id:
            host, port, url = channel.get("host"), channel.get("port"), channel.get("url")
            find = True
    if not find:
        return "error channel id...not register"

    ret = requests.get("{}:{}{}".format(host, port, url), data=context)
    return ret


@app.route('/', methods=["POST", "GET"])
def lambda_simulator():
    print("enter lambda...")
    print(request.json)
    event = request.json.get("event")
    context = request.json.get("context")
    ret = handler(event, context)
    print("exit lambda...", ret)
    return json.dumps(ret.status_code, ensure_ascii=False)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=3000, debug=True)
