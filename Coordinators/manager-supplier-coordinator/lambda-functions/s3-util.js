const YAML = require('js-yaml');
var json2yaml = require('json2yaml');
var AWS = require('aws-sdk');
AWS.config.update({
    accessKeyId: 'AKIASFFPCCPAGLHYS4VW',
    secretAccessKey: '843Yg7BqKHjvkpMNaTUmk66imy1r7TUWXX4QXQRl',
    region: 'cn-northwest-1'
});
var s3 = new AWS.S3();
var params = {
    Bucket: 'l2l-conf-center', //replace example bucket with your s3 bucket name
    Key: 'coordinators/lvc/application.yaml' // replace file location with your s3 file location
}
//Fetch or read application.yaml from aws s3
exports.readConfig = async () => { 
    var conf = {};
    var getParams = {
        Bucket: params.Bucket, //replace example bucket with your s3 bucket name
        Key: params.Key // replace file location with your s3 file location
    }
    return s3.getObject(getParams).promise();
       // return conf.application;
}
 

exports.writeConfig = async(data) => {
    var yaml_doc = json2yaml.stringify(data);
    console.log("transformed yaml doc : ", yaml_doc)
    var putParams = {
        Body: yaml_doc,//Buffer.from('...') || 'STRING_VALUE' || streamObject,
        Bucket: params.Bucket, //replace example bucket with your s3 bucket name
        Key: params.Key // replace file location with your s3 file location
    }

    return s3.putObject(putParams).promise(); 
}

// read local files : 
// const env = {
//   "S3_KEY": "resources/application.yaml"
// }
// console.log("S3_KEY: ", env.S3_KEY)
// let doc = YAML.safeLoad(fs.readFileSync(env.S3_KEY, 'utf8'));

