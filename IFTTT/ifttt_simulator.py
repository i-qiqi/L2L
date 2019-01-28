# -*- coding: utf-8 -*-
"""
Created on Sat Jan 12 13:47:07 2019

@author: clhu
"""
from flask import Flask
from flask import request, abort
import requests
import yaml
import json

app = Flask(__name__)


def handler(event, context):
    """
    @event: dict
    @context: dict
    return: string
    """
    print("entering handler....", event, context)
    rules = yaml.load(open('rule.yaml', encoding='utf8'))

    event_id = event.get('event_id')
    channel_id = 0
    for recipe in rules.get('events'):
        if recipe.get('event_id') == event_id:
            channel_id = recipe.get("channel_id")
            break
    if not channel_id:
        return "error event id"

    find, host, port, url = False, None, None, None
    for channel in rules.get("channels"):
        if channel.get("uuid") == channel_id:
            find, host, port, url = True, channel.get("host"), channel.get("port"), channel.get("url")
    if not find:
        return "error channel id...not register"

    data = {"event":event, "context": context}
    ret = requests.post("http://{}:{}{}".format(host, port, url), data=data)

    print("exit handler....", ret)
    return ret

@app.route("/ping", methods=["GET", "POST"])
def ifttt_test():
    print("enter test...")
    print(request)
    print("exit test...")
    return json.dumps(request.json if request.json else {"status":"200"}, ensure_ascii=False)

@app.route('/', methods=["POST", "GET"])
def ifttt_simulator():
    print("enter ifttt...", request.json)

    if "event" not in request.json or "context" not in request.json:
        abort(400)
    ret = handler(request.json.get("event"), request.json.get("context"))

    print("exit ifttt...", ret)
    ret = {"return_message": ret} if ret else {"return_message": ""}
    return json.dumps(ret, ensure_ascii=False)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=3000, debug=True)
