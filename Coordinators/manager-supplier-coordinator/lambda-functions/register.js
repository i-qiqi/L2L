 // const url = 'http://checkip.amazonaws.com/';
let response;
const YAML = require('js-yaml');
var AWS = require('aws-sdk');
var s3_util = require('./s3-util.js');
var ddb = new AWS.DynamoDB({params : {TableName : 'snsLambda'}});
// Set the region 
AWS.config.update({region: 'cn-northwest-1'});
// Create the DynamoDB service object
var ddb = new AWS.DynamoDB({apiVersion: '2012-08-10'});
//Create the SNS Client
var snsClient = new AWS.SNS({apiVersion: '2010-03-31'});
/**
 * send registration event to topic-bulletin
 */


exports.send = async (event, context) => {
  try { 
      var global_conf = null;
      var global_conf_promise = s3_util.readConfig(); //load global config
      await global_conf_promise .then(
        function (data) {
                conf = YAML.safeLoad(data.Body.toString(),'utf-8');
                console.log(conf);
                global_conf = conf.application;
        }).catch(
        function(err){
            console.log(err);
            return err;
        });
      console.log("global configure : ", global_conf);

      var params = {
        TopicArn: global_conf.output_channel,
        Message: JSON.stringify({
                "msg_Type": "Registration", 
                "member_Type": global_conf.type,
                "name": global_conf.name,
                "ID": global_conf.id,
                "business_Key": global_conf.business_key,
                "description": global_conf.description,
                "input_Channel": global_conf.input_channel          
            }), /*required */
        MessageAttributes: {
          'event_type': { 
            DataType: 'String', /* required */
            StringValue: 'registration'
          },
        },  
        MessageStructure: 'string'
      };

      console.log("params : ", params);
  
      var publishTextPromise = new AWS.SNS({apiVersion: '2010-03-31'}).publish(params).promise();
      await publishTextPromise.then(
            function(data) {
              console.log('MessageID is ' + data.MessageId);     
            }).catch(
            function(err) {
              console.error(err, err.stack);
              return err;
            });
      var response = {
        'statusCode': 200,
        'headers':{
          'from':'topic-bulletin'
        },
        'body': JSON.stringify({
            message: 'Registration event has been emitted',
        }),
        "isBase64Encoded": false
      }
      if(global_conf.ID !== 'none') {
          response.body = JSON.stringify({
            message: 'The coordinator is registered',
        });
      }
      console.log("response : ", response);
      return response;  
  } catch (err) {
      console.log(err);
      return err;
  }
};
    
/**
 * receive the results(the generated uuid) from the topic-bulletin
 * 
 */
exports.receive = async (event,context) => {
    try{
        //extract event to set the coordinator uuid.
        var global_conf = null;
        var global_conf_promise = s3_util.readConfig(); //load global config
        await global_conf_promise.then((data) =>{
          global_conf = YAML.safeLoad(data.Body.toString(),'utf-8');
        })
        console.log("global configure : ", global_conf );

        var msg = JSON.parse(event.Records[0].Sns.Message);
        console.log("MSC:SET-UUID: recevie => ", msg); 
        console.log("Message Attributtes : ", event.Records[0].Sns.MessageAttributes);

        global_conf.application.id = msg.coordinator_Uuid; // update the conf
        var write_conf_promise = s3_util.writeConfig(global_conf);// write back to the config
        await write_conf_promise.then((data)=>{
          console.log(data); 
        }).catch((err)=>{
          console.log(err);
        })

        global_conf_promise = s3_util.readConfig(); // reload the conf to see the differences.
        await global_conf_promise.then((data)=>{
          global_conf = YAML.safeLoad(data.Body.toString(),'utf-8');
        })

        //re-read the application.yaml to check the new value.
        console.log("UUID is generated : ", global_conf);
    }catch(err){
        console.log(err);
        return err;
    }
}
