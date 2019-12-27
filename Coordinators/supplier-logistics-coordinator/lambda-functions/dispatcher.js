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

exports.lambda_handler = (event, context) => {
  try {
      const ret = await axios(url);
      var params = {
        TopicArn: 'arn:aws-cn:sns:cn-northwest-1:148543509440:context-sharing-input-channel',
        Message: JSON.stringify({
            'event': 'MESSAGE_TEXT_FROM_LAMBDA_of_NODEJS8.10'  
          }), /* required */
        MessageStructure: 'string'
      };
  
      var publishTextPromise = new AWS.SNS({apiVersion: '2010-03-31'}).publish(params).promise();
      publishTextPromise.then(
        function(data) {
          console.log('MessageID is ' + data.MessageId);
        }).catch(
          function(err) {
          console.error(err, err.stack);
      });

      var params = {
        TableName: 'snslambda',
        Item: {
            'date': {S:  new Date() }
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

      response = {
          'statusCode': 200,
          'body': JSON.stringify({
              message: 'dispatch finished',
              // location: ret.data.trim()
          })
      }
  } catch (err) {
      console.log(err);
      return err;
  }

  return response
};
