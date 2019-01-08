package com.test.sms.controller;

import com.test.sms.rest.RestClient;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Controller()
public class MessageController {
    RestClient client = new RestClient();
    int counter = 1;

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.ok().body("Message status received at CRM");
    }

    @PostMapping("/apis-simulator/smsDeliveryNotification")
    public ResponseEntity<Object> updateMessageStatus(@RequestBody Map<String, ?> input) {
        String messageId = (String) input.get("callbackId");
        Integer messageStatus = (Integer) input.get("status");
        if (messageId == null || messageStatus == null) {
            System.out.println("MessageSid or MessageStatus parameters not supplied while calling /message/outbound API.");
            return ResponseEntity.badRequest().body("{\"message\":\"status and/or callbackId parameters not supplied\"}");
        }

        System.out.println("Message status received at crm:" + messageStatus + " with callbackId:" + messageId);

        return ResponseEntity.ok().body("Message status received at CRM");

    }

    @PostMapping("/apis-simulator/twilioApi")
    public ResponseEntity<Object> processMessage(@RequestParam String To, @RequestParam String From, @RequestParam String Body, @RequestParam(required = false) String StatusCallback) throws URISyntaxException {

        if (Body == null || From == null || To == null) {
            System.out.println("Body, From and/or To parameters not supplied while calling /message/outbound API.");
            return ResponseEntity.badRequest().body("{\"message\":\"Body, From and/or To parameters not supplied\"}");
        }

        counter++;
        if (StatusCallback != null) {
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    JSONObject jsObject = new JSONObject("{\"MessageSid\":\"" + counter + "\",\"MessageStatus\":\"delivered\"}");
                    //client.callPostApi(jsObject,StatusCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        System.out.println("Message queued,text:" + Body + " Number:" + To);
        return ResponseEntity.created(new URI("")).body("{\"message\":\"message queued successfuly\",\"sid\":\"" + counter + "\"}");
    }

    @PostMapping("/apis-simulator/ngrok")
    public ResponseEntity<Object> ngrok(@RequestParam String To, @RequestParam String MessageSid, @RequestParam String SmsStatus) throws URISyntaxException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MessageSid", MessageSid);
        jsonObject.put("MessageStatus", SmsStatus);
        String url = "http://192.168.1.234:8081/sms-client/delivery-notification";
        client.callPostApi(jsonObject, url, SmsStatus, MessageSid);
        return ResponseEntity.ok().body("{\"message\":\"message queued successfuly\",\"sid\":\"" + counter + "\"}");
    }

    @PostMapping("/sms-client/receive")
    public ResponseEntity<Object> processMessage(@RequestParam String To, @RequestParam String From, @RequestParam String Body) {

        if (Body == null || From == null || To == null) {
            System.out.println("Body, From and/or To parameters not supplied while calling /message/outbound API.");
            return ResponseEntity.badRequest().body("{\"message\":\"Body, From and/or To parameters not supplied\"}");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Body", Body);
        jsonObject.put("From", From);
        jsonObject.put("To", To);
        String url = "http://192.168.1.29:8081/sms-client/receive";
        // String url = "http://localhost:8080/sms-client/receive";
        client.callPostApi(jsonObject, url, Body, To, From);
        return ResponseEntity.ok().body("");
    }

}
