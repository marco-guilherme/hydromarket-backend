package br.com.hydromarket.services;

import br.com.hydromarket.components.WhatsAppProperties;
import br.com.hydromarket.dtos.whatsapp.webhook.Message;
import br.com.hydromarket.dtos.whatsapp.webhook.Value;
import br.com.hydromarket.dtos.whatsapp.webhook.WhatsAppWebhookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WhatsAppService {
    private final RestClient restClient;
    private final WhatsAppProperties whatsAppProperties;
    private final Map<String, Map<String, String>> conversationStates;

    public WhatsAppService(RestClient.Builder builder, WhatsAppProperties whatsAppProperties) {
        this.restClient = builder.build();
        this.whatsAppProperties = whatsAppProperties;
        this.conversationStates = new ConcurrentHashMap<>();
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
            this.handleMessageReceived(body);
        }
        else {
            log.debug(">>> Status: {}", value.statuses().getFirst().status());
        }
    }

    public void sendMessage(String toTelephoneNumber, String message) {
        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", toTelephoneNumber,
                "type", "text",
                "text", Map.of(
                        "body", message
                )
        );

        log.info("Sending message to {}", toTelephoneNumber);

        restClient.post()
                .uri(this.whatsAppProperties.getSendMessageEndpoint())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.whatsAppProperties.getSystemUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private void handleMessageReceived(WhatsAppWebhookDTO body) {
        String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());

        String conversationIdentifier = body.entry().getFirst().identifier();

        String userWhatsAppNumber = body.entry().getFirst()
                .changes().getFirst()
                .value()
                .messages().getFirst()
                .from();

        Message message = body.entry().getFirst()
                .changes().getFirst()
                .value()
                .messages().getFirst();

        String reply = "TEST";

        if(message.type().equals("text")) {
            reply = message.text().body();
        }

        boolean isFirstConversationMessage = !this.conversationStates.containsKey(conversationIdentifier);

        Map<String, String> conversationContext = this.conversationStates.computeIfAbsent(conversationIdentifier, _ -> new ConcurrentHashMap<>());

        conversationContext.put("lastMessageSentByTheUserTimestamp", currentTimestamp);
        conversationContext.put("userWhatsAppNumber", userWhatsAppNumber);
        conversationContext.put("reply", reply);

        this.conversationStates.put(conversationIdentifier, conversationContext);

        log.debug("\n\n\n{}\n\n\n", this.conversationStates);

        this.sendMessage(userWhatsAppNumber, isFirstConversationMessage ? "Clique no catálogo" : ">>>>>>>>>: " + reply);
    }
}
