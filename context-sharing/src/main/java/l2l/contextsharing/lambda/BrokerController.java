package l2l.contextsharing.lambda;

import l2l.contextsharing.util.Message;
import l2l.contextsharing.util.MessageValidUtil;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import java.util.logging.Logger;

import static l2l.contextsharing.util.MessageReadUtil.readMessageFromJson;

@RestController//处理http请求，返回json格式
@RequestMapping//配置url，让该类下的所有接口url都映射在/users下
public class BrokerController {
    private static Logger log = Logger.getLogger("log");
   
    private ObjectMapper objectMapper = new ObjectMapper();
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

    @RequestMapping(value = "/broker", method = RequestMethod.POST)
    @ResponseBody
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SecurityException {
        String messagetype = request.getHeader("x-amz-sns-message-type");
        //Get the message type header.
//        String messagetype = request.getHeader("x-amz-sns-message-type");
        //If message doesn't have the message type header, don't process it.
        if (messagetype == null) {
            return;
        }

        int totalBytes = request.getContentLength();
        // Parse the JSON message in the message body
        // and hydrate a Message object with its contents
        // so that we have easy access to the name/value pairs
        // from the JSON message.
        Scanner scan = new Scanner(request.getInputStream());
        StringBuilder builder = new StringBuilder();
        while (scan.hasNextLine()) {
            String s = scan.nextLine();
            System.out.println(s);
            builder.append(s);
        }
        Message msg = readMessageFromJson(builder.toString());
        System.out.println(msg.getType());
        // The signature is based on SignatureVersion 1.
        // If the sig version is something other than 1,
        // throw an exception.
        if (msg.getSignatureVersion().equals("1")) {
            // Check the signature and throw an exception if the signature verification fails.
            if (MessageValidUtil.isMessageSignatureValid(msg)) {
                log.info(">>Signature verification succeeded");
            } else {
                log.info(">>Signature verification failed");
                throw new SecurityException("Signature verification failed.");
            }
        } else {
            log.info(">>Unexpected signature version. Unable to verify signature.");
            throw new SecurityException("Unexpected signature version. Unable to verify signature.");
        }

        // Process the message based on type.
        if (messagetype.equals("Notification")) {
            //Do something with the Message and Subject.
            //Just log the subject (if it exists) and the message.
            String logMsgAndSubject = ">>Notification received from topic " + msg.getTopicArn();
            if (msg.getSubject() != null) {
                logMsgAndSubject += " Subject: " + msg.getSubject();
            }
            logMsgAndSubject += " Message: " + msg.getMessage();
            //TODO : process message 
            
            JsonNode msgNode = objectMapper.readTree(msg.getMessage()); 
            String event = msgNode.path("event").asText();         
            log.info("event : "+event);
            log.info(logMsgAndSubject); 
        } else if (messagetype.equals("SubscriptionConfirmation")) {
            //You should make sure that this subscription is from the topic you expect. Compare topicARN to your list of topics
            //that you want to enable to add this endpoint as a subscription.

            //Confirm the subscription by going to the subscribeURL location
            //and capture the return value (XML message body as a string)
            Scanner sc = new Scanner(new URL(msg.getSubscribeURL()).openStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            log.info(">>Subscription confirmation (" + msg.getSubscribeURL() + ") Return value: " + sb.toString());
            //Process the return value to ensure the endpoint is subscribed.
        } else if (messagetype.equals("UnsubscribeConfirmation")) {
            //Handle UnsubscribeConfirmation message.
            //For example, take action if unsubscribing should not have occurred.
            //You can read the SubscribeURL from this message and
            //re-subscribe the endpoint.
            log.info(">>Unsubscribe confirmation: " + msg.getMessage());
        } else {
            //Handle unknown message type.
            log.info(">>Unknown message type.");
        }
        log.info(">>Done processing message: " + msg.getMessageId());
    }
}
