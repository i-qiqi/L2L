 // const axios = require('axios')
// const url = 'http://checkip.amazonaws.com/';
let response;
const chinaTime = require('china-time');
var aws = require('aws-sdk')
var fs = require('fs')
var YAML = require('yamljs');

var ddb = new aws.DynamoDB({params : {TableName : 'snsLambda'}})
/**
 *
 * Event doc: https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html#api-gateway-simple-proxy-for-lambda-input-format
 * @param {Object} event - API Gateway Lambda Proxy Input Format
 *
 * Context doc: https://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-context.html 
 * @param {Object} context
 *
 * Return doc: https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
 * @returns {Object} object - API Gateway Lambda Proxy Output Format
 * 
 */
var AWS = require('aws-sdk');

// Create the DynamoDB service object
var ddb = new AWS.DynamoDB({apiVersion: '2012-08-10'});
//Create the SNS Client
var snsClient = new AWS.SNS({apiVersion: '2010-03-31'});
// Set the region 
AWS.config.update({region: 'cn-northwest-1'});
const env = {
    "S3_KEY": "resources/application.yaml"
}
console.log("S3_KEY: ", env.S3_KEY)
var tb_Conf = YAML.parse(fs.readFileSync(env.S3_KEY).toString());
console.log("conf : ",tb_Conf.topic_Bulletin.db_Tables);
var db_Tables = tb_Conf.topic_Bulletin.db_Tables;
exports.lambda_handler = (event, context) => {
  try {
      console.log(event);
      var msg = event.Records[0].Sns.Message;
      //if event is type of 'Registration', 
      if(msg.msg_Type === "Registration" ){
        console.log("Registration --> msg :",msg);
        var AWS = require('aws-sdk');
        var params = {
          TableName: db_Tables[1].coordinators.name,
          Key: {
            'id': {S: 'id123'}
          },
          ProjectionExpression: 'bussiness_key'
        };
        ddb.getItem(params, function(err, data) {
          console.log("item data : ",data);
          if (err) {
            console.log('Error', err);
          } else {
            console.log('Success', data);
            if(JSON.stringify(data) === '{}'){
              //register into AWS topic-bulletin
              console.log("register into AWS topic-bulletin");
              // putItem(msg);
            }
          }
        });
        
        
    }else{
      console.log("the type of event is wrong, should be ", msg.msg_Type);
    }
  } catch (err) {
      console.log(err);
  }
};

let putItem = function(msg){
  //check if the member has existed
  var params = {
    TableName: db_Tables[1].coordinators.name,
    Item: {
      'id' : {S: msg.ID},
      'name' : {S:  msg.name},
      'bussiness_key' : {S: msg.bussiness_Key},
      'description' : {S: msg.description}, 
      'timestamp': {S: chinaTime().toString()}
    }
  };
  // Call DynamoDB to add the item to the table
  ddb.putItem(params, function(err, data) {
    if (err) {
      console.log("Error", err);
    } else {
      console.log("Success", data);
    }
  })
}
