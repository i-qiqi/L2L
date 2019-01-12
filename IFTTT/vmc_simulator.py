# -*- coding: utf-8 -*-
import json
from flask import Flask
from flask import request

app = Flask(__name__)


@app.route('/apply')
def hello_world():
    print("got request", request)
    print(request.json)
    return json.dumps("apply approved", ensure_ascii=False)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=3001, debug=True)
