var AWS = require('aws-sdk');
AWS.config.update({
    accessKeyId: 'AKIASFFPCCPAGHQEP5QY',
    secretAccessKey: '8qmwxT2Jawr7W8gWTUysfpiKV7MLO5v7WA3tiBRe',
    region: 'cn-northwest-1'
});
var s3 = new AWS.S3();

var getParams = {
    Bucket: 'coordinator', //replace example bucket with your s3 bucket name
    Key: 'config.json' // replace file location with your s3 file location
}

//Fetch or read data from aws s3
s3.getObject(getParams, function (err, data) {

    if (err) {
        console.log(err);
    } else {
        var d = JSON.parse(data.Body.toString());
        console.log(data.Body.toString()); //this will log data to console
    }

})