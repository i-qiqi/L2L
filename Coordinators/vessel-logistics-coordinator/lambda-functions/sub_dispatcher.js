 // const axios = require('axios')
// const url = 'http://checkip.amazonaws.com/';
let response;
var aws = require('aws-sdk')
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
// Set the region 
AWS.config.update({region: 'cn-northwest-1'});

// Create the DynamoDB service object
var ddb = new AWS.DynamoDB({apiVersion: '2012-08-10'});
//Create the SNS Client
var snsClient = new AWS.SNS({apiVersion: '2010-03-31'});

exports.sub_events_dispatch =  (event, context) => {
  try {
      // const ret = await axios(url);
      response = {
          'statusCode': 200,
          'body': JSON.stringify({
              message: 'hello world',
              // location: ret.data.trim()
          })
      }
  } catch (err) {
      console.log(err);
      return err;
  }

  return response
};
