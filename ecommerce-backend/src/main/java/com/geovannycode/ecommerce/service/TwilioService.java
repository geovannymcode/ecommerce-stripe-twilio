package com.geovannycode.ecommerce.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
    private static final Logger logger = LoggerFactory.getLogger(TwilioService.class);

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        logger.info("✅ Twilio initialized");
    }

    public void sendSMS(String toPhoneNumber, String message) {
        try {
            Message twilioMessage = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();

            logger.info("✅ SMS sent to: {} - SID: {}", toPhoneNumber, twilioMessage.getSid());

        } catch (Exception e) {
            logger.error("❌ Error sending SMS to: {}", toPhoneNumber, e);
            throw new RuntimeException("Error al enviar SMS: " + e.getMessage());
        }
    }

    public void sendWhatsApp(String toPhoneNumber, String message) {
        try {
            Message twilioMessage = Message.creator(
                    new PhoneNumber("whatsapp:" + toPhoneNumber),
                    new PhoneNumber("whatsapp:" + fromPhoneNumber),
                    message
            ).create();

            logger.info("✅ WhatsApp sent to: {} - SID: {}", toPhoneNumber, twilioMessage.getSid());

        } catch (Exception e) {
            logger.error("❌ Error sending WhatsApp to: {}", toPhoneNumber, e);
            throw new RuntimeException("Error al enviar WhatsApp: " + e.getMessage());
        }
    }
}
