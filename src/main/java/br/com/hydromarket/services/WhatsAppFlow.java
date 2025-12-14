package br.com.hydromarket.services;

import br.com.hydromarket.components.WhatsAppProperties;
import br.com.hydromarket.dtos.whatsapp.webhook.Value;
import br.com.hydromarket.dtos.whatsapp.webhook.WhatsAppWebhookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WhatsAppFlow {
    private final WhatsAppProperties whatsAppProperties;

    public WhatsAppFlow(WhatsAppProperties whatsAppProperties) {
        this.whatsAppProperties = whatsAppProperties;
    }

    public boolean isVerifyTokenCorrect(String mode, String verifyToken) {
        return "subscribe".equals(mode) &&
                this.whatsAppProperties.getVerifyToken().equals(verifyToken);
    }

    public void messageReceived(WhatsAppWebhookDTO body) {
        if(log.isDebugEnabled()) {
            try {
                String pretty = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(body);

                log.debug(pretty);
            }
            catch(JsonProcessingException exception) {
                log.debug(body.toString());
            }
        }

        Value value = body.entry().getFirst()
                .changes().getFirst()
                .value();

        boolean isMessageReceivedBody = value.statuses() == null;

        if(isMessageReceivedBody) {
            String message = value.messages().getFirst()
                .text()
                .body();

            log.debug(">>> Message: {}", message);
        }
        else {
            log.debug(">>> Status: {}", value.statuses().getFirst().status());
        }
    }
}
